package com.bitbybitlabs.weather.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.bitbybitlabs.weather.entity.CurrentConditionEntity
import com.bitbybitlabs.weather.entity.HourlyForecastsEntity
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