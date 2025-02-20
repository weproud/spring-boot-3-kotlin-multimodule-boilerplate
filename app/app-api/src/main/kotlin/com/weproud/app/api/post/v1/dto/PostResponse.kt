package com.weproud.app.api.post.v1.dto

import java.time.ZonedDateTime

data class PostResponse(
    val id: Long,
    val userId: Long,
    val title: String,
    val content: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
