package com.tops.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.tops.chatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FCM
        initializeFCM()

        // Handle notification click
        handleNotificationIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    /**
     * Initialize Firebase Cloud Messaging
     */
    private fun initializeFCM() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Log.d("MainActivity", "FCM Token: $token")

                // TODO: Send token to your app server if needed
            }
    }

    /**
     * Handle when user clicks on notification
     * This will navigate to the specific chat
     */
    private fun handleNotificationIntent(intent: Intent?) {
        intent?.let {
            val openChat = it.getBooleanExtra("open_chat", false)
            if (openChat) {
                val senderId = it.getStringExtra("sender_id")
                val chatId = it.getStringExtra("chat_id")

                Log.d("MainActivity", "Opening chat from notification - SenderId: $senderId, ChatId: $chatId")

                // TODO: Navigate to specific chat fragment
                // You can use NavController or start specific activity/fragment here
                // Example:
                // val navController = findNavController(R.id.nav_host_fragment)
                // val bundle = Bundle().apply {
                //     putString("sender_id", senderId)
                //     putString("chat_id", chatId)
                // }
                // navController.navigate(R.id.chatFragment, bundle)
            }
        }
    }
}