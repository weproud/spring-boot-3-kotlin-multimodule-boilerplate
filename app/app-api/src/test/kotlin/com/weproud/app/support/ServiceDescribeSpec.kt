package com.weproud.app.support

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ExtendWith(MockKExtension::class)
abstract class ServiceDescribeSpec(
    body: DescribeSpec.() -> Unit = {},
) : DescribeSpec(body) {
    init {
        extensions(SpringExtension)
    }

    override fun afterSpec(f: suspend (Spec) -> Unit) {
        super.afterSpec(f)
        clearAllMocks()
    }
}
