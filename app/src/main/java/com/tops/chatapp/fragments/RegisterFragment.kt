package com.tops.chatapp.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.tops.chatapp.R
import com.tops.chatapp.databinding.FragmentRegisterBinding
import com.tops.chatapp.viewModels.RegisterViewModel


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.btnRegister.setOnClickListener {
            if (signup()==true){
                val username = binding.etUsername.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etConfirmPassword.text.toString()

                viewModel.registerUser(username, email, password) { success, message ->
                    if (success) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(context,"Please Fill Details Properly",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnlogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun signup(): Boolean {

        var isvalid = true

        if (binding.etUsername.text.toString().isEmpty()){
            binding.etUsername.setError("Should not Empty")
            isvalid = false
        }else{
            binding.etUsername.error = null
        }

        if (binding.etEmail.text.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()){
            binding.etEmail.setError("Enter Valid Email Address")
            isvalid = false
        }else{
            binding.etEmail.error = null

        }
        if (binding.etpassword.text.toString().isEmpty() || binding.etpassword.length()< 8 ){
            binding.etpassword.setError("Improper password, must contain 8 Character Long")
            isvalid = false
        }else{
            binding.etpassword.error = null

        }
        if (binding.etConfirmPassword.text.toString().isEmpty() || binding.etConfirmPassword.text.toString() != binding.etpassword.text.toString()){
            binding.etConfirmPassword.setError("Password & Confirm Password must be same")
            isvalid = false
        }else{
            binding.etConfirmPassword.error = null
        }

        return isvalid
    }
}


