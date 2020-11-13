package edu.bu.metcs673.project.util

import edu.bu.metcs673.project.model.message.ChatRoomModel

interface OnItemClickListener {
    fun onItemClicked(chatRoomModel: ChatRoomModel)
}