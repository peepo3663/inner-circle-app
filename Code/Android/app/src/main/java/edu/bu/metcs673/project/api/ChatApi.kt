package edu.bu.metcs673.project.api

import edu.bu.metcs673.project.model.message.ChatRoomModel
import edu.bu.metcs673.project.model.user.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {

    @POST("/chats/create")
    fun createChatRoom(@Body chatRoomModel: ChatRoomModel): Call<ChatRoomModel>
}