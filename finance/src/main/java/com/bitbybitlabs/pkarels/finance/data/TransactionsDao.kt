package com.bitbybitlabs.pkarels.finance.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Flowable
import org.threeten.bp.LocalDateTime

@Dao
interface TransactionsDao {

    @Insert(onConflict = REPLACE)
    fun upsertTransaction(transactionEntity: TransactionEntity)

    @Delete
    fun deleteTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM 'transaction' WHERE transaction_date < :dateTime")
    fun getTransactions(dateTime: LocalDateTime = LocalDateTime.now()): Flowable<List<TransactionEntity>>

    @Query("SELECT * FROM `transaction` WHERE transaction_date BETWEEN :startDate AND :endDate")
    fun getTransactionsInRange(startDate: LocalDateTime, endDate: LocalDateTime): Flowable<List<TransactionEntity>>

    @Query("SELECT * FROM `transaction` WHERE transaction_date < :transactionDate LIMIT 1")
    fun getPreviousTransaction(transactionDate: LocalDateTime): Flowable<TransactionEntity>
}