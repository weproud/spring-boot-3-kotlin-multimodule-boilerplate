package com.weproud.app.config.auth

import com.weproud.domain.rds.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
//        val user =
//            userRepository.findByIdOrNull(username.toLong())
//                ?: throw UserNotFoundException(username.toLong())
        return CustomUserDetails(1L, "username")
    }
}
