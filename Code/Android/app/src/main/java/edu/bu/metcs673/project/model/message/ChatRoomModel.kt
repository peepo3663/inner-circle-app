package edu.bu.metcs673.project.model.message

import com.google.firebase.Timestamp
import edu.bu.metcs673.project.model.user.User

class ChatRoomModel(val chatRoomId: String?, data: Map<String, Any?>?) {
    val userCount = data?.get("users_count") as Long?
    val createdAt: Timestamp? = data?.get("createdAt") as Timestamp?
    var users: ArrayList<User>? = null
    val userIds: ArrayList<String>? = data?.get("userIds") as ArrayList<String>?
    val messages = data?.get("messages")
    val latestText = data?.get("latestText") as String?

    init {
        val users = data?.get("users") as ArrayList<Map<String, Any?>?>?
        val usersList = ArrayList<User>()
        if (users != null) {
            users.forEach { myUser ->
                usersList.add(User(myUser?.get("uid") as String, myUser))
            }
            this.users = usersList
        }
    }
}