package edu.bu.metcs673.project.ui.listener

import edu.bu.metcs673.project.model.message.ChatRoomModel
import edu.bu.metcs673.project.model.user.User

interface ChatRoomClickListener {
    fun chatRoomClick(chatRoomModel: ChatRoomModel)
    fun onProfileClick(friend: User)
}