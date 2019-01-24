package com.example.githubusers

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestfulAPi {

    // Http "GET" request to url with path "/users" with query string: "?since=", the expected result is array with users
    @GET("users")
    fun getGitHubUsers(@Query("since") since: String) : Observable<ArrayList<UserModel>>

    // Http "GET" request to url with path "/users/{login}" and user's login like param
    @GET("users/{login}")
    fun getGitHubUserDetails(@Path("login") login: String) : Observable<UserModel>

}