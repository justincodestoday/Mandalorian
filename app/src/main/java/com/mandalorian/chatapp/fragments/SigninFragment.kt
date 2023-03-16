package com.mandalorian.chatapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mandalorian.chatapp.R

class SigninFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val group = view.findViewById<TextView>(R.id.emailEt)

        Log.d("debugging", "testing")
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
    }
}