package edu.bu.metcs673.project.ui.message

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import edu.bu.metcs673.project.adapter.message.MessageListAdapter
import edu.bu.metcs673.project.model.chat.MessageModel
import edu.bu.metcs673.project.model.message.ChatRoomModel
import edu.bu.metcs673.project.ui.chat.ChatViewModel
import edu.bu.metcs673.project.util.OnItemClickListener
import java.net.URL
import kotlin.math.max

class MessageListFragment: Fragment(), OnItemClickListener {
    companion object {
        private const val TAG = "MessageListFragment"
    }

    private lateinit var mMessageAdapter: MessageListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MessageListViewModel

    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message_list, container, false)

        recyclerView = view.findViewById(R.id.messageListRecyclerView)

        mMessageAdapter = MessageListAdapter(this)
        viewModel = ViewModelProviders.of(this).get(MessageListViewModel::class.java)
        viewModel.getAllChatrooms().observe(this, Observer {
            mMessageAdapter.setChatRoom(it)
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

        return view
    }

    override fun onItemClicked(chatRoomModel: ChatRoomModel) {

    }
}