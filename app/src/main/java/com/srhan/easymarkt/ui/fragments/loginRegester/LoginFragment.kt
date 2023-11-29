package com.srhan.easymarkt.ui.fragments.loginRegester

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.srhan.easymarkt.R
import com.srhan.easymarkt.databinding.FragmentLoginBinding
import com.srhan.easymarkt.ui.activities.ShoppingActivity
import com.srhan.easymarkt.ui.fragments.dialog.setupBottomSheetDialog
import com.srhan.easymarkt.util.Resource
import com.srhan.easymarkt.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnLoginLogin.setOnClickListener {
                val email = etEmailLogin.text.toString().trim()
                val password = etPasswordLogin.text.toString()
                viewModel.login(email, password)
            }
        }

        binding.tvForgetPasswordLogin.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.resetPassword(email)

            }
        }
        lifecycleScope.launch {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {
                        Log.e("LoadingResetPassword", it.data.toString())

                    }

                    is Resource.Success -> {
                        Snackbar.make(
                            requireView(),
                            "Reset link was sent to your email",
                            Snackbar.LENGTH_LONG
                        ).show()
                        Log.e("SuccessResetPassword", "onViewCreated: ${it.data}")
                    }

                    is Resource.Error -> {
                        Log.e("ErrorResetPassword", it.message.toString())
                        Snackbar.make(
                            requireView(),
                            "Error : ${it.message.toString()}",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }

            }
        }
        binding.tvDontHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        lifecycleScope.launch {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnLoginLogin.startAnimation()
                        Log.e("TestLoginFragmentLoading", "onViewCreated: ${it.data}")

                    }

                    is Resource.Success -> {
                        Log.e("TestLoginFragmentSuccess", it.data.toString())
                        binding.btnLoginLogin.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                            startActivity(intent)

                        }

                    }

                    is Resource.Error -> {
                        Log.e("TestLoginFragmentError", it.message.toString())

                        binding.btnLoginLogin.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }
        }
    }

}