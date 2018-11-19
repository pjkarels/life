package com.bitbybitlabs.pkarels.finance.ui

import com.bitbybitlabs.pkarels.finance.data.TransactionEntity

interface TransactionModifiedListener {
    fun onTransactionSaved(transaction: TransactionEntity)
    fun onTransactionDeleted(transaction: TransactionEntity)
}