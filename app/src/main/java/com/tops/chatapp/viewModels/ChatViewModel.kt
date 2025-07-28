package com.tops.chatapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tops.chatapp.model.Message

class ChatViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private var messagesListener: ValueEventListener? = null
    private var chatRef: DatabaseReference? = null

    fun sendMessage(chatId: String, senderId: String, receiverId: String, messageText: String) {
        Log.d("ChatViewModel", "Sending message - ChatId: $chatId, SenderId: $senderId, Message: $messageText")

        val messageId = database.child("Messages").child(chatId).push().key
        if (messageId == null) {
            Log.e("ChatViewModel", "Failed to generate message ID")
            return
        }

        val message = Message(
            messageId = messageId,
            senderId = senderId,
            receiverId = receiverId,
            message = messageText,
            timestamp = System.currentTimeMillis()
        )

        Log.d("ChatViewModel", "Message object created: $message")

        // Save message to Firebase
        database.child("Messages").child(chatId).child(messageId).setValue(message)
            .addOnSuccessListener {
                Log.d("ChatViewModel", "Message saved successfully to Firebase")
                // Update chat metadata
                updateChatMetadata(chatId, senderId, receiverId, messageText)
            }
            .addOnFailureListener { error ->
                Log.e("ChatViewModel", "Failed to save message: ${error.message}")
            }
    }

    fun loadMessages(chatId: String) {
        Log.d("ChatViewModel", "Loading messages for chatId: $chatId")

        // Remove previous listener if exists
        messagesListener?.let { listener ->
            chatRef?.removeEventListener(listener)
        }

        chatRef = database.child("Messages").child(chatId)
        messagesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ChatViewModel", "onDataChange called - snapshot exists: ${snapshot.exists()}")
                Log.d("ChatViewModel", "Number of children in snapshot: ${snapshot.childrenCount}")

                val messagesList = mutableListOf<Message>()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    Log.d("ChatViewModel", "Retrieved message: $message")
                    message?.let {
                        messagesList.add(it)
                        Log.d("ChatViewModel", "Added message to list: ${it.message}")
                    }
                }

                val sortedMessages = messagesList.sortedBy { it.timestamp }
                Log.d("ChatViewModel", "Total messages loaded: ${sortedMessages.size}")
                _messages.value = sortedMessages
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatViewModel", "Database error: ${error.message}")
            }
        }

        chatRef?.addValueEventListener(messagesListener!!)
    }

    private fun updateChatMetadata(chatId: String, senderId: String, receiverId: String, lastMessage: String) {
        Log.d("ChatViewModel", "Updating chat metadata for chatId: $chatId")

        val chatData = mapOf(
            "participants" to listOf(senderId, receiverId),
            "lastMessage" to lastMessage,
            "lastMessageTime" to System.currentTimeMillis()
        )

        database.child("Chats").child(chatId).setValue(chatData)
            .addOnSuccessListener {
                Log.d("ChatViewModel", "Chat metadata updated successfully")
            }
            .addOnFailureListener { error ->
                Log.e("ChatViewModel", "Failed to update chat metadata: ${error.message}")
            }
    }

    override fun onCleared() {
        super.onCleared()
        // Remove listener when ViewModel is cleared
        messagesListener?.let { listener ->
            chatRef?.removeEventListener(listener)
        }
        Log.d("ChatViewModel", "ViewModel cleared, listeners removed")
    }
}