package com.bitbybitlabs.pkarels.finance.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(tableName = "transaction")
data class TransactionEntity(
        @ColumnInfo(name = "transaction_date")
        var transactionDate: LocalDateTime = LocalDateTime.now(),

        @ColumnInfo(name = "transaction_type", typeAffinity = ColumnInfo.TEXT)
        var transactionType: String = "",

        @ColumnInfo(name = "isCredit", typeAffinity = ColumnInfo.INTEGER)
        var isCredit: Boolean = false,

        @ColumnInfo(name = "transaction_amount", typeAffinity = ColumnInfo.REAL)
        var transactionAmount: Double = Double.NaN,

        @ColumnInfo(name = "transaction_description", typeAffinity = ColumnInfo.TEXT)
        var description: String = "",

        @ColumnInfo(name = "transaction_cleared", typeAffinity = ColumnInfo.INTEGER)
        var cleared: Boolean = false,

        @ColumnInfo(name = "resulting_balance", typeAffinity = ColumnInfo.REAL)
        var resultingBalance: Double = 0.0,

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int = 0
)
