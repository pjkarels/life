package com.bitbybitlabs.pkarels.finance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import com.bitbybitlabs.pkarels.finance.ui.TransactionSavedListener
import kotlinx.android.synthetic.main.activity_transactions.*

class TransactionsActivity : AppCompatActivity(), TransactionSavedListener {
    override fun onTransactionSaved(transaction: TransactionEntity) {
        val transactionsFragment = supportFragmentManager.findFragmentById(R.id.transactions_fragment) as TransactionsFragment
        transactionsFragment.saveTransaction(transaction)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)
        setSupportActionBar(toolbar)
    }

}
