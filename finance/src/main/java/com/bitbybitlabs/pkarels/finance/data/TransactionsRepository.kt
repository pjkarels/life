package com.bitbybitlabs.pkarels.finance.data

import android.app.Application
import io.reactivex.Flowable
import org.threeten.bp.LocalDateTime


class TransactionsRepository(application: Application) {

    private val transactionsDatabase = TransactionsDatabase.create(application)
    private val transactionsDao = transactionsDatabase.transactionDao()

    fun getTransactions(date: LocalDateTime = LocalDateTime.now()): Flowable<List<TransactionEntity>> {
        val transactions = transactionsDao.getTransactions(date)
        return transactions
    }
    fun getTransactions(startDate: LocalDateTime, endDate: LocalDateTime) = transactionsDao.getTransactionsInRange(startDate, endDate)

    fun saveOrUpdateTransaction(transaction: TransactionEntity) {
        transactionsDao.upsertTransaction(transaction)
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        transactionsDao.deleteTransaction(transaction)
    }
}