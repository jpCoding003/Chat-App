package com.tops.chatapp.utils

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class NotificationHelper {

    companion object {
        private const val TAG = "NotificationHelper"
        private const val FCM_URL = "https://fcm.googleapis.com/fcm/send"

        // TODO: Replace with your Firebase Server Key from Firebase Console > Project Settings > Cloud Messaging
        private const val SERVER_KEY = "AIzaSyBWNIOs8hH1WURsj-_xzhDOw2ymHwbJA9Y"

        /**
         * Send push notification to a specific user
         * Call this method when sending a message
         */
        fun sendNotificationToUser(
            receiverId: String,
            senderName: String,
            message: String,
            senderId: String,
            chatId: String
        ) {
            Log.d(TAG, "Preparing to send notification to user: $receiverId")

            // Get receiver's FCM token from database
            val database = FirebaseDatabase.getInstance().reference
            database.child("Users").child(receiverId).child("fcmToken")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val fcmToken = snapshot.getValue(String::class.java)
                        if (!fcmToken.isNullOrEmpty()) {
                            Log.d(TAG, "Found FCM token for user: $receiverId")
                            sendFCMNotification(fcmToken, senderName, message, senderId, chatId)
                        } else {
                            Log.w(TAG, "No FCM token found for user: $receiverId")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Failed to get FCM token: ${error.message}")
                    }
                })
        }

        /**
         * Send FCM notification using HTTP request
         */
        private fun sendFCMNotification(
            fcmToken: String,
            senderName: String,
            message: String,
            senderId: String,
            chatId: String
        ) {
            try {
                Log.d(TAG, "Sending FCM notification...")

                val client = OkHttpClient()

                // Create notification payload
                val notification = JSONObject().apply {
                    put("title", senderName)
                    put("body", message)
                    put("sound", "default")
                }

                // Create data payload (custom data)
                val data = JSONObject().apply {
                    put("senderName", senderName)
                    put("message", message)
                    put("senderId", senderId)
                    put("chatId", chatId)
                    put("click_action", "FLUTTER_NOTIFICATION_CLICK")
                }

                // Create main payload
                val payload = JSONObject().apply {
                    put("to", fcmToken)
                    put("notification", notification)
                    put("data", data)
                    put("priority", "high")
                }

                val requestBody = payload.toString().toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url(FCM_URL)
                    .post(requestBody)
                    .addHeader("Authorization", "key=$SERVER_KEY")
                    .addHeader("Content-Type", "application/json")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "Failed to send notification: ${e.message}")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "Notification sent successfully")
                        } else {
                            Log.e(TAG, "Failed to send notification: ${response.code}")
                        }
                    }
                })

            } catch (e: Exception) {
                Log.e(TAG, "Error sending notification: ${e.message}")
            }
        }
    }
}