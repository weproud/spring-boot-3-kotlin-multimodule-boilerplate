package com.weproud.app.support

import com.weproud.app.support.restdoc.RestDocsField
import com.weproud.app.support.restdoc.responseBody
import com.weproud.core.ObjectMapperFactory
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.test.context.ActiveProfiles

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension::class, MockKExtension::class)
abstract class ControllerDescribeSpec(
    body: DescribeSpec.() -> Unit = {},
) : DescribeSpec(body) {
    init {
        extensions(SpringExtension)
    }

    override fun afterSpec(f: suspend (Spec) -> Unit) {
        super.afterSpec(f)
        clearAllMocks()
    }

    companion object {
        val objectMapper = ObjectMapperFactory.objectMapper()

        fun toJson(value: Any): String {
            return objectMapper.writeValueAsString(value)
        }

        fun envelopeResponseBody(
            vararg fields: RestDocsField,
//            dataOptional: Boolean = false,
        ): ResponseFieldsSnippet {
            return responseBody(
//                "code" type STRING means "응답 코드",
//                "message" type STRING means "응답 메시지",
//                "data" type OBJECT means "응답 데이터" isOptional dataOptional,
            ).and(fields.map { it.descriptor })
        }
    }
}
