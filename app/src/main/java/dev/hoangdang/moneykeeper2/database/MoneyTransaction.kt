package dev.hoangdang.moneykeeper2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "money_transaction_table")
data class MoneyTransaction(
    @PrimaryKey(autoGenerate = true)
    var transactionId: Long = 0L,

    @ColumnInfo(name = "transaction_date")
    var transactionDate: Long = -1,

    @ColumnInfo(name = "transaction_amt")
    var transactionAmt: Double = -1.1,

    @ColumnInfo(name = "transaction_category")
    var transactionCategory: Int = 0,

    @ColumnInfo(name = "transaction_time")
    var transactionTime: Long = -1,

    @ColumnInfo(name = "transaction_note")
    var transactionNote: String = ""
)