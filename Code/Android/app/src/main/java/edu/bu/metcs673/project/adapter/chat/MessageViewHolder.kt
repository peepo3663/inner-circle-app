package edu.bu.metcs673.project.adapter.chat

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bu.metcs673.project.R

/**
 * Message View Holder - binding the element for chat bubbles.
 *
 * @constructor View from recyclerView, extends from ViewHolder class.
 *
 *
 * @param itemView View from recyclerView.
 */
class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * textView to show the message.
     */
    val mTextView: TextView = itemView.findViewById(R.id.text_message_body)

    /**
     * show the text time that be sent.
     */
    val mTextTime: TextView = itemView.findViewById(R.id.text_message_time)

    /**
     * Picture view for displaying friend's profile image.
     */
    val mPictureView: ImageView? = itemView.findViewById(R.id.image_message_profile)
}
