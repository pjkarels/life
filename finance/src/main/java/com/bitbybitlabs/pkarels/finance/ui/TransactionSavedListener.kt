package com.bitbybitlabs.pkarels.finance.ui

import com.bitbybitlabs.pkarels.finance.data.TransactionEntity

interface TransactionSavedListener {
    fun onTransactionSaved(transaction: TransactionEntity)
}