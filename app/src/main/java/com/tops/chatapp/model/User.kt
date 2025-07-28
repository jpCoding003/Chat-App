package com.tops.chatapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
//    val uid: String = "",
//    val username: String = "",
//    val email: String = "",
//    val fcmToken: String = "",
//    val status: String = "Hey there! I’m using Chat App",
//    val imageUrl: String = "default" // Can update after uploading image

    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val status: String = "",
    val imageUrl: String = "",
    val fcmToken: String = "",  // ADD THIS LINE - FCM token for push notifications
    val isOnline: Boolean = false,
    val lastSeen: Long = 0L
): Parcelable {
    // No-argument constructor for Firebase
    constructor() : this("", "", "", "", "", "", false, 0L)
}
