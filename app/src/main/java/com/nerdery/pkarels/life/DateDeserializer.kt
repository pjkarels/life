package com.nerdery.pkarels.life

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.*

class DateDeserializer : JsonDeserializer<Date> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type, context: JsonDeserializationContext): Date? {
        return if (json == null) null else Date(json.asLong * 1000)
    }

}
