package com.weproud.core

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.IOException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.jvm.Throws
import kotlin.jvm.java

object ObjectMapperFactory {
    fun objectMapper(): ObjectMapper =
        jacksonObjectMapper()
            .findAndRegisterModules()
            .registerKotlinModule()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
            .registerModule(
                JavaTimeModule()
                    .addSerializer(ZonedDateTime::class.java, ZonedDateTimeSerializer())
                    .addDeserializer(ZonedDateTime::class.java, ZonedDateTimeDeserializer()),
            )

    private class ZonedDateTimeSerializer : StdSerializer<ZonedDateTime>(ZonedDateTime::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            value: ZonedDateTime,
            generator: JsonGenerator,
            provider: SerializerProvider,
        ) {
            generator.writeString(
                value.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            )
        }
    }

    private class ZonedDateTimeDeserializer : StdDeserializer<ZonedDateTime>(ZonedDateTime::class.java) {
        @Throws(IOException::class)
        override fun deserialize(
            parser: JsonParser,
            context: DeserializationContext,
        ): ZonedDateTime {
            return InstantDeserializer.ZONED_DATE_TIME.deserialize(parser, context)
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
        }
    }
}
