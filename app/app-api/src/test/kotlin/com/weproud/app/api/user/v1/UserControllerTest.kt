package com.weproud.app.api.user.v1

import com.ninjasquad.springmockk.MockkBean
import com.weproud.app.api.ControllerTestSupport
import com.weproud.app.api.WithMockCustomerUser
import com.weproud.app.api.andDocument
import com.weproud.app.api.andExpectResponse
import com.weproud.app.api.andExpectStatusOk
import com.weproud.app.api.user.v1.dto.UserResponse
import com.weproud.app.config.SecurityConfig
import com.weproud.app.config.filter.JwtAuthenticationFilter
import io.mockk.every
import java.time.ZonedDateTime
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WebMvcTest(
    controllers = [UserController::class],
//    includeFilters = [
//        ComponentScan.Filter(
//            type = FilterType.ASSIGNABLE_TYPE,
//            classes = [SecurityConfig::class, JwtAuthenticationFilter::class],
//        ),
//    ],
)
class UserControllerTest : ControllerTestSupport() {
    @MockkBean
    private lateinit var userService: UserService

    @Test
    @WithMockCustomerUser
    fun getUsers() {
        // given
        val expectation = UserResponse(
            id = 1L,
            email = "email",
            createdAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
            updatedAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
        )
        every { userService.getUser(any(), any<Long>()) } returns expectation

        // when, then
        mockMvcGet("/api/v1/users/1")
            .andExpectStatusOk()
            .andExpectResponse(expectation)
            .andDocument("users")
    }
}