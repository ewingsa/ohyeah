package com.ewingsa.ohyeah.info

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ewingsa.ohyeah.resources.R as MainR

class InfoAdapter : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    class InfoViewHolder(val tutorialImageView: View) : RecyclerView.ViewHolder(tutorialImageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val tutorialImageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.tutorial_image, parent, false) as View
        return InfoViewHolder(tutorialImageView)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.tutorialImageView.context?.let {
            val description = it.getString(TUTORIAL_DESCRIPTIONS[position])
            holder.tutorialImageView.findViewById<TextView>(R.id.info_tutorial_description).text = if (position != 0) {
                description
            } else {
                titleText(description, it)
            }
        }
        holder.tutorialImageView.findViewById<ImageView>(R.id.info_tutorial_image).apply {
            setImageResource(TUTORIAL_IMAGES[position] ?: 0)
            (drawable as? AnimationDrawable)?.start()
        }
    }

    private fun titleText(title: String, context: Context): CharSequence {
        return SpannableString(title).apply {
            setSpan(ForegroundColorSpan(ContextCompat.getColor(context, MainR.color.red)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(context, MainR.color.yellow)), 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(context, MainR.color.red)), 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun onViewRecycled(holder: InfoViewHolder) {
        (holder.tutorialImageView.findViewById<ImageView>(R.id.info_tutorial_image).drawable as? AnimationDrawable)?.stop()
        super.onViewRecycled(holder)
    }

    override fun getItemCount() = TUTORIAL_IMAGES.size

    private companion object {
        val TUTORIAL_DESCRIPTIONS = listOf(
            R.string.info_app_description,
            R.string.info_tutorial_topics,
            R.string.info_tutorial_messages,
            R.string.info_tutorial_notification,
            R.string.info_tutorial_set_reminder,
            R.string.info_tutorial_edit_reminder,
            R.string.info_tutorial_call_to_action
        )
        val TUTORIAL_IMAGES = listOf(
            null,
            R.drawable.tutorial_topics,
            R.drawable.tutorial_messages,
            R.drawable.tutorial_push,
            R.drawable.tutorial_new,
            R.drawable.tutorial_edit_animation,
            null
        )
    }
}
