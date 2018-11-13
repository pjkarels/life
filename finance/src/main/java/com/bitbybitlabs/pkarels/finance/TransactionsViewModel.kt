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
import timber.log.Timber.e

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TransactionsRepository(application)

    private val transactions = MutableLiveData<List<TransactionEntity>>()
    fun transactions(): LiveData<List<TransactionEntity>> = transactions

    fun getTransactions() {
        repository.getTransactions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { transactions.value = it },
                        Timber::e
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
}
