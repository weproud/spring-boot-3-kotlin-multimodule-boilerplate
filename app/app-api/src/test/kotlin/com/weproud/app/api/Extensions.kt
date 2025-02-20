package com.weproud.app.api

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.weproud.core.ObjectMapperFactory
import org.springframework.test.web.servlet.ResultActions
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

fun ResultActions.andDocument(description: String): ResultActions =
    andDo(
        MockMvcRestDocumentationWrapper.document(
            identifier = "{class-name}/{method-name}",
            description = description,
        ),
    )

fun <T> ResultActions.andExpectResponse(t: T): ResultActions =
    andExpect(
        MockMvcResultMatchers.content()
            .json(ObjectMapperFactory.objectMapper().writeValueAsString(t)),
    )
