package edu.bu.metcs673.project.adapter.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.bu.metcs673.project.R
import kotlinx.android.synthetic.main.message_item_left.view.*

class ChatsAdapter(mContext: Context, mChatList: List<IndividualChat>) : RecyclerView.Adapter<ViewHolder?>()
{
    private val mContext: Context
    private val mChatList: List<IndividualChat>
    var firebaseUser: FirebaseUser= FirebaseAuth.getInstance().currentUser!!

    init
    {
        this.mChatList=mChatList
        this.mContext=mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder
    {
        return if (position ==1)
        {
            val view: View = LayoutInflater.from(mContext).inflate(R.layout.message_item_left, parent, false)
            ViewHolder(view)
        }
        else
        {
            val view: View = LayoutInflater.from(mContext).inflate(R.layout.message_item_right, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int
    {
        return mChatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val chat: IndividualChat = mChatList[position]
        holder.showtextmessage!!.text=chat.getMessage()

    }

    override fun getItemViewType(position: Int): Int {

        return if (mChatList[position].getSender().equals(firebaseUser!!.uid))
        {
            1
        }
        else
        {
            0
        }
    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val showtextmessage :TextView= itemView.findViewById(R.id.show_text_message)
    var textmessagename :TextView= itemView.findViewById(R.id.text_message_name)
    var profileimage:ImageView= itemView.findViewById(R.id.image_message_profile)

}