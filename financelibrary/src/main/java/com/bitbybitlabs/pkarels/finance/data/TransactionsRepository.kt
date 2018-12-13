package com.bitbybitlabs.pkarels.finance.data

import android.app.Application
import org.threeten.bp.LocalDateTime


class TransactionsRepository(application: Application) {

    private val transactionsDatabase = TransactionsDatabase.create(application)
    private val transactionsDao = transactionsDatabase.transactionDao()

    fun getTransactions(date: LocalDateTime = LocalDateTime.now()) = transactionsDao.getTransactions(date)

    fun getTransactions(startDate: LocalDateTime, endDate: LocalDateTime) = transactionsDao.getTransactionsInRange(startDate, endDate)

    fun getMoreRecentTransactions(id: Int) = transactionsDao.getMoreRecentTransactions(id)

    fun saveOrUpdateTransaction(transaction: TransactionEntity) {
        transactionsDao.upsertTransaction(transaction)
    }

    fun deleteTransaction(id: Int): Int {
        return transactionsDao.deleteTransaction(id)
    }

    fun getTransaction(id: Int) = transactionsDao.getTransaction(id)
}