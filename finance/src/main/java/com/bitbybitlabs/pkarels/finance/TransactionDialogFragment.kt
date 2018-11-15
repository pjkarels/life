package com.bitbybitlabs.pkarels.finance

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bitbybitlabs.life.Util
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import com.bitbybitlabs.pkarels.finance.model.TransactionViewModel
import com.bitbybitlabs.pkarels.finance.ui.TransactionSavedListener
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class TransactionDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(transactionId: Int? = null): TransactionDialogFragment {
            val fragment = TransactionDialogFragment()
            if (transactionId != null) {
                val args = Bundle()
                args.putInt(Util.ARGS_BUNDLE_TRANSACTION_ID, transactionId)
                fragment.arguments = args
            }

            return fragment
        }
    }

    private lateinit var viewModel: TransactionViewModel
    private lateinit var listener: TransactionSavedListener
    private val previousBalance = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        viewModel.transactionData.observe(this, Observer {
            configureTransaction(it)
        })

        // retrieve transaction if an id is passed for an edit
        val myArguments = arguments
        if (myArguments != null && myArguments.containsKey(Util.ARGS_BUNDLE_TRANSACTION_ID)) {
            viewModel.fetchTransaction(myArguments.getInt(Util.ARGS_BUNDLE_TRANSACTION_ID))
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
                .setView(R.layout.fragment_transaction_dialog)
                .setTitle("Add/Edit Transaction")
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    listener.onTransactionSaved(updateTransaction())
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

    /**
     * Updates the current (or new) transaction object from the input
     */
    private fun updateTransaction(): TransactionEntity {
        val currentTransaction = viewModel.transactionData.value as TransactionEntity

        val transactionTypeView = dialog?.findViewById<EditText>(R.id.transaction_type)
        val transactionDateView = dialog?.findViewById<EditText>(R.id.transaction_date)
        val transactionDescriptionView = dialog?.findViewById<EditText>(R.id.transaction_description)
        val transactionCreditView = dialog?.findViewById<CheckBox>(R.id.transaction_isCredit)
        val transactionAmountView = dialog?.findViewById<EditText>(R.id.transaction_amount)
        val transactionClearedView = dialog?.findViewById<CheckBox>(R.id.transaction_cleared)

        // retrieve or initialize values to default
        val transactionType = transactionTypeView?.text.toString()
        val transactionDate = transactionDateView?.text.toString()
        val transactionDescription = transactionDescriptionView?.text.toString()
        val transactionCredit = transactionCreditView?.isChecked ?: false
        val transactionAmountString = transactionAmountView?.text.toString()
        val transactionAmount = Util.stringToDouble(transactionAmountString)
        val transactionCleared = transactionClearedView?.isChecked ?: false

        currentTransaction.transactionType = transactionType
        currentTransaction.transactionDate = parseDateString(transactionDate)
        currentTransaction.description = transactionDescription
        currentTransaction.isCredit = transactionCredit
        currentTransaction.transactionAmount = transactionAmount
        currentTransaction.cleared = transactionCleared
        currentTransaction.resultingBalance = previousBalance

        return currentTransaction
    }

    private fun parseDateString(dateString: String): LocalDateTime {
        val useDateString = "$dateString ${Util.DEFAULT_TIME}"
        return LocalDateTime.parse(useDateString, DateTimeFormatter.ofPattern(Util.DEFAULT_DATE_ENTRY_PARSE_PATTERN, Locale.US))
    }

    private fun configureTransaction(transaction: TransactionEntity) {
        val dialogView = requireDialog()
        dialogView.findViewById<TextView>(R.id.transaction_type).text = transaction.transactionType
        dialogView.findViewById<TextView>(R.id.transaction_date).text = transaction.transactionDate.format(DateTimeFormatter.ofPattern(Util.DATE_PATTERN, Locale.US))
        dialogView.findViewById<TextView>(R.id.transaction_description).text = transaction.description
        dialogView.findViewById<CheckBox>(R.id.transaction_isCredit).isChecked = transaction.isCredit
        dialogView.findViewById<TextView>(R.id.transaction_amount).text = if (transaction.transactionAmount != null) transaction.transactionAmount.toString() else ""
        dialogView.findViewById<CheckBox>(R.id.transaction_cleared).isChecked = transaction.cleared
    }
}