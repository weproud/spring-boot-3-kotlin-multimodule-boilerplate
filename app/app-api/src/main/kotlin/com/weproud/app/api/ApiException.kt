package com.weproud.app.api

import org.springframework.http.HttpStatus

open class ApiException(val status: HttpStatus, message: String) : RuntimeException(message)

// InternalServerException
open class InternalServerException(message: String = "알 수 없는 서버에러입니다") :
    ApiException(HttpStatus.INTERNAL_SERVER_ERROR, message)

// BadRequestException
open class BadRequestException(message: String) : ApiException(HttpStatus.BAD_REQUEST, message)

// auth
open class AuthException(message: String) : BadRequestException(message)

// user
open class UserNotFoundException(id: Long) : BadRequestException("사용자를 찾을 수 없습니다. id: $id")
