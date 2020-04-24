package com.ewingsa.ohyeah.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ewingsa.ohyeah.messages.datamodels.DateLabelDataModel
import com.ewingsa.ohyeah.messages.datamodels.MessageDataModel
import com.ewingsa.ohyeah.messages.viewmodels.MessageViewModel
import kotlinx.android.synthetic.main.message_view.view.message_view_date
import kotlinx.android.synthetic.main.message_view.view.message_view_image
import kotlinx.android.synthetic.main.message_view.view.message_view_layout
import kotlinx.android.synthetic.main.message_view.view.message_view_text
import kotlinx.android.synthetic.main.message_view.view.message_view_time

class MessagesAdapter(private val messages: List<MessageViewModel>) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    class MessageViewHolder(val messageView: View) : RecyclerView.ViewHolder(messageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val conversationView = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_view, parent, false) as View
        return MessageViewHolder(conversationView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        (messages[position].messageDataModel as? MessageDataModel)?.let {
            holder.messageView.message_view_date.visibility = View.GONE
            holder.messageView.message_view_text.text = it.message
            holder.messageView.message_view_time.text = it.displayTime
            it.displayImage?.let { image ->
                holder.messageView.message_view_image.setImageBitmap(image)
            }
            holder.messageView.message_view_text.setOnLongClickListener {
                messages[position].onLongPress()
                true
            }
            holder.messageView.message_view_layout.visibility = View.VISIBLE
        } ?: (messages[position].messageDataModel as? DateLabelDataModel)?.let {
            holder.messageView.message_view_layout.visibility = View.GONE
            holder.messageView.message_view_date.text = it.displayDate
            holder.messageView.message_view_date.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = messages.size
}
