package edu.bu.metcs673.project.api

import edu.bu.metcs673.project.model.user.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/users/create")
    fun loginUser(@Body userData: User): Call<User>
}