package com.bitbybitlabs.pkarels.finance.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bitbybitlabs.pkarels.finance.data.TransactionEntity
import com.bitbybitlabs.pkarels.finance.data.TransactionsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TransactionsRepository(application)

    val transactionData = MutableLiveData<TransactionEntity>()

    init {
        transactionData.value = TransactionEntity()
    }

    fun fetchTransaction(id: Int) {
        repository.getTransaction(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    onSuccess(it)
                }
                .doOnError { t ->
                    t.printStackTrace()
                }
                .subscribe()
    }

    private fun onSuccess(transaction: TransactionEntity) {
        transactionData.value = transaction
    }
}