package com.weproud.app.config.advice

import com.weproud.app.api.ApiException
import com.weproud.app.api.ErrorResponse
import com.weproud.domain.DomainRdsException
import com.weproud.framework.client.base.FrameworkClientException
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private val klogger = KotlinLogging.logger {}

@RestControllerAdvice
class ExceptionControllerAdvice : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException) = ResponseEntity.status(ex.status).body(ErrorResponse(ex.message!!))

    @ExceptionHandler(DomainRdsException::class)
    fun handleDomainRdsException(ex: DomainRdsException) = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(ex.message!!))

    @ExceptionHandler(FrameworkClientException::class)
    fun handleFrameworkClientException(ex: FrameworkClientException) = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(ex.message!!))

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorResponse> {
        klogger.error(ex) { "Internal Server Error" }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse(ex.message!!))
    }

    // 최소한만 구현
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        return ResponseEntity.badRequest().body(ErrorResponse(ex.bindingResult.allErrors.first().defaultMessage!!))
    }
}
