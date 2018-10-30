package com.nerdery.pkarels.weather.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.nerdery.pkarels.weather.entity.CurrentConditionEntity
import com.nerdery.pkarels.weather.entity.HourlyForecastsEntity
import io.reactivex.Flowable
import org.threeten.bp.LocalDateTime

@Dao
interface CurrentConditionDao {
    @Insert(onConflict = REPLACE)
    fun saveCurrentConditions(currentConditionEntity: CurrentConditionEntity)

    @Insert(onConflict = REPLACE)
    fun saveHourlyConditions(hourlyConditions: List<HourlyForecastsEntity>)

    @Query("DELETE FROM current_condition WHERE time < :time")
    fun deleteCurrentConditions(time: LocalDateTime = LocalDateTime.now())

    @Query("DELETE FROM hour_forecasts WHERE time < :time")
    fun deleteHourlyConditions(time: LocalDateTime = LocalDateTime.now())

    @Query("SELECT * FROM current_condition WHERE zipcode = :zipcode")
    fun loadCurrentConditions(zipcode: Long): Flowable<List<CurrentConditionEntity>>

    @Query("SELECT * FROM hour_forecasts WHERE zipcode = :zipcode")
    fun loadHourlyConditions(zipcode: Long): Flowable<List<HourlyForecastsEntity>>
}