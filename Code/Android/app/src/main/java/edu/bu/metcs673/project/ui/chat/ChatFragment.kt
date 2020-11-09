
package edu.bu.metcs673.project.ui.chat

//import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
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
import edu.bu.metcs673.project.util.UIUtil
import java.net.URL
import kotlin.math.max

class ChatFragment : androidx.fragment.app.Fragment() {
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

        //Every time user taps "Message" tab on controller, it will initiate the view
        //Pass view, return viewModel
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel.getAllMessages()?.observe(this, Observer {
            mMessageAdapter.setMessages(it)

            // Update cycle view based on allMessages array
            mMessageAdapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(max(it.size - 1, 0))
        })
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = mMessageAdapter
        recyclerView.setOnTouchListener { view, event ->
            val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
            false
        }

        //When user hits send, give response action
        val sendButton = view.findViewById<Button>(R.id.button_chatbox_send)
        val userInput = view.findViewById<EditText>(R.id.user_text_chatbox)
        sendButton.setOnClickListener {
            sendMessage(userInput.text.toString(), viewModel, userId as String, MainActivity.currentUser.profilePicture, userInput)
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        // viewModel.unobserve()
    }

    private fun sendMessage(text: String, viewModel: ChatViewModel, userId: String, profilePicture: URL?, userInput: EditText) {
        // generate the user message
        val userMessage = MessageModel(text, true, userId, profilePicture?.toString())
        viewModel.insert(userMessage)
        // notify observers data has changed
        mMessageAdapter.notifyDataSetChanged()
        // add scrollable effect
        if (!UIUtil.isTextVisible(recyclerView, mMessageAdapter.itemCount)) {
            recyclerView.smoothScrollToPosition(max(0, mMessageAdapter.itemCount - 1))
        }
        // clear the user input
        userInput.setText("")
    }
}