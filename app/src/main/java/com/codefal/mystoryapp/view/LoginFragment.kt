package com.codefal.mystoryapp.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.codefal.mystoryapp.R
import com.codefal.mystoryapp.databinding.FragmentLoginBinding
import com.codefal.mystoryapp.viewmodel.AuthViewModel
import com.codefal.mystoryapp.viewmodel.PrefViewModel
import com.google.android.gms.maps.model.Marker
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

        binding.btnSignUp.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.edLoginEmail.doAfterTextChanged { validateForm() }
        binding.edLoginPassword.doAfterTextChanged { validateForm() }

        binding.buttonLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()
            login(email, password)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

    }

    private fun validateForm() {
        binding.apply {
            val email = edLoginEmail.text.toString()
            val password = edLoginPassword.text.toString()
            buttonLogin.isEnabled = password.length >= 8 && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
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
        binding.loadingBar.isVisible = status
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}