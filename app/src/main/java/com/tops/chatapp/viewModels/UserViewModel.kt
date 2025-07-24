package com.tops.chatapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tops.chatapp.model.User

class UserViewModel : ViewModel() {

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList

    private val dbRef = FirebaseDatabase.getInstance().getReference("Users")

    fun fetchAllUsers(currentUid: String) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (userSnap in snapshot.children) {
                    val user = userSnap.getValue(User::class.java)
                    if (user != null && user.uid != currentUid) {
                        users.add(user)
                    }
                }
                _userList.value = users
            }

            override fun onCancelled(error: DatabaseError) {
                // handle error
            }
        })
    }

}