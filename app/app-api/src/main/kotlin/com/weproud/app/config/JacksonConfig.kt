package com.weproud.app.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.weproud.core.ObjectMapperFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {
    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapperFactory.objectMapper()
}
