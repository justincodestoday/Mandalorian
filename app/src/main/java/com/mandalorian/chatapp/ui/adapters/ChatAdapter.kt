package com.mandalorian.chatapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mandalorian.chatapp.data.model.Chat
import com.mandalorian.chatapp.databinding.ItemLayoutChatBinding
import com.mandalorian.chatapp.utils.Utils.update

class ChatAdapter(private var items: MutableList<Chat>) :
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
            tvUsername.text = item.user1
            tvMessage.text = item.messages[0].message
            cvChatItem.setOnClickListener { listener?.onClick(item) }
        }
    }

    fun setChats(items: MutableList<Chat>) {
        val oldItems = this.items
        this.items = items.toMutableList()
        update(oldItems, items) { chat1, chat2 ->
            chat1.id == chat2.id
        }
    }

    class ItemChatHolder(val binding: ItemLayoutChatBinding) :
        RecyclerView.ViewHolder(binding.root)


    interface Listener {
        fun onClick(item: Chat)
    }
}