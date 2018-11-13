package com.bitbybitlabs.pkarels.finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import com.bitbybitlabs.pkarels.finance.ui.TransactionsListAdapter


class TransactionsFragment : Fragment() {

    companion object {
        fun newInstance() = TransactionsFragment()
    }

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

        val fab = contentView.findViewById<View>(R.id.fab)
        fab.setOnClickListener { view ->
            TransactionDialogFragment().show(requireFragmentManager(), TransactionDialogFragment::javaClass.name)
        }
        return contentView
    }

    private fun configureTransaction(transactions: List<TransactionEntity>) {
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.transactions_recycler_view)
        recyclerView.adapter = TransactionsListAdapter(transactions)
    }

    fun saveTransaction(transaction: TransactionEntity) {
        viewModel.addOrUpdateTransaction(transaction)
    }
}
