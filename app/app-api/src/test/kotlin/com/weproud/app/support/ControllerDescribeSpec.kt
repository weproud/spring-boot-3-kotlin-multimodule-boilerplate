package com.weproud.app.support

import com.weproud.app.config.auth.AuthRole
import com.weproud.app.config.auth.CustomUserDetails
import com.weproud.app.support.restdoc.OBJECT
import com.weproud.app.support.restdoc.RestDocsField
import com.weproud.app.support.restdoc.STRING
import com.weproud.app.support.restdoc.responseBody
import com.weproud.app.support.restdoc.type
import com.weproud.core.ObjectMapperFactory
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import java.nio.charset.StandardCharsets

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
            dataOptional: Boolean = false,
        ): ResponseFieldsSnippet {
            return responseBody(
                "message" type STRING means "응답 메시지",
                "data" type OBJECT means "응답 데이터" isOptional dataOptional,
            ).andWithPrefix("data.", fields.map { it.descriptor })
        }

        fun mockRequest(
            httpMethod: HttpMethod,
            urlTemplate: String,
            vararg urlVariables: Any,
        ) = request(httpMethod, urlTemplate, urlVariables)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8.name())

        fun requestGet(
            urlTemplate: String,
            vararg urlVariables: Any,
        ) = mockRequest(HttpMethod.GET, urlTemplate, *urlVariables)

        fun requestPost(
            urlTemplate: String,
            vararg urlVariables: Any,
        ) = mockRequest(HttpMethod.POST, urlTemplate, *urlVariables)

        fun withMockUser(
            id: Long = 1,
            name: String = "weproud",
            block: () -> Unit,
        ) {
            val customUserDetails = CustomUserDetails(id, name)
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    "N/A",
                    arrayOf(AuthRole.USER.name).map { SimpleGrantedAuthority("ROLE_$it") },
                )
            block()
            SecurityContextHolder.clearContext()
        }
    }
}
