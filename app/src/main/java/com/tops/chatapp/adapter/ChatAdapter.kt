package com.tops.chatapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tops.chatapp.R
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
        return if (messageList[position].senderId == senderId) ITEM_SENT else ITEM_RECEIVE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false)
            SentViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false)
            ReceiveViewHolder(view)
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is SentViewHolder) {
            holder.binding.tvSentMessage.text = message.message
        } else if (holder is ReceiveViewHolder) {
            holder.binding.tvReceiveMessage.text = message.message
        }
    }

    class SentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSentBinding.bind(view)
    }

    class ReceiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemReceiveBinding.bind(view)
    }
}
