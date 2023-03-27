package com.mandalorian.chatapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.databinding.FragmentRegisterBinding
import com.mandalorian.chatapp.fragments.BaseFragment
import com.mandalorian.chatapp.viewModel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val viewModel: SignUpViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_register

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        binding?.run {
            button.setOnClickListener {
                val name = nameEt.text.toString()
                val email = emailEt.text.toString()
                val pass = passET.text.toString()

                viewModel.signUp(name, email, pass)
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.signupFinish.collect {
                val action = LoginFragmentDirections.toLogin()
                navController.navigate(action)
            }
        }
    }
}