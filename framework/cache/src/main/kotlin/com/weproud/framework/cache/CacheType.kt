package com.weproud.framework.cache

enum class CacheType(
    val ttl: Long,
) {
    USER_SEARCH(60 * 60),
    USER_LIST(60 * 60),
    USER_DETAIL(60 * 60),
}
