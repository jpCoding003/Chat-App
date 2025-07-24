package com.tops.chatapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.tops.chatapp.R
import com.tops.chatapp.adapter.UserAdapter
import com.tops.chatapp.databinding.FragmentHomeBinding
import com.tops.chatapp.viewModels.UserViewModel


class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private val userviewmodel : UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        auth = Firebase.auth
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser

        user?.let{ user->

            Toast.makeText(context, "WellCome ${user.email}", Toast.LENGTH_LONG).show()
        }
        userviewmodel.userList.observe(viewLifecycleOwner, Observer{
            list-> UserAdapter(list.toMutableList())
        })

        setupMenu()
    }

    private fun setupMenu(){
        val menuHost : MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                Log.d("HomeFragment", "Menu is being created")
              menuInflater.inflate(R.menu.app_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.logout_menu -> {
                        Firebase.auth.signOut()
                        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                        true
                    }
                    else -> false
                }
            }

        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}