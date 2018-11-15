package com.bitbybitlabs.pkarels.finance.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bitbybitlabs.life.Util
import com.bitbybitlabs.pkarels.finance.R
import com.bitbybitlabs.pkarels.finance.TransactionDialogFragment
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import org.threeten.bp.format.DateTimeFormatter

class TransactionsListAdapter(private val transactions: List<TransactionEntity>,
                              private val activity: AppCompatActivity)
    : RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_view, parent, false))
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = transactions[position]
        holder.transactionTypeView.text = item.transactionType
        holder.transactionDateView.text = item.transactionDate.format(DateTimeFormatter.ofPattern(Util.DATE_PATTERN))
        holder.transactionAmountView.text = item.transactionAmount.toString()
        if (!item.isCredit) {
            holder.transactionAmountView.setTextColor(activity.resources.getColor(android.R.color.holo_red_light))
        }
        holder.transactionBalanceview.text = Util.round(item.resultingBalance, 2)
        holder.transactionDescriptionView.text = item.description
        holder.transactionIsClearedView.isChecked = item.cleared
        holder.itemView.tag = item
        holder.itemView.setOnClickListener { v ->
            val previousBalance = transactions[position - 1].resultingBalance
            val transactionDialog = TransactionDialogFragment.newInstance(previousBalance, (v.tag as TransactionEntity).id)
            transactionDialog.show(activity.supportFragmentManager, TransactionDialogFragment::javaClass.name)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionTypeView = view.findViewById<TextView>(R.id.item_transaction_type)
        val transactionDateView = view.findViewById<TextView>(R.id.item_transaction_date)
        val transactionAmountView = view.findViewById<TextView>(R.id.item_transaction_amount)
        val transactionBalanceview = view.findViewById<TextView>(R.id.item_transaction_balance)
        val transactionDescriptionView = view.findViewById<TextView>(R.id.item_transaction_description)
        val transactionIsClearedView = view.findViewById<CheckBox>(R.id.item_transaction_cleared)
    }
}