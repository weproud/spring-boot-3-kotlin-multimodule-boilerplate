package com.weproud.app.api.post.v1

import com.weproud.app.api.post.v1.dto.PostResponse
import com.weproud.domain.rds.user.UserRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class PostService(
    private val userRepository: UserRepository,
) {
    fun getPosts(): List<PostResponse> {
        return listOf(
            PostResponse(
                id = 1L,
                userId = 1L,
                title = "title",
                content = "content",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            ),
            PostResponse(
                id = 2L,
                userId = 2L,
                title = "title",
                content = "content",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            ),
        )
    }
}
