package com.mandalorian.chatapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mandalorian.chatapp.data.repository.AuthRepositoryImpl
import com.mandalorian.chatapp.data.repository.RealTimeRepositoryImpl
import com.mandalorian.chatapp.data.repository.UserRepositoryImpl
import com.mandalorian.chatapp.data.repository.AuthRepository
import com.mandalorian.chatapp.data.repository.RealTimeRepository
import com.mandalorian.chatapp.domain.repository.UserRepository
import com.mandalorian.chatapp.domain.useCase.LoginUseCase
import com.mandalorian.chatapp.domain.useCase.GetMessagesUseCase
import com.mandalorian.chatapp.domain.useCase.GetUsersUseCase
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
    fun getAuthRepository(auth: FirebaseAuth, db: FirebaseFirestore): AuthRepository {
        return AuthRepositoryImpl(auth, db.collection("users"))
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

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepo: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepo)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository): GetUsersUseCase {
        return GetUsersUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetMessagesUseCase(realTimeRepo: RealTimeRepository): GetMessagesUseCase {
        return GetMessagesUseCase(realTimeRepo)
    }
}