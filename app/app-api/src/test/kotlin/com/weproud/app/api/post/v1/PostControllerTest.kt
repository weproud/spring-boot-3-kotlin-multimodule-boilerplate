package com.weproud.app.api.post.v1

import com.ninjasquad.springmockk.MockkBean
import com.weproud.app.api.andDocument
import com.weproud.app.api.andExpectResponse
import com.weproud.app.api.andExpectStatusOk
import com.weproud.app.api.post.v1.dto.PostResponse
import com.weproud.app.support.ControllerDescribeSpec
import com.weproud.app.support.restdoc.RestDocsBuilder.restDocMockMvc
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.web.context.WebApplicationContext
import java.time.ZonedDateTime

@WebMvcTest(controllers = [PostController::class])
class PostControllerTest(
    @Autowired
    private val context: WebApplicationContext,
    @MockkBean(relaxed = true)
    private val postService: PostService,
) : ControllerDescribeSpec(
        {
            val baseUrl = "/api/v1/posts"
            val restDocumentation = ManualRestDocumentation()
            val mockMvc = restDocMockMvc(context, restDocumentation)
            beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
            afterEach { restDocumentation.afterTest() }

            describe("BASE: $baseUrl") {
                val url = "$baseUrl"
                context("GET: $url") {
                    val expectation =
                        listOf(
                            PostResponse(
                                id = 1L,
                                userId = 1L,
                                title = "title",
                                content = "content",
                                createdAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
                                updatedAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
                            ),
                            PostResponse(
                                id = 2L,
                                userId = 2L,
                                title = "title",
                                content = "content",
                                createdAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
                                updatedAt = ZonedDateTime.parse("2025-02-20T15:27:43+09:00"),
                            ),
                        )
                    it("200 OK") {
                        val request = requestGet(url)
                        withMockUser {
                            every { postService.getPosts() } returns expectation
                            mockMvc.perform(request)
                                .andExpectStatusOk()
                                .andExpectResponse(expectation)
                                .andDocument("posts")
                        }
                    }
                }
            }
        },
    )
