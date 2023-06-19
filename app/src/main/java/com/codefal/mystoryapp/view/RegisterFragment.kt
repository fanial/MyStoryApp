package com.codefal.mystoryapp.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.codefal.mystoryapp.R
import com.codefal.mystoryapp.databinding.FragmentRegisterBinding
import com.codefal.mystoryapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authModel : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        authModel.loadingObserver().observe(viewLifecycleOwner){ loading(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.edRegisterName.doAfterTextChanged { validateForm() }
        binding.edRegisterEmail.doAfterTextChanged { validateForm() }
        binding.edRegisterPassword.doAfterTextChanged { validateForm() }

        binding.buttonRegister.setOnClickListener {
            register()
        }
    }

    private fun validateForm() {
        binding.apply {
            val username = edRegisterName.text.toString().trim()
            val email = edRegisterEmail.text.toString().trim()
            val password = edRegisterPassword.text.toString().trim()
            buttonRegister.isEnabled = password.length >= 8 && Patterns.EMAIL_ADDRESS.matcher(email).matches() && username.isNotEmpty()
            when {
                username.isEmpty() -> {
                    layoutNameReg.error = getString(R.string.username_error_message)
                }
                else ->{
                    layoutNameReg.error = null
                    layoutEmailReg.error = null
                    layoutPasswordReg.error = null
                }
            }
        }
    }

    private fun register() {
        val username = binding.edRegisterName.text.toString().trim()
        val email = binding.edRegisterEmail.text.toString().trim()
        val password = binding.edRegisterPassword.text.toString().trim()
        authModel.doRegister(username, email, password)
        authModel.doRegisterObserver().observe(viewLifecycleOwner){ auth ->
            if (auth != null){
                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
                Log.i("Success", "register: $username, $email")
            }else{
                authModel.messageObserver().observe(viewLifecycleOwner){ msg ->
                    if (msg !=null){
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
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
        onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}