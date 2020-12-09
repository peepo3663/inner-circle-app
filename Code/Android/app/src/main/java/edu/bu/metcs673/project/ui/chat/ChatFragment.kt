package edu.bu.metcs673.project.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.adapter.chat.MessageAdapter
import edu.bu.metcs673.project.core.ICApp
import edu.bu.metcs673.project.factory.ChatViewModelFactory
import edu.bu.metcs673.project.model.chat.MessageModel
import edu.bu.metcs673.project.model.user.User
import edu.bu.metcs673.project.ui.base.BaseFragment
import edu.bu.metcs673.project.util.UIUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max

class ChatFragment : BaseFragment() {
    companion object {
        private const val TAG = "ChatFragment"
    }

    private lateinit var mMessageAdapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ChatViewModel

    lateinit var chatRoomId: String
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val chatRoomId = arguments?.getString("CHATROOM_ID") ?: return
        this.chatRoomId = chatRoomId
    }

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
        val parentActivity = activity ?: return null
        viewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(parentActivity.application, chatRoomId)
        ).get(ChatViewModel::class.java)
        viewModel.getAllMessages()?.observe(this, Observer {
            mMessageAdapter.setMessages(it)

            // Update cycle view based on allMessages array
            mMessageAdapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(max(it.size - 1, 0))
        })
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = mMessageAdapter
        recyclerView.setOnTouchListener { view, event ->
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
            false
        }

        //When user hits send, give response action
        val sendButton = view.findViewById<Button>(R.id.button_chatbox_send)
        val userInput = view.findViewById<EditText>(R.id.user_text_chatbox)
        sendButton.setOnClickListener {
            val myApp = activity?.application as ICApp
            sendMessage(userInput.text.toString(), viewModel, myApp.currentUser as User, userInput)
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unobserve()
    }

    private fun sendMessage(
        text: String,
        viewModel: ChatViewModel,
        currentUser: User,
        userInput: EditText
    ) {
        // generate the user message
        val data = mutableMapOf(
            "text" to text,
            "userId" to currentUser.userId,
            "profile_picture" to currentUser.profilePicture?.toString(),
            "userName" to currentUser.name,
            "createdAt" to Timestamp.now(),
            "chatRoomId" to chatRoomId
        )
        val userMessage = MessageModel(null, data)
        userMessage.isOwnMessage = true
        viewModel.insert(userMessage)
        val chatActivity = (activity as ChatActivity)
        chatActivity.showPopupProgressSpinner(true)
        messageAPI.sendMessage(userMessage).enqueue(object : Callback<MessageModel> {
            override fun onFailure(call: Call<MessageModel>, t: Throwable) {
                chatActivity.showPopupProgressSpinner(false)
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_LONG).show()
                viewModel.remove(userMessage)
            }

            override fun onResponse(call: Call<MessageModel>, response: Response<MessageModel>) {
                chatActivity.showPopupProgressSpinner(false)
                if (response.isSuccessful) {
                    sendMessageSuccessfully(userInput)
                } else {
                    Toast.makeText(context, response.errorBody()?.string() ?: "Error", Toast.LENGTH_LONG).show()
                    viewModel.remove(userMessage)
                }
            }
        })
    }

    private fun sendMessageSuccessfully(userInput: EditText) {
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