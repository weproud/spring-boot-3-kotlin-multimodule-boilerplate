package com.weproud.app.api.user.v1

import com.weproud.app.api.user.v1.dto.UserResponse
import com.weproud.app.config.auth.AuthUser
import com.weproud.app.config.auth.CustomUserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/{id}")
    fun getUser(
        @AuthUser authUser: CustomUserDetails,
        @PathVariable(name = "id") id: Long,
    ): UserResponse {
        return userService.getUser(authUser, id)
    }
}
