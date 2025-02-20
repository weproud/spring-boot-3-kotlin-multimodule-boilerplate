package com.weproud.app.api

import com.ninjasquad.springmockk.MockkBean
import com.weproud.app.config.SecurityConfig
import com.weproud.app.config.auth.CustomUserDetailsService
import com.weproud.core.ObjectMapperFactory
import io.mockk.junit5.MockKExtension
import java.nio.charset.StandardCharsets
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@Import(SecurityConfig::class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class, MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class ControllerTestSupport {
    val objectMapper = ObjectMapperFactory.objectMapper()

    @MockkBean
    protected lateinit var customUserDetailsService: CustomUserDetailsService

    lateinit var mockMvc: MockMvc

    @BeforeEach
    internal fun setUp(
        context: WebApplicationContext,
        provider: RestDocumentationContextProvider,
    ) {
        mockMvc =
            MockMvcBuilders.webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
                .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
                .apply<DefaultMockMvcBuilder>(documentationConfiguration(provider))
                .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
                .build()
    }

    fun <T> mockMvcPost(
        api: String,
        input: T,
    ): ResultActions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.post(api)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(input)),
        )

    fun mockMvcGet(
        api: String,
        queryParams: Map<String, String> = emptyMap()
    ): ResultActions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.get(api)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .apply {
                    queryParams.forEach { (key, value) ->
                        this.param(key, value)
                    }
                }
        )
}
