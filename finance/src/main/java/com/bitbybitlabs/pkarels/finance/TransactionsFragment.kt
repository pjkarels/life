package com.bitbybitlabs.pkarels.finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity


class TransactionsFragment : Fragment() {

    companion object {
        fun newInstance() = TransactionsFragment()
    }

    private lateinit var viewModel: TransactionsViewModel
    private lateinit var contentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(TransactionsViewModel::class.java)
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

    fun saveTransaction(transaction: TransactionEntity) {
        viewModel.addOrUpdateTransaction(transaction)
    }
}
