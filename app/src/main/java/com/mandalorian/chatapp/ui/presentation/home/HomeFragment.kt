package com.mandalorian.chatapp.ui.presentation.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.data.receiver.MyBroadcastReceiver
import com.mandalorian.chatapp.databinding.FragmentHomeBinding
import com.mandalorian.chatapp.service.MyWork
import com.mandalorian.chatapp.ui.MainActivity
import com.mandalorian.chatapp.ui.presentation.base.BaseFragment
import com.mandalorian.chatapp.ui.presentation.adapters.ChatAdapter
import com.mandalorian.chatapp.ui.presentation.home.viewModel.HomeViewModelImpl
import com.mandalorian.chatapp.utils.Constants
import com.mandalorian.chatapp.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import java.util.concurrent.TimeUnit

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
            btnAdd.setOnClickListener {
                Log.d(Constants.DEBUG, "Service started")
                (requireActivity() as MainActivity).startService()
            }
            btnStop.setOnClickListener {
                Log.d(Constants.DEBUG, "Service stopped")
                (requireActivity() as MainActivity).stopService()
            }
            btnAlarm.setOnClickListener {
                setupAlarm()
            }
            btnCreateNotification.setOnClickListener {
                NotificationUtils.createNotification(
                    requireContext(),
                    "1st Notification",
                    "We are done"
                )
            }
            btnCreateRemoteInput.setOnClickListener {
                NotificationUtils.createNotificationWithPendingIntent(
                    requireContext(),
                    "Notification with pending intent",
                    "We are learning notification with pending intent"
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

    fun testCoroutine(): String {
        // coroutine -> asynchronous, multiple operations run at the same time
        // runBlocking -> synchronous, in linear sequence

        val job = lifecycleScope.launch(Dispatchers.Default) {
            for (i in 1..10) {
                Log.d(Constants.DEBUG, "Test is running...")
                delay(1000)
            }
        }
        runBlocking {
            job.join()
        }
        return "I'm done with test coroutine"
    }

    fun setupAlarm() {
//        val msg = testCoroutine()
//        Log.d(Constants.DEBUG, msg)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val data = Data.Builder()
            .putString("TEST", "We are testing work data")
            .putInt("EXTRA_INT", 101)
            .build()

        val data2 = Data.Builder()
            .putString("TEST", "We are testing work data")
            .putInt("EXTRA_INT", 102)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<MyWork>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val workRequest2 = PeriodicWorkRequestBuilder<MyWork>(1, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data2)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(workRequest)
        WorkManager.getInstance(requireContext()).enqueue(workRequest2)

//        val alarmManager = requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(requireContext(), MyBroadcastReceiver::class.java)
//
//        intent.action = "CHAT_APP_ALARM"
//        intent.putExtra("messages", "We are implementing alarm")
//
//        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_MUTABLE)
//        val alarmDelay = 10
//        val alarmTime = System.currentTimeMillis() + alarmDelay * 1_000L
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
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