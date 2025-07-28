package com.tops.chatapp.model


data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: String = "text"
){
    // No-argument constructor for Firebase
    constructor() : this("", "", "", "", 0L, "text")
}
