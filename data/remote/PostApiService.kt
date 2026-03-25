package com.udes.taskmanager.data.remote

import retrofit2.http.*

interface PostApiService {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @PATCH("posts/{id}")
    suspend fun updateFavorite(
        @Path("id") id: Int,
        @Body body: Map<String, Boolean>
    ): PostDto
}

data class PostDto(val id: Int, val title: String, val body: String, val userId: Int)