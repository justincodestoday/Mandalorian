package com.mandalorian.chatapp.service

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mandalorian.chatapp.utils.Constants
import kotlinx.coroutines.*

class MyWork(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        val testMsg = inputData.getString("TEST")
        val testNum = inputData.getInt("EXTRA_INT", 0)
        return try {
            val job = CoroutineScope(Dispatchers.Default).launch {
                for (i in 1..10) {
                    Log.d(Constants.DEBUG, "$testMsg $testNum Working....")
                    delay(1000)
                }
            }
            runBlocking { job.join() }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}