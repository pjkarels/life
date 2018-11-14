package com.bitbybitlabs.pkarels.finance.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(tableName = "transaction")
data class TransactionEntity(
        @ColumnInfo(name = "transaction_date")
        val transactionDate: LocalDateTime,

        @ColumnInfo(name = "transaction_type", typeAffinity = ColumnInfo.TEXT)
        val transactionType: String,

        @ColumnInfo(name = "isCredit", typeAffinity = ColumnInfo.INTEGER)
        val isCredit: Boolean = true,

        @ColumnInfo(name = "transaction_amount", typeAffinity = ColumnInfo.REAL)
        val transactionAmount: Double,

        @ColumnInfo(name = "transaction_description", typeAffinity = ColumnInfo.TEXT)
        val description: String,

        @ColumnInfo(name = "transaction_cleared", typeAffinity = ColumnInfo.INTEGER)
        val cleared: Boolean,

        @ColumnInfo(name = "resulting_balance", typeAffinity = ColumnInfo.REAL)
        val resultingBalance: Double,

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int = 0
)
