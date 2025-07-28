package com.tops.chatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.tops.chatapp.R
import com.tops.chatapp.adapter.UserAdapter
import com.tops.chatapp.databinding.FragmentUserListBinding
import com.tops.chatapp.model.User
import com.tops.chatapp.viewModels.UserViewModel


class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private val userviewModel: UserViewModel by viewModels()
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<User>

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserAdapter(mutableListOf()) { selectedUser ->
            val bundle = Bundle().apply {
                putParcelable("receiver_user", selectedUser)
            }
            findNavController().navigate(R.id.action_userListFragment_to_chatFragment, bundle)
        }
        binding.recyclerview.adapter = adapter

        userviewModel.userList.observe(viewLifecycleOwner) { users ->
            Log.d("UserListFragment", "Users received: ${users.size}")
           adapter.updateList(users)
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_userListFragment_to_splashScreenFragment)
        }
        userviewModel.fetchAllUsers(currentUid)
    }
}