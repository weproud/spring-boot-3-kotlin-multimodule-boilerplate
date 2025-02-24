package com.weproud.app.support

import com.weproud.app.config.auth.CustomUserDetails
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomerUser> {
    override fun createSecurityContext(withMockCustomerUser: WithMockCustomerUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val principal =
            CustomUserDetails(
                id = withMockCustomerUser.id,
                name = withMockCustomerUser.name,
            )
        val auth: Authentication = UsernamePasswordAuthenticationToken(principal, "password", principal.authorities)
        context.authentication = auth
        return context
    }
}
