package com.mandalorian.chatapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
        binding?.run {
            button.setOnClickListener {
                val email = emailEt.text.toString()
                val pass = passET.text.toString()
                viewModel.login(email, pass)
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.loginFinish.collect {
                val action = LoginFragmentDirections.toHome()
                navController.navigate(action)
            }
        }
        binding?.textView2?.setOnClickListener {
            val action = LoginFragmentDirections.toRegister()
            navController.navigate(action)
        }
    }
}