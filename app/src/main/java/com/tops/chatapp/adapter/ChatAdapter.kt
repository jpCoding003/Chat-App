package com.tops.chatapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tops.chatapp.databinding.ItemReceiveBinding
import com.tops.chatapp.databinding.ItemSentBinding
import com.tops.chatapp.model.Message

class ChatAdapter(
    private val context: Context,
    private val messageList: ArrayList<Message>,
    private val senderId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_SENT = 1
    private val ITEM_RECEIVE = 2

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        Log.d("ChatAdapter", "Message at position $position - SenderId: ${message.senderId}, CurrentUser: $senderId")
        return if (message.senderId == senderId) ITEM_SENT else ITEM_RECEIVE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT) {
            val binding = ItemSentBinding.inflate(LayoutInflater.from(context), parent, false)
            SentViewHolder(binding)
        } else {
            val binding = ItemReceiveBinding.inflate(LayoutInflater.from(context), parent, false)
            ReceiveViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        Log.d("ChatAdapter", "Total messages: ${messageList.size}")
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        Log.d("ChatAdapter", "Binding message at position $position: ${message.message}")

        if (holder is SentViewHolder) {
            holder.binding.tvSentMessage.text = message.message
        } else if (holder is ReceiveViewHolder) {
            holder.binding.tvReceiveMessage.text = message.message
        }
    }

    // Method to update the entire list
    fun updateMessages(newMessages: List<Message>) {
        messageList.clear()
        messageList.addAll(newMessages)
        Log.d("ChatAdapter", "Updated messages, new count: ${messageList.size}")
        notifyDataSetChanged()
    }

    // Method to add a single message
    fun addMessage(message: Message) {
        messageList.add(message)
        Log.d("ChatAdapter", "Added message: ${message.message}, total count: ${messageList.size}")
        notifyItemInserted(messageList.size - 1)
    }

    class SentViewHolder(val binding: ItemSentBinding) : RecyclerView.ViewHolder(binding.root)

    class ReceiveViewHolder(val binding: ItemReceiveBinding) : RecyclerView.ViewHolder(binding.root)
}