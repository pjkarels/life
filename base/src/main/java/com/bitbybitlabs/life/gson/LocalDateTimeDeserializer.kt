package com.bitbybitlabs.life.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import java.lang.reflect.Type

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {
    override fun deserialize(json: JsonElement?, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime? {
        if (json == null) return null
        return LocalDateTime.ofEpochSecond(json.asLong, 0, ZoneOffset.UTC)
    }
}