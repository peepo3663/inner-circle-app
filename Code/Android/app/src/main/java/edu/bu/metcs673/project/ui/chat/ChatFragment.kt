package edu.bu.metcs673.project.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import edu.bu.metcs673.project.MainActivity
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.adapter.chat.MessageAdapter
import edu.bu.metcs673.project.model.chat.MessageModel
import java.net.URL

class ChatFragment : Fragment() {
    companion object {
        private const val TAG = "ChatFragment"
    }

    private lateinit var mMessageAdapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ChatViewModel

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewMessageList)

        mMessageAdapter = MessageAdapter()
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel.getAllMessages()?.observe(this, Observer {
            mMessageAdapter.setMessages(it)
            mMessageAdapter.notifyDataSetChanged()
        })
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = mMessageAdapter

        val sendButton = view.findViewById<Button>(R.id.button_chatbox_send)
        val userInput = view.findViewById<EditText>(R.id.user_text_chatbox)
        sendButton.setOnClickListener {
            sendMessage(userInput.text.toString(), viewModel, userId as String, MainActivity.currentUser.profilePicture, userInput)
        }

        return view
    }

    private fun sendMessage(text: String, viewModel: ChatViewModel, userId: String, profilePicture: URL?, userInput: EditText) {
        // generate the user message
        val userMessage = MessageModel(text, true, userId, profilePicture?.toString())
        viewModel.insert(userMessage)
        // notify observers data has changed
        mMessageAdapter.notifyDataSetChanged()
        // add scrollable effect
        if (!isTextVisible()) {
            recyclerView.smoothScrollToPosition(mMessageAdapter.itemCount - 1)
        }
        // clear the user input
        userInput.setText("")
    }

    /** Helper method to view the chat messages.
     * Scrolls to the bottom of the screen every time the user types  */
    private fun isTextVisible(): Boolean {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val positionOfLastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
        val itemCount = mMessageAdapter.itemCount
        return positionOfLastVisibleItem >= itemCount
    }
}