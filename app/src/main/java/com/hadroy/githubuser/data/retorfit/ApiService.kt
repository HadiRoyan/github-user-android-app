package com.hadroy.githubuser.data.retorfit

import com.hadroy.githubuser.data.response.DetailUserResponse
import com.hadroy.githubuser.data.response.FollowsResponseItem
import com.hadroy.githubuser.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("search/users")
    fun getSearchUser(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<FollowsResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<FollowsResponseItem>>
}