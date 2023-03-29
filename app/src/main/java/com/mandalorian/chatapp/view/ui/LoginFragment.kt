package com.mandalorian.chatapp.view.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mandalorian.chatapp.MyApplication
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.databinding.FragmentLoginBinding
import com.mandalorian.chatapp.fragments.BaseFragment
import com.mandalorian.chatapp.viewModel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val viewModel: SignInViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_login

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        binding?.viewModel = viewModel
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.signInComplete.collect {
                navigateToHome()
            }
        }

        lifecycleScope.launch {
            viewModel.signUpComplete.collect {
                navigateToSignUp()
            }
        }
    }

    private fun navigateToSignUp() {
        val action = LoginFragmentDirections.toRegister()
        navController.navigate(action)
    }

    private fun navigateToHome() {
        (requireContext().applicationContext as MyApplication).fetchUsername()
        val action = LoginFragmentDirections.toHome()
        navController.navigate(action)
    }
}