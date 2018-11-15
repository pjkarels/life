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

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TransactionsRepository(application)

    private val previousBalance = MutableLiveData<Double>()
    fun previousBalance() = previousBalance as LiveData<Double>

    private val transactions = MutableLiveData<List<TransactionEntity>>()
    fun transactions(): LiveData<List<TransactionEntity>> = transactions

    fun getTransactions() {
        repository.getTransactions()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t -> t.printStackTrace() }
                .doOnNext { onLoadFromDb(it) }
                .subscribe(
                )
    }

    fun addOrUpdateTransaction(transaction: TransactionEntity) {
        Single.fromCallable {
            repository.saveOrUpdateTransaction(transaction)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, Timber::e)
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        Single.fromCallable {
            repository.deleteTransaction(transaction)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, Timber::e)
    }

    private fun getPreviousTransaction(currentId: Int) {
        repository.getPreviousTransaction(currentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t -> t.printStackTrace() }
                .doOnSuccess {
                    onLoadPreviousTransaction(it)
                }
                .subscribe()
    }

    private fun onLoadFromDb(transactionsList: List<TransactionEntity>) {
        transactions.value = transactionsList
    }

    private fun onLoadPreviousTransaction(transaction: TransactionEntity) {
        previousBalance.value = transaction.resultingBalance
    }

    private fun onError() {

    }
}
