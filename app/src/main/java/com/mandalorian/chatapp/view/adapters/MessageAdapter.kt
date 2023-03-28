package com.mandalorian.chatapp.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mandalorian.chatapp.MyApplication
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.model.model.Message
import com.mandalorian.chatapp.databinding.ItemLayoutMessageBinding
import com.mandalorian.chatapp.utils.Utils.update
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(var items: MutableList<Message>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutMessageBinding.inflate(layoutInflater, parent, false)
        return if (viewType == INCOMING) {
            ItemIncomingMessageHolder(binding)
        } else {
            ItemOutgoingMessageHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when(holder) {
            is ItemIncomingMessageHolder -> {
                holder.bind(item)
            }
            is ItemOutgoingMessageHolder -> {
                holder.bind(item)
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

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        val currentUsername = (context.applicationContext as MyApplication).username ?: ""
        if (currentUsername == item.name) {
            return INCOMING
        }
        return OUTGOING
    }

    class ItemIncomingMessageHolder(val binding: ItemLayoutMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.run {
                cvMessage.setBackgroundResource(R.drawable.outgoing_bubble)
                tvUserName.text = message.name
                tvMessage.text = message.message.trim()

                // Convert timestamp to date and time
                val date = Date(message.timestamp as Long)
                val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                formatter.timeZone = TimeZone.getDefault() // Set time zone to local time zone
                val formattedDate = formatter.format(date)
                tvTime.text = formattedDate
            }
        }
    }

    class ItemOutgoingMessageHolder(val binding: ItemLayoutMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.run {
                cvMessage.setBackgroundResource(R.drawable.incoming_bubble)
                tvUserName.text = message.name
                tvMessage.text = message.message.trim()

                // Convert timestamp to date and time
                val date = Date(message.timestamp as Long)
                val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                formatter.timeZone = TimeZone.getDefault() // Set time zone to local time zone
                val formattedDate = formatter.format(date)
                tvTime.text = formattedDate
            }
        }
    }

    companion object {
        const val INCOMING = 100
        const val OUTGOING = 101
    }
}