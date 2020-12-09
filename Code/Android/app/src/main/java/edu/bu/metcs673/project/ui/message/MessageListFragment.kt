package edu.bu.metcs673.project.ui.message

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.adapter.message.MessageListAdapter
import edu.bu.metcs673.project.model.message.ChatRoomModel
import edu.bu.metcs673.project.ui.base.BaseFragment
import edu.bu.metcs673.project.ui.listener.ChatRoomClickListener
import edu.bu.metcs673.project.ui.listener.OnItemClickListener
import edu.bu.metcs673.project.util.UIUtil
import java.lang.ClassCastException
import kotlin.math.max

class MessageListFragment: BaseFragment(),
    OnItemClickListener {
    companion object {
        private const val TAG = "MessageListFragment"
    }

    private lateinit var mMessageAdapter: MessageListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MessageListViewModel

    var chatRoomClickListener: ChatRoomClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message_list, container, false)

        recyclerView = view.findViewById(R.id.messageListRecyclerView)

        mMessageAdapter = MessageListAdapter(this)
        viewModel = ViewModelProviders.of(this).get(MessageListViewModel::class.java)
        showLoader(true)
        viewModel.getAllChatrooms().observe(viewLifecycleOwner, Observer {
            showLoader(false)
            mMessageAdapter.setChatRoom(it)
            mMessageAdapter.notifyDataSetChanged()
        })
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = mMessageAdapter
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            chatRoomClickListener = context as ChatRoomClickListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ChatRoomClickListener")
        }
    }

    override fun onItemClicked(chatRoomModel: ChatRoomModel) {
        chatRoomClickListener?.chatRoomClick(chatRoomModel)
    }
}