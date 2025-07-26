package com.tops.chatapp.fragments


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.tops.chatapp.R
import com.tops.chatapp.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private
    lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()

        val currentuser = auth.currentUser
        Handler(Looper.getMainLooper()).postDelayed({
            if (currentuser != null){
                findNavController().navigate(R.id.action_splashScreenFragment_to_userListFragment)
            }else{
                findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
            } }, 3000)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        auth = Firebase.auth
        return binding.root
    }

}