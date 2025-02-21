package com.weproud.app.support.restdoc

import com.weproud.core.ObjectMapperFactory
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.JsonPathResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

fun ResultActions.andExpectStatusOk(): ResultActions = andExpect(status().isOk)

fun ResultActions.andExpectStatusCreated(): ResultActions = andExpect(status().isCreated)

fun ResultActions.andExpectStatusBadRequest(): ResultActions = andExpect(status().isBadRequest)

fun ResultActions.andExpectStatusInternalServer(): ResultActions = andExpect(status().isInternalServerError)

fun ResultActions.andExpectStatusGone(): ResultActions = andExpect(status().isGone)

fun ResultActions.andExpectNotFound(): ResultActions = andExpect(status().isNotFound)

fun ResultActions.andExpectStatusNotFound(): ResultActions = andExpect(status().isNotFound)

fun ResultActions.andExpectNoContent(): ResultActions = andExpect(status().isNoContent)

fun ResultActions.andDocument(
    identifier: String,
    vararg snippets: Snippet,
): ResultActions {
    return andDo(document(identifier, *snippets))
}

fun ResultActions.andExpectData(vararg matchers: ResultMatcher): ResultActions {
    return andExpectAll(*matchers)
}

fun <T> ResultActions.andExpectResponse(t: T): ResultActions =
    andExpect(
        MockMvcResultMatchers.content()
            .json(ObjectMapperFactory.objectMapper().writeValueAsString(t)),
    )

infix fun JsonPathResultMatchers.shouldBe(expectedValue: Any): ResultMatcher {
    return this.value(expectedValue)
}
