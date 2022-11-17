package com.example.rushabhtawkto.api

import com.example.tawktopractice.data.model.User
import com.example.tawktopractice.data.model.UserDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/*
* User api interface
* */
interface UserApi {

    @GET("/users")
    suspend fun getUsers(
        @Query("since") since: Int = 0
    ): List<User>

    @GET("/users/{login}")
    suspend fun getUserDetail(
        @Path("login") login: String = ""
    ): Response<UserDetail>

}