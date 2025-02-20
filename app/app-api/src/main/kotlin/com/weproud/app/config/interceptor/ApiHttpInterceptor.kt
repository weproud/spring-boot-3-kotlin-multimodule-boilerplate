package com.weproud.app.config.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

private val logger = KotlinLogging.logger {}

@Component
class ApiHttpInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val method = request.method
        val requestPath = request.contextPath + request.servletPath
        logger.info { "$method $requestPath" }
        return true
    }
}
