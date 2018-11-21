package com.bitbybitlabs.pkarels.finance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import com.bitbybitlabs.pkarels.finance.data.TransactionsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

fun TransactionEntity.amountString(): String {
    return if (transactionAmount.equals(Double.NaN)) "" else transactionAmount.toString()
}

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TransactionsRepository(application)

    private var previousBalance = Double.NaN

    private val transactions = MutableLiveData<List<TransactionEntity>>()
    fun transactions(): LiveData<List<TransactionEntity>> = transactions

    private lateinit var fullTransactionsList: List<TransactionEntity>

    fun getTransactions() {
        repository.getTransactions()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t -> t.printStackTrace() }
                .doOnNext { onLoadFromDb(it) }
                .subscribe()
    }

    fun addOrUpdateTransaction(transaction: TransactionEntity) {
        previousBalance = transaction.previousBalance
        Single.fromCallable {
            repository.saveOrUpdateTransaction(transaction)
            updateBalances(transaction)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, Timber::e)
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        previousBalance = transaction.previousBalance
        Single.fromCallable {
            repository.deleteTransaction(transaction)
            updateBalances(transaction)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, Timber::e)
    }

    fun searchTransactions(query: String) {
        val currentTransactions = transactions.value as List<TransactionEntity>
        val filteredTransactions = currentTransactions.filter { item -> item.description.contains(query) }

        transactions.value = filteredTransactions
    }

    fun cancelSearch() {
        transactions.value = fullTransactionsList
    }

    private fun onLoadFromDb(transactionsList: List<TransactionEntity>) {
        fullTransactionsList = transactionsList
        transactions.value = transactionsList
    }

    private fun onError() {

    }

    private fun updateBalances(currentTransaction: TransactionEntity) {
        val transactionsList = fullTransactionsList as MutableList<TransactionEntity>
        val stopPosition = transactionsList.indexOf(transactionsList.find { element -> element.id == currentTransaction.id })
        transactionsList[stopPosition] = currentTransaction
        var previousResultingBalance = previousBalance
        for (position in stopPosition downTo (0)) {
            val transaction = transactionsList[position]
            transaction.previousBalance = previousResultingBalance
            transaction.resultingBalance =
                    if (transaction.isCredit) {
                        previousResultingBalance + transaction.transactionAmount
                    } else {
                        previousResultingBalance - transaction.transactionAmount
                    }
            transactionsList[position] = transaction
            previousResultingBalance = transaction.resultingBalance
            repository.saveOrUpdateTransaction(transaction)
        }
    }
}
