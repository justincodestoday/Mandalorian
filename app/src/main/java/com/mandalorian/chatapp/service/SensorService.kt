package com.mandalorian.chatapp.service

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.IBinder
import android.util.Log
import com.mandalorian.chatapp.utils.Constants

class SensorService : Service(), SensorEventListener {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_MOTION_DETECT) {
            event.values.forEach {
                Log.d(Constants.DEBUG, it.toString())
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }
}