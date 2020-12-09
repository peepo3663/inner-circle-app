package edu.bu.metcs673.project.api

import edu.bu.metcs673.project.model.chat.MessageModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageAPI {

    @POST("/messages/send")
    fun sendMessage(@Body message: MessageModel): Call<MessageModel>
}