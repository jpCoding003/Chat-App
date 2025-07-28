package com.tops.chatapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.tops.chatapp.adapters.ChatAdapter
import com.tops.chatapp.databinding.FragmentChatBinding
import com.tops.chatapp.model.Message
import com.tops.chatapp.model.User
import com.tops.chatapp.viewModels.ChatViewModel

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messageList: ArrayList<Message>
    private val chatViewModel: ChatViewModel by viewModels()

    private var receiverUser: User? = null
    private var currentUserId: String = ""
    private var chatId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get receiver user from arguments
        receiverUser = arguments?.getParcelable("receiver_user")
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        Log.d("ChatFragment", "Current User ID: $currentUserId")
        Log.d("ChatFragment", "Receiver User: ${receiverUser?.username}")

        setupRecyclerView()
        setupClickListeners()
        observeMessages()

        // Generate chat ID
        chatId = generateChatId(currentUserId, receiverUser?.uid ?: "")
        Log.d("ChatFragment", "Chat ID: $chatId")

        // Load existing messages
        chatViewModel.loadMessages(chatId)
    }

    private fun setupRecyclerView() {
        messageList = ArrayList()
        chatAdapter = ChatAdapter(requireContext(), messageList, currentUserId)

        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        Log.d("ChatFragment", "RecyclerView setup complete")
    }

    private fun setupClickListeners() {
        binding.btnSend.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()
            Log.d("ChatFragment", "Send button clicked, message: $messageText")

            if (messageText.isNotEmpty()) {
                chatViewModel.sendMessage(
                    chatId = chatId,
                    senderId = currentUserId,
                    receiverId = receiverUser?.uid ?: "",
                    messageText = messageText
                )
                binding.etMessage.text?.clear()
            }
        }
    }

    private fun observeMessages() {
        Log.d("ChatFragment", "Setting up message observer")

        chatViewModel.messages.observe(viewLifecycleOwner) { messages ->
            Log.d("ChatFragment", "Messages received from ViewModel: ${messages.size}")

            messages.forEach { message ->
                Log.d("ChatFragment", "Message details - ID: ${message.messageId}, Sender: ${message.senderId}, Text: ${message.message}")
            }

            // Update adapter
            chatAdapter.updateMessages(messages)

            Log.d("ChatFragment", "Adapter updated, current messageList size: ${messageList.size}")

            // Scroll to bottom when new message arrives
            if (messages.isNotEmpty()) {
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
                Log.d("ChatFragment", "Scrolled to position: ${messages.size - 1}")
            }
        }
    }

    private fun generateChatId(uid1: String, uid2: String): String {
        return if (uid1 < uid2) "${uid1}_$uid2" else "${uid2}_$uid1"
    }
}