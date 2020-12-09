package edu.bu.metcs673.project.ui.listener

import edu.bu.metcs673.project.model.message.ChatRoomModel

interface OnItemClickListener {
    fun onItemClicked(chatRoomModel: ChatRoomModel)
}