package com.bitbybitlabs.pkarels.finance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import com.bitbybitlabs.pkarels.finance.ui.TransactionModifiedListener
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_transactions.*

class TransactionsActivity : AppCompatActivity(), TransactionModifiedListener {
    override fun onTransactionSaved(transaction: TransactionEntity) {
        val transactionsFragment = supportFragmentManager.findFragmentById(R.id.transactions_fragment) as TransactionsFragment
        transactionsFragment.saveTransaction(transaction)
    }

    override fun onTransactionDeleted(transaction: TransactionEntity) {
        val transactionsFragment = supportFragmentManager.findFragmentById(R.id.transactions_fragment) as TransactionsFragment
        transactionsFragment.deleteTransaction(transaction)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)
        setSupportActionBar(toolbar)
        AndroidThreeTen.init(this)
    }
}
