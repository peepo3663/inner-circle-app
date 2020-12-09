package edu.bu.metcs673.project.model.chat

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.annotations.SerializedName
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MessageModel(val messageId: String?, map: Map<String, Any?>?) {
    var text: String? = null
    var isOwnMessage: Boolean? = null
    var userId: String? = null
    var userName: String? = null

    @SerializedName("profile_picture")
    var profilePicture: URL? = null

    var createdAt: Date? = null
    var chatRoomId: String? = null

    init {
        this.text = map?.get("text") as String?
        this.userName = map?.get("userName") as String?
        val createdAt = map?.get("createdAt") as Timestamp?
        if (createdAt != null) {
            this.createdAt = createdAt.toDate()
        }
        val profilePictureString = map?.get("profile_picture") as String?
        profilePictureString?.let {
            profilePicture = try { URL(it) } catch (e: MalformedURLException) { null }
        }
        this.userId = map?.get("userId") as String?
        this.chatRoomId = map?.get("chatRoomId") as String?
    }

    constructor(documentSnapshot: DocumentSnapshot): this(documentSnapshot.id, documentSnapshot.data)
}