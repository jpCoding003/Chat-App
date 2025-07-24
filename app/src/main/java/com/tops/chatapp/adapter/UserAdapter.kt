package com.tops.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tops.chatapp.R
import com.tops.chatapp.databinding.UserRowItemBinding
import com.tops.chatapp.model.User

class UserAdapter(private var userlist: MutableList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val binding = UserRowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val users = userlist[position]
        holder.binding.tvUsername.text = users.username
        holder.binding.tvStatus.text = users.status

        Glide.with(holder.itemView.context)
            .load(if (users.imageUrl == "default") R.drawable.ic_launcher_foreground else users.imageUrl)
            .into(holder.binding.imgProfile)


    }

    override fun getItemCount(): Int = userlist.size

    class UserViewHolder(val binding: UserRowItemBinding) : RecyclerView.ViewHolder(binding.root)
}