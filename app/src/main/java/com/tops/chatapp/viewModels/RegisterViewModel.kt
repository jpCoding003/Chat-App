package com.tops.chatapp.viewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.tops.chatapp.model.User

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun registerUser(username: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                    // Get FCM token for this user
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener { tokenTask ->
                            val fcmToken = if (tokenTask.isSuccessful) {
                                tokenTask.result ?: ""
                            } else {
                                ""
                            }

                            val user = User(
                                uid = uid,
                                username = username,
                                email = email,
                                status = "Hey there! I'm using Chat App",
                                imageUrl = "default",
                                fcmToken = fcmToken,  // ADD THIS LINE - Save FCM token
                                isOnline = true,
                                lastSeen = System.currentTimeMillis()
                            )

                            // Store in Realtime DB under "Users/uid"
                            database.child("Users").child(uid).setValue(user)
                                .addOnSuccessListener {
                                    onResult(true, "Registration Successful")
                                }
                                .addOnFailureListener {
                                    onResult(false, "Failed to save user data: ${it.message}")
                                }
                        }
                } else {
                    onResult(false, task.exception?.message ?: "Registration Failed")
                }
            }
    }
}