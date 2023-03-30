package com.mandalorian.chatapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mandalorian.chatapp.service.AuthService
import com.mandalorian.chatapp.data.repository.RealTimeRepositoryImpl
import com.mandalorian.chatapp.data.repository.UserRepositoryImpl
import com.mandalorian.chatapp.domain.repository.RealTimeRepository
import com.mandalorian.chatapp.domain.repository.UserRepository
import com.mandalorian.chatapp.domain.usecase.GetUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyAppDependency {

    @Provides
    @Singleton
    fun provideRealtimeDatabase(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("chat-app")
    }

    @Provides
    @Singleton
    fun getFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun getFireAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun getAuthService(auth: FirebaseAuth, db: FirebaseFirestore): AuthService {
        return AuthService(auth, db.collection("users"))
    }

    @Provides
    @Singleton
    fun getRealTimeRepository(): RealTimeRepository {
        return RealTimeRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(db.collection("users"))
    }

//    @Provides
//    @Singleton
//    fun provideGetUserUseCase(userRepository: UserRepository): GetUsersUseCase {
//        return GetUsersUseCase(userRepository)
//    }

}