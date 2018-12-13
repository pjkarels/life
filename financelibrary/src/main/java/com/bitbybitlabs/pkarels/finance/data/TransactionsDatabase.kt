package com.bitbybitlabs.pkarels.finance.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bitbybitlabs.life.converter.ThreeTenDateTimeConverters

@Database(entities = [TransactionEntity::class], version = 1)
@TypeConverters(ThreeTenDateTimeConverters::class)
abstract class TransactionsDatabase : RoomDatabase() {
    companion object {
        fun create(application: Application) = Room
                .databaseBuilder(application, TransactionsDatabase::class.java, "transactions.db")
                .build()
    }

    abstract fun transactionDao(): TransactionsDao
}