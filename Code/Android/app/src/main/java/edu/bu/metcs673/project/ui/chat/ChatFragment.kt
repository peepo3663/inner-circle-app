package edu.bu.metcs673.project.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.adapter.chat.MessageAdapter
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {
    companion object {
        private const val TAG = "ChatFragment"
    }

    private lateinit var mMessageAdapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewMessageList)

        mMessageAdapter = MessageAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = mMessageAdapter

        val sendButton = view.findViewById<Button>(R.id.button_chatbox_send)
        sendButton.setOnClickListener {

        }

        return view
    }
}