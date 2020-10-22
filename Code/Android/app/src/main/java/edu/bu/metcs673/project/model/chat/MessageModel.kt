package edu.bu.metcs673.project.model.chat

import com.google.firebase.Timestamp

data class MessageModel (
    var text: String? = null,
    var isOwnMessage: Boolean? = null,
    var userId: String? = null,
    var profile_picture: String? = null,
    var createdAt: Timestamp? = null
)