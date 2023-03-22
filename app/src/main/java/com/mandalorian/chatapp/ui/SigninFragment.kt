package com.mandalorian.chatapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.databinding.FragmentSignInBinding
import com.mandalorian.chatapp.viewModel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SigninFragment : BaseFragment<FragmentSignInBinding>() {
    override val viewModel: SignInViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_sign_in

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        binding?.btnSignIn?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.signIn()
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.signIn.collect {
                binding?.run {
                    etEmail.text?.clear()
                    etPassword.text?.clear()
                }

//                (activity as MainActivity).identify()
//                val action =
//                    CredentialsFragmentDirections.actionCredentialsFragmentToHomeFragment()
//                navController.navigate(action)
            }
        }
    }
}