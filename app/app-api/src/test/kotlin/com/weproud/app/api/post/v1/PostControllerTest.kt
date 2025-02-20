package com.weproud.app.api.post.v1

import com.ninjasquad.springmockk.MockkBean
import com.weproud.app.api.ControllerTestSupport
import com.weproud.app.api.andDocument
import com.weproud.app.api.andExpectResponse
import com.weproud.app.api.andExpectStatusOk
import com.weproud.app.api.post.v1.dto.PostResponse
import io.mockk.every
import java.time.ZonedDateTime
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

@WebMvcTest(controllers = [PostController::class])
class PostControllerTest : ControllerTestSupport() {
    @MockkBean
    private lateinit var postService: PostService

    @Test
    fun getPosts() {
        // given
        val expectation = listOf(
            PostResponse(
                id = 1L,
                userId = 1L,
                title = "title",
                content = "content",
                createdAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
                updatedAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
            ),
            PostResponse(
                id = 2L,
                userId = 2L,
                title = "title",
                content = "content",
                createdAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
                updatedAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
            ),
        )
        every { postService.getPosts() } returns expectation

        // when, then
        mockMvcGet("/api/v1/posts")
            .andExpectStatusOk()
            .andExpectResponse(expectation)
            .andDocument("users")
    }
}