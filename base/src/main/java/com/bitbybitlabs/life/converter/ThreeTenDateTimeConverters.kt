package com.bitbybitlabs.life.converter

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset

object ThreeTenDateTimeConverters {
    /**
     * [LocalDate] converters to/from [Long] for Room
     */
    @TypeConverter
    @JvmStatic
    fun fromLocalDate(localDate: LocalDate?) = localDate?.toEpochDay()

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: Long?) = if (value == null) null else LocalDate.ofEpochDay(value)

    /**
     * [LocalTime] converters to/from [Long] for Room
     */
    @TypeConverter
    @JvmStatic
    fun fromLocalTime(localTime: LocalTime?) = localTime?.toNanoOfDay()

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: Long?) = if (value == null) null else LocalTime.ofNanoOfDay(value)

    /**
     * [LocalDateTime] converters to/from [Long] for Room
     * Note: Nanos are truncated in this conversion
     */
    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(localDateTime: LocalDateTime?) = localDateTime?.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value: Long?) = if (value == null) null else LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC)
}