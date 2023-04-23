package com.mandalorian.chatapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mandalorian.chatapp.data.repository.AuthRepository
import com.mandalorian.chatapp.domain.useCase.LoginUseCase
import com.mandalorian.chatapp.ui.presentation.login.viewModel.SignInViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: SignInViewModel
    private lateinit var loginUseCase: LoginUseCase
    private val authRepo = Mockito.mock(AuthRepository::class.java)
//    private val loginUseCase = Mockito.mock(LoginUseCase::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        loginUseCase = LoginUseCase(authRepo)
        loginViewModel = SignInViewModel(loginUseCase)
    }

    @Test
    fun `user should be able to login with the correct credentials`() = runTest {
        Mockito.`when`(authRepo.login("abc@abc.com", "qwerqwer")).thenReturn(true)
        loginViewModel.email.value = "abc@abc.com"
        loginViewModel.password.value = "qwerqwer"
        loginViewModel.login()
        assertEquals(loginViewModel.loginFinish.first(), Unit)
    }

    @Test
    fun `user should not be able to login with the wrong credentials`() = runTest {
        Mockito.`when`(authRepo.login("abc@abc.com", "qwerqw")).thenReturn(false)
        loginViewModel.email.value = "abc@abc.com"
        loginViewModel.password.value = "qwerqw"
        loginViewModel.login()
        assertEquals(loginViewModel.newError.first(), "Failed")
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }
}