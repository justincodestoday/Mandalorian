package com.mandalorian.chatapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.utils.UserDiffUtil.update
import com.mandalorian.chatapp.data.model.Message
import com.mandalorian.chatapp.databinding.ItemLayoutMessageBinding

class MessageAdapter(private var items: MutableList<Message>) :
    RecyclerView.Adapter<MessageAdapter.ItemMessageHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMessageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutMessageBinding.inflate(layoutInflater, parent, false)
        return ItemMessageHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemMessageHolder, position: Int) {
        val item = items[position]
        holder.binding.run {
            tvUserName.text = item.name
            tvMessage.text = item.message

            if(position % 2 != 0) {
                cvMessage.setBackgroundResource(R.drawable.incoming_bubble)
            }
        }
    }

    fun setMessages(items: MutableList<Message>) {
        val oldItems = this.items
        this.items = items.toMutableList()
        update(oldItems, items) { message1, message2 ->
            message1.id == message2.id
        }
    }

    class ItemMessageHolder(val binding: ItemLayoutMessageBinding) :
        RecyclerView.ViewHolder(binding.root)
}