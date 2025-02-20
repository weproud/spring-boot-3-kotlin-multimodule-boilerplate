package com.weproud.app.api

import com.weproud.domain.rds.user.UserRepository
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ExtendWith(SpringExtension::class, MockKExtension::class)
abstract class ServiceTestSupport {
    @MockK
    lateinit var userRepository: UserRepository

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }
}
