package com.ewingsa.ohyeah.conversations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ewingsa.ohyeah.conversations.datamodels.ConversationDataModel
import com.ewingsa.ohyeah.conversations.viewmodels.ConversationViewModel
import kotlinx.android.synthetic.main.conversation_view.view.conversation_view_image
import kotlinx.android.synthetic.main.conversation_view.view.conversation_view_layout
import kotlinx.android.synthetic.main.conversation_view.view.conversation_view_preview
import kotlinx.android.synthetic.main.conversation_view.view.conversation_view_time
import kotlinx.android.synthetic.main.conversation_view.view.conversation_view_title
import kotlinx.android.synthetic.main.conversation_view.view.conversation_view_unread
import kotlinx.android.synthetic.main.conversation_view.view.conversation_view_upcoming

class ConversationsAdapter(
    private val conversations: List<ConversationViewModel>
) : RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder>() {

    class ConversationViewHolder(val conversationView: View) : RecyclerView.ViewHolder(conversationView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val conversationView = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversation_view, parent, false) as View
        return ConversationViewHolder(conversationView)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        (conversations[position].conversationDataModel as? ConversationDataModel)?.let {
            holder.conversationView.conversation_view_upcoming.visibility = View.GONE

            val conversationDataModel = conversations[position].conversationDataModel as ConversationDataModel

            holder.conversationView.conversation_view_preview.text = conversationDataModel.previewText
            holder.conversationView.conversation_view_time.text = conversationDataModel.displayTime
            holder.conversationView.conversation_view_title.text = conversationDataModel.senderName
            conversationDataModel.numberUnread?.let {
                holder.conversationView.conversation_view_unread.text = it
                holder.conversationView.conversation_view_unread.visibility = View.VISIBLE
            } ?: run { holder.conversationView.conversation_view_unread.visibility = View.INVISIBLE }
            conversationDataModel.displayImage?.let {
                holder.conversationView.conversation_view_image.setImageBitmap(it)
            }
            holder.conversationView.setOnClickListener { conversations[position].onPress() }
            holder.conversationView.setOnLongClickListener {
                conversations[position].onLongPress()
                true
            }
            holder.conversationView.conversation_view_layout.visibility = View.VISIBLE
        } ?: run {
            holder.conversationView.conversation_view_layout.visibility = View.GONE
            holder.conversationView.conversation_view_upcoming.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = conversations.size
}
