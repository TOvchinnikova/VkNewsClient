package com.example.vknewsclient.domain

data class PostComment(
    val id: Long,
    val authorName: String = "Author",
    val authorAvatarUrl: String,
    val commentText: String = "Long comment text",
    val publicationDate: String = "14:00"
)