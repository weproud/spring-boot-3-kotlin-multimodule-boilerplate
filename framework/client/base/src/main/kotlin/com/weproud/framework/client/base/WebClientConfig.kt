package com.weproud.framework.client.base

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration

// application.yaml로 속성 빼기

@Configuration
class WebClientConfig {
    @Bean(name = ["webClientObjectMapper"])
    fun webClientObjectMapper(): ObjectMapper = defaultWebClientObjectMapper()

    @Bean
    fun webClient(
        httpClient: HttpClient,
        exchangeStrategies: ExchangeStrategies,
    ): WebClient =
        WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .exchangeStrategies(exchangeStrategies)
            .build()

    @Bean
    fun defaultHttpClient(provider: ConnectionProvider): HttpClient =
        HttpClient.create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(10))
                    .addHandlerLast(WriteTimeoutHandler(10))
            }

    @Bean
    fun connectionProvider(): ConnectionProvider =
        ConnectionProvider.builder("http-pool")
            .maxConnections(100)
            .pendingAcquireTimeout(Duration.ofMillis(0))
            .pendingAcquireMaxCount(-1)
            .maxIdleTime(Duration.ofMillis(2000L))
            .build()

    @Bean
    fun defaultExchangeStrategies(
        @Qualifier("webClientObjectMapper") objectMapper: ObjectMapper,
    ): ExchangeStrategies =
        ExchangeStrategies.builder()
            .codecs { codec ->
                codec.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
                codec.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON))
                codec.defaultCodecs().maxInMemorySize(1024 * 1024)
            }
            .build()

    companion object {
        fun defaultWebClientObjectMapper(): ObjectMapper =
            jacksonObjectMapper()
                .findAndRegisterModules()
                // toJson()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                // fromJson()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
                .disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
    }
}
