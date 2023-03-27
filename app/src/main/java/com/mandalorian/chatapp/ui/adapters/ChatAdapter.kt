package com.mandalorian.chatapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.data.service.AuthService
import com.mandalorian.chatapp.databinding.ItemLayoutChatBinding
import com.mandalorian.chatapp.utils.Utils.update

class ChatAdapter(private var items: MutableList<User>) :
RecyclerView.Adapter<ChatAdapter.ItemChatHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChatHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutChatBinding.inflate(layoutInflater, parent, false)
        return ItemChatHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemChatHolder, position: Int) {
        val item = items[position]
        holder.binding.run {
            tvUsername.text = item.username
            tvMessage.text = item.email
            cvChatItem.setOnClickListener { listener?.onClick(item) }

//            if(tvUsername.text == ) {
//                Log.d("test", "Hiding card for ${item.username}")
//                cvChatItem.visibility = View.GONE
//            } else {
//                cvChatItem.visibility = View.VISIBLE
//            }
        }
    }

    fun setChats(items: MutableList<User>) {
        val oldItems = this.items
        this.items = items.toMutableList()
        update(oldItems, items) { chat1, chat2 ->
            chat1.id == chat2.id
        }
    }

    class ItemChatHolder(val binding: ItemLayoutChatBinding) :
        RecyclerView.ViewHolder(binding.root)


    interface Listener {
        fun onClick(item: User)
    }
}