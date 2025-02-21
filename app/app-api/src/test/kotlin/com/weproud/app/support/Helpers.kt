package com.weproud.app.support

import org.springframework.http.HttpMethod
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request

object Helpers {
    fun requestGet(
        urlTemplate: String,
        vararg urlVariables: Any,
    ) = request(HttpMethod.GET, urlTemplate, urlVariables)
}
