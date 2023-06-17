package com.codefal.mystoryapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.codefal.mystoryapp.R
import com.codefal.mystoryapp.databinding.FragmentLoginBinding
import com.codefal.mystoryapp.viewmodel.AuthViewModel
import com.codefal.mystoryapp.viewmodel.PrefViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authModel : AuthViewModel by viewModels()
    private val prefModel : PrefViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        authModel.loadingObserver().observe(viewLifecycleOwner){ loading(it) }
        authModel.messageObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignUp.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.buttonLogin.setOnClickListener {
            binding.apply {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString().trim()
                if (email.isEmpty() && password.isEmpty()) {
                    layoutEmail.error = getString(R.string.email_error_message)
                    layoutPassword.error = getString(R.string.password_error_message)
                } else {
                    layoutEmail.error = null
                    layoutPassword.error = null
                    login(email, password)
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

    }

    private fun login(email: String, password: String) {
        authModel.doLogin(email, password)
        authModel.doLoginObserver().observe(viewLifecycleOwner){ auth ->
                if (auth != null){
                    val token = auth.loginResult?.token.toString()
                    val name = auth.loginResult?.name.toString()
                    prefModel.setToken("Bearer $token", name, true)
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_storyFragment)
                    Log.i("Success", "login: $name, $token")
                    val fragmentManager = requireActivity().supportFragmentManager
                    fragmentManager.popBackStack("loginFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
        }

    }

    private fun loading(status: Boolean) {
        when(status){
            true -> {
                binding.loadingBar.visibility = View.VISIBLE
            }
            false -> {
                binding.loadingBar.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}