package com.weproud.app.api

import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithMockUser(roles = ["USER"])
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomerUser(
    val id: Long = 1L,
    val email: String = "test@weproud.com",
    val name: String = "test",
)
