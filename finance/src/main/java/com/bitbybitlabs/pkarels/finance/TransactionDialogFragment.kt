package com.bitbybitlabs.pkarels.finance

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.CheckedTextView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import com.bitbybitlabs.pkarels.finance.ui.TransactionSavedListener
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class TransactionDialogFragment : DialogFragment() {
    private lateinit var listener: TransactionSavedListener
    private val previousBalance = 0.0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
                .setView(R.layout.fragment_transaction_dialog)
                .setTitle("Add/Edit Transaction")
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    listener.onTransactionSaved(createTransaction())
                }
                .setNegativeButton(android.R.string.cancel) { dialog, which ->
                    dialog.dismiss()
                }
                .create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is TransactionSavedListener) {
            listener = context
        } else {
            throw ClassCastException(context.javaClass.name + "does not implement" + TransactionSavedListener::class.java.simpleName)
        }
    }

    private fun createTransaction(): TransactionEntity {
        val transactionTypeView = dialog?.findViewById<EditText>(R.id.transaction_type)
        val transactionDateView = dialog?.findViewById<EditText>(R.id.transaction_date)
        val transactionDescriptionView = dialog?.findViewById<EditText>(R.id.transaction_description)
        val transactionCreditView = dialog?.findViewById<CheckedTextView>(R.id.transaction_isCredit)
        val transactionAmountView = dialog?.findViewById<EditText>(R.id.transaction_amount)
        val transactionClearedView = dialog?.findViewById<CheckedTextView>(R.id.transaction_cleared)

        val transactionType = transactionTypeView?.text ?: ""
        val transactionDate = transactionDateView?.text ?: ""
        val transactionDescription = transactionDescriptionView?.text ?: ""
        val transactionCredit = transactionCreditView?.isChecked ?: false
        val transactionAmountString = transactionAmountView?.text ?: ""
        val transactionCleared = transactionClearedView?.isChecked ?: false

        val transactionAmount = transactionAmountString.sumByDouble { t -> t.toDouble() }

        val transaction = TransactionEntity(parseDateString(transactionDate.toString()),
                transactionType.toString(),
                transactionCredit,
                transactionAmount,
                transactionDescription.toString(),
                transactionCleared,
                previousBalance)


        return transaction
    }

    private fun parseDateString(dateString: String): LocalDateTime {
        val useDateString = dateString + " 12:00"
        val date = LocalDateTime.parse(useDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm", Locale.US))

        return date
    }
}