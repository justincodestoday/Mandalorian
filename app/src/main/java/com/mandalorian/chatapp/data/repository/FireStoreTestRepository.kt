package com.mandalorian.chatapp.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FireStoreTestRepository {
    fun fetchData() {
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
    }
}