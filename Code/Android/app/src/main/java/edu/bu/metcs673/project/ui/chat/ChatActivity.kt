package edu.bu.metcs673.project.ui.chat

import android.os.Bundle
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.ui.base.BaseActivity
import edu.bu.metcs673.project.util.BundleExtraKeys
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chatRoomId = intent.getStringExtra(BundleExtraKeys.CHATROOM_ID)
        val chatRoomName = intent.getStringExtra(BundleExtraKeys.CHATROOM_NAME)
        if (chatRoomId == null) {
            onBackPressed()
            return
        }
        title = chatRoomName ?: "Chat"
        if (savedInstanceState == null) {
            val chatFragment = ChatFragment()
            val args = Bundle()
            args.putString("CHATROOM_ID", chatRoomId)
            chatFragment.arguments = args
            supportFragmentManager.beginTransaction().add(R.id.chat_container, chatFragment).commit()
        }
    }
}