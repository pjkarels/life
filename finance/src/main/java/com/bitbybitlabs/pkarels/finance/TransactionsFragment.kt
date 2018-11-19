package com.bitbybitlabs.pkarels.finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bitbybitlabs.life.Util
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import com.bitbybitlabs.pkarels.finance.ui.TransactionsListAdapter


class TransactionsFragment : Fragment() {

    private lateinit var viewModel: TransactionsViewModel
    private lateinit var contentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(TransactionsViewModel::class.java)
        viewModel.transactions().observe(this, Observer {
            configureTransaction(it)
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.getTransactions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        contentView = inflater.inflate(R.layout.fragment_transactions, container, false)

        return contentView
    }

    private fun configureTransaction(transactions: List<TransactionEntity>) {
        val totalBalanceView = contentView.findViewById<TextView>(R.id.total_balance_text)
        val availableBalanceView = contentView.findViewById<TextView>(R.id.available_balance_text)
        totalBalanceView.text = Util.round(if (transactions.isNotEmpty()) transactions[0].resultingBalance else 0.0, 2, true)
        availableBalanceView.text = Util.round(if (transactions.isNotEmpty()) transactions[0].resultingBalance else 0.0, 2, true)
        val fab = contentView.findViewById<View>(R.id.fab)
        fab.setOnClickListener { view ->
            TransactionDialogFragment.newInstance(if (transactions.isNotEmpty()) transactions[0].resultingBalance else 0.0)
                    .show(requireFragmentManager(), TransactionDialogFragment::javaClass.name)
        }

        val recyclerView = contentView.findViewById<RecyclerView>(R.id.transactions_recycler_view)
        recyclerView.adapter = TransactionsListAdapter(transactions, activity as AppCompatActivity)
    }

    fun saveTransaction(transaction: TransactionEntity) {
        viewModel.addOrUpdateTransaction(transaction)
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModel.deleteTransaction(transaction)
    }
}
