package com.bitbybitlabs.pkarels.finance.data

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.INTEGER
import androidx.room.ColumnInfo.REAL
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(tableName = "transaction")
data class TransactionEntity(
        @PrimaryKey
        @ColumnInfo(name = "transaction_date")
        val transactionDate: LocalDateTime,

        @ColumnInfo(name = "transaction_type")
        val transactionType: String,

        @ColumnInfo(name = "isDebit", typeAffinity = INTEGER)
        val isDebit: Boolean = true,

        @ColumnInfo(name = "transaction_amount", typeAffinity = REAL)
        val transactionAmount: Double,

        @ColumnInfo(name = "transaction_description")
        val description: String,

        @ColumnInfo(name = "transaction_cleared", typeAffinity = INTEGER)
        val cleared: Boolean,

        @ColumnInfo(name = "resulting_balance", typeAffinity = REAL)
        val resultingBalance: Double
)