package com.weproud.app.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

data class BaseResponse<T>(
    val message: String = "success",
    val data: T? = null,
) {
    companion object {
        fun <T> ok(data: T? = null): BaseResponse<T> = BaseResponse(data = data)
    }
}

data class ErrorResponse(val message: String)

data class PageResponse<T>(
    val content: List<T>,
    val pageable: PageableResponse,
    val totalElements: Long,
//    val last: Boolean,
//    val first: Boolean,
    val empty: Boolean,
    val number: Int,
    val size: Int,
    val numberOfElements: Int,
    val totalPages: Int,
//    val sort: Sort,
)

data class PageableResponse(
    val offset: Long,
    val pageNumber: Int,
    val pageSize: Int,
//    val paged: Boolean,
//    val unpaged: Boolean,
//    val sort: Sort,
)

fun Pageable.toPageableResponse(): PageableResponse =
    PageableResponse(
        offset = offset,
        pageNumber = pageNumber,
        pageSize = pageSize,
//        paged = isPaged,
//        unpaged = isUnpaged,
//        sort = sort,
    )

fun <T> Page<T>.toPageResponse(): PageResponse<T> =
    PageResponse(
        content = content,
        pageable = pageable.toPageableResponse(),
        totalElements = totalElements,
//        last = isLast,
//        first = isFirst,
        empty = isEmpty,
        number = number,
        size = size,
        numberOfElements = numberOfElements,
        totalPages = totalPages,
//        sort = sort,
    )
