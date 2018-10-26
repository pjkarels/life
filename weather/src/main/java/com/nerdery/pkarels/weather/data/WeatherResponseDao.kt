package com.nerdery.pkarels.weather.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.nerdery.pkarels.life.entity.CurrentEntity

@Dao
interface WeatherResponseDao {
    @Insert(onConflict = REPLACE)
    fun save(condition: CurrentEntity)

    @Query("SELECT * FROM current_weather")
    fun load(): LiveData<CurrentEntity>
}