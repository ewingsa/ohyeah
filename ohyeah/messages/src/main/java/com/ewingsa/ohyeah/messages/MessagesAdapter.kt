package com.ewingsa.ohyeah.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ewingsa.ohyeah.messages.datamodels.DateLabelDataModel
import com.ewingsa.ohyeah.messages.datamodels.MessageDataModel
import com.ewingsa.ohyeah.messages.viewmodels.MessageViewModel

class MessagesAdapter(private val messages: List<MessageViewModel>) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    class MessageViewHolder(val messageView: View) : RecyclerView.ViewHolder(messageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val conversationView = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_view, parent, false) as View
        return MessageViewHolder(conversationView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        (messages[position].messageDataModel as? MessageDataModel)?.let {
            holder.messageView.findViewById<TextView>(R.id.message_view_date).visibility = View.GONE
            holder.messageView.findViewById<TextView>(R.id.message_view_text).text = it.message
            holder.messageView.findViewById<TextView>(R.id.message_view_time).text = it.displayTime
            it.displayImage?.let { image ->
                holder.messageView.findViewById<ImageView>(R.id.message_view_image).setImageBitmap(image)
            }
            holder.messageView.findViewById<TextView>(R.id.message_view_text).setOnLongClickListener {
                messages[position].onLongPress()
                true
            }
            holder.messageView.findViewById<RelativeLayout>(R.id.message_view_layout).visibility = View.VISIBLE
        } ?: (messages[position].messageDataModel as? DateLabelDataModel)?.let {
            holder.messageView.findViewById<RelativeLayout>(R.id.message_view_layout).visibility = View.GONE
            holder.messageView.findViewById<TextView>(R.id.message_view_date).apply {
                text = it.displayDate
                visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount() = messages.size
}
