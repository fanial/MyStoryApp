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
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.codefal.mystoryapp.R
import com.codefal.mystoryapp.databinding.FragmentRegisterBinding
import com.codefal.mystoryapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
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

        binding.buttonSignUp.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.buttonRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        binding.apply {
            val username = edRegisterName.text.toString().trim()
            val email = edRegisterEmail.text.toString().trim()
            val password = edRegisterPassword.text.toString().trim()
            when {
                username.isEmpty() -> {
                    layoutNameReg.error = getString(R.string.username_error_message)
                }
                email.isEmpty() ->{
                    layoutEmailReg.error = getString(R.string.email_error_message)
                }
                password.isEmpty() ->{
                    layoutPasswordReg.error = getString(R.string.password_error_message)
                }
                else ->{
                    layoutNameReg.error = null
                    layoutEmailReg.error = null
                    layoutPasswordReg.error = null
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
        onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}