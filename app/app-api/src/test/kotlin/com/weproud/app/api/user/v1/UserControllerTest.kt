package com.weproud.app.api.user.v1

import com.ninjasquad.springmockk.MockkBean
import com.weproud.app.api.ControllerTestSupport
import com.weproud.app.api.andDocument
import com.weproud.app.api.andExpectResponse
import com.weproud.app.api.andExpectStatusOk
import com.weproud.app.api.user.v1.dto.UserResponse
import com.weproud.app.support.WithMockCustomerUser
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime

@WebMvcTest(controllers = [UserController::class])
class UserControllerTest : ControllerTestSupport() {
    @MockkBean
    private lateinit var userService: UserService

    @Test
    @WithMockCustomerUser
    fun getUsers() {
        // given
        val expectation =
            UserResponse(
                id = 1L,
                email = "email",
                createdAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
                updatedAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
            )
        every { userService.getUser(any(), any<Long>()) } returns expectation

        // when, then
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/users/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name()),
        ).andExpectStatusOk()
            .andExpectResponse(expectation)
            .andDocument("users")
    }
}
