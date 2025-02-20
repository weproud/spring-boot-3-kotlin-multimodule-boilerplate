package com.weproud.app

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "weproud")
data class AppProperties
    @ConstructorBinding
    constructor(
        val host: HostProperties,
    )

data class HostProperties(
    val image: String,
)