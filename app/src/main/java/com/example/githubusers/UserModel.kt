package com.example.githubusers

data class UserModel(val id: Long, val login: String, val avatar_url: String, val name: String, val company: String, val followers: Int, val blog: String, val location: String, val email: String)