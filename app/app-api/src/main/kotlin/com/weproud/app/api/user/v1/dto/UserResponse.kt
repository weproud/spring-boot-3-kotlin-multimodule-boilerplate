package com.weproud.app.api.user.v1.dto

import java.time.ZonedDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
