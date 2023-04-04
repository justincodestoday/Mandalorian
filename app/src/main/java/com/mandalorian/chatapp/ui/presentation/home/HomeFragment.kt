package com.mandalorian.chatapp.ui.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.databinding.FragmentHomeBinding
import com.mandalorian.chatapp.ui.presentation.base.BaseFragment
import com.mandalorian.chatapp.ui.presentation.adapters.ChatAdapter
import com.mandalorian.chatapp.ui.presentation.home.viewModel.HomeViewModelImpl
import com.mandalorian.chatapp.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var adapter: ChatAdapter
    override val viewModel: HomeViewModelImpl by viewModels()
    override fun getLayoutResource() = R.layout.fragment_home

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        setupAdapter()

        binding?.run {
//            btnAdd.setOnClickListener {
//                val action = HomeFragmentDirections.actionHomeFragmentToMessageFragment()
//                NavHostFragment.findNavController(this@HomeFragment).navigate(action)
//            }
            btnCreateNotification.setOnClickListener {
                NotificationUtils.createNotification(
                    requireContext(),
                    "1st Notification",
                    "We are done"
                )
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.users.observe(viewLifecycleOwner) {
            adapter.setChats(it.toMutableList())
        }
    }

    fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = ChatAdapter(mutableListOf())
        adapter.listener = object : ChatAdapter.Listener {
            override fun onClick(item: User) {
                val action =
                    item.id.let { HomeFragmentDirections.actionHomeFragmentToMessageFragment(item.id) }
                NavHostFragment.findNavController(this@HomeFragment).navigate(action)
            }
        }

        binding?.rvChat?.adapter = adapter
        binding?.rvChat?.layoutManager = layoutManager

        layoutManager.scrollToPosition(adapter.itemCount - 1)
    }
}