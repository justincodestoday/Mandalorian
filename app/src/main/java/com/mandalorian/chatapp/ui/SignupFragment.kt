package com.mandalorian.chatapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.databinding.FragmentSignUpBinding
import com.mandalorian.chatapp.viewModel.BaseViewModel
import com.mandalorian.chatapp.viewModel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignUpBinding>() {
    override val viewModel: SignUpViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_sign_up

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        binding?.btnSignUp?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.signUp()
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.signUp.collect {
                binding?.run {
                    etName.text?.clear()
                    etEmail.text?.clear()
                    etPassword.text?.clear()
                    etConfirmPassword.text?.clear()
                }

//                (activity as MainActivity).identify()
//                val action =
//                    CredentialsFragmentDirections.actionCredentialsFragmentToHomeFragment()
//                navController.navigate(action)
            }
        }
    }
}