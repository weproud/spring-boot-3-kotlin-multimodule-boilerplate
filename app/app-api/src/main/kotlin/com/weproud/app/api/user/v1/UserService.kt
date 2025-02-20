package com.weproud.app.api.user.v1

import com.weproud.app.api.UserNotFoundException
import com.weproud.app.api.user.v1.dto.UserResponse
import com.weproud.app.config.auth.CustomUserDetails
import com.weproud.domain.rds.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun getUser(
        authUser: CustomUserDetails,
        id: Long,
    ): UserResponse {
        val user = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException(id)
        return UserResponse(
            id = user.id,
            email = user.email,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
        )
    }
}
