package com.tops.chatapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val status: String = "Hey there! I’m using Chat App",
    val imageUrl: String = "default" // Can update after uploading image
): Parcelable
