package com.bitbybitlabs.pkarels.finance.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bitbybitlabs.pkarels.finance.R
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import org.threeten.bp.format.DateTimeFormatter

class TransactionsListAdapter(private val transactions: List<TransactionEntity>) : RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_view, parent, false))
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = transactions[position]
        holder.transactionTypeView.text = item.transactionType
        holder.transactionDateView.text = item.transactionDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        holder.transactionAmountView.text = item.transactionAmount.toString()
        holder.transactionDescriptionView.text = item.description
        holder.transactionIsClearedView.isChecked = item.cleared
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionTypeView = view.findViewById<TextView>(R.id.item_transaction_type)
        val transactionDateView = view.findViewById<TextView>(R.id.item_transaction_date)
        val transactionAmountView = view.findViewById<TextView>(R.id.item_transaction_amount)
        val transactionDescriptionView = view.findViewById<TextView>(R.id.item_transaction_description)
        val transactionIsClearedView = view.findViewById<CheckBox>(R.id.item_transaction_cleared)
    }
}