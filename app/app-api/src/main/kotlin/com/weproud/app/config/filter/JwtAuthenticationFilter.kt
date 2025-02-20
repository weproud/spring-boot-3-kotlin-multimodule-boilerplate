package com.weproud.app.config.filter

import com.weproud.app.api.ApiException
import com.weproud.app.api.ErrorResponse
import com.weproud.app.config.auth.AuthHeader
import com.weproud.app.config.auth.CustomUserDetailsService
import com.weproud.core.ObjectMapperFactory
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

private val klogger = KotlinLogging.logger {}

class JwtAuthenticationFilter(
    private val userDetailsService: CustomUserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            resolveAuthorization(request)?.let { token ->
                val userDetails = userDetailsService.loadUserByUsername(token)
                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            }
            filterChain.doFilter(request, response)
        } catch (e: ApiException) {
            klogger.error { "인증 실패했습니다. ${e.message}" }
            errorResponse(
                response,
                e.status,
                ErrorResponse(e.message!!),
            )
        } catch (e: Exception) {
            klogger.error { "인증 실패했습니다. ${e.message}" }
            errorResponse(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorResponse("인증 실패했습니다"),
            )
        }
    }

    private fun errorResponse(
        response: HttpServletResponse,
        status: HttpStatus,
        errorResponse: ErrorResponse,
    ) {
        try {
            response.characterEncoding = "UTF-8"
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = status.value()
            response.writer.use {
                it.print(ObjectMapperFactory.objectMapper().writeValueAsString(errorResponse))
                it.flush()
            }
        } catch (e: IOException) {
            klogger.warn("IOException Occur")
            throw RuntimeException()
        }
    }

    private fun resolveAuthorization(request: HttpServletRequest): String? =
        request.getHeader(AuthHeader.AUTHORIZATION)?.let {
            if (it.startsWith("Bearer ")) {
                it.substring(7)
            } else {
                null
            }
        }

    private fun isProd(): Boolean {
        return environment.activeProfiles.any { it == "prod" }
    }
}
