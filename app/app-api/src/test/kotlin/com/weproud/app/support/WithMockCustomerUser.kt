package com.weproud.app.support

import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithMockUser(roles = ["USER"])
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomerUser(
    val id: Long = 1L,
    val name: String = "test",
)
