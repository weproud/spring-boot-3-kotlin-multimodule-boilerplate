package com.weproud.app.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.weproud.app.api.ErrorResponse
import com.weproud.app.config.logging.CachedBodyHttpServletRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.UUID
import mu.KotlinLogging

private val klogger = KotlinLogging.logger {}

@Component
class RequestResponseFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            try {
                // 아래 4줄 수정 금지
                val traceId = UUID.randomUUID().toString().substring(0, 8)
                MDC.put("traceId", traceId)
                val requestWrapper = CachedBodyHttpServletRequest(request)
                // 반드시 설정해야함. ApiRequestResponseAspect 참고
                requestWrapper.setAttribute("traceId", traceId)

                val requestUri = request.requestURI
                MDC.put("requestUri", requestUri)
                MDC.put("method", request.method)

                filterChain.doFilter(requestWrapper, response)
            } catch (e: Exception) {
                klogger.error(e.message)
                errorResponse(
                    response,
                    ErrorResponse(message = "에러"),
                )
            }
        } finally {
            MDC.clear()
        }
    }

    private fun errorResponse(
        response: HttpServletResponse,
        errorResponse: ErrorResponse,
    ) {
        try {
            response.characterEncoding = "UTF-8"
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = HttpStatus.BAD_REQUEST.value()
            response.writer.use {
                it.print(objectMapper.writeValueAsString(errorResponse))
                it.flush()
            }
        } catch (e: IOException) {
            klogger.warn("IOException Occur")
            throw RuntimeException()
        }
    }
}
