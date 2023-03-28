package com.mandalorian.chatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.databinding.FragmentMessageBinding
import com.mandalorian.chatapp.ui.adapters.MessageAdapter
import com.mandalorian.chatapp.viewModel.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MessageFragment : BaseFragment<FragmentMessageBinding>() {
    private lateinit var adapter: MessageAdapter
    override val viewModel: MessageViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_message
    private val args: MessageFragmentArgs by navArgs()

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        setupAdapter()

        binding?.run {
            viewModel.getUser(args.id)
            viewModel.person.observe(viewLifecycleOwner) { person ->
                tvUsername.text = person?.username ?: ""
            }
            viewModel.user.observe(viewLifecycleOwner) { user ->
                val lastSeen = user.lastSeen
                val date = Date(lastSeen)
                val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val lastSeenTime = formatter.format(date)
                if (user?.online == true) {
                    tvOnlineStatus.text = "Online ●"
                } else {
                    tvOnlineStatus.text = "Last seen at: ${lastSeenTime}"
                }
                Log.d("debugging", user.toString())
            }
            btnSend.setOnClickListener {
                val msg = etMessage.text.toString()
                etMessage.setText("")
                viewModel.sendMessage(args.id, msg)
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        viewModel.initializeUserStatus()

        lifecycleScope.launch {
            viewModel.getAllMessages(args.id).collect {
                adapter.setMessages(it.toMutableList())
            }
        }
    }

    fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = MessageAdapter(mutableListOf(), requireContext())

        binding?.rvMessages?.adapter = adapter
        binding?.rvMessages?.layoutManager = layoutManager

        viewModel.getAllMessages(args.id).onEach { messages ->
            adapter.items = messages.toMutableList()
            adapter.notifyDataSetChanged()
            binding?.rvMessages?.scrollToPosition(adapter.itemCount - 1)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}