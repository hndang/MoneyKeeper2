package dev.hoangdang.moneykeeper2.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao

interface TransactionDatabaseDAO{
    @Insert
    fun insert(transaction : MoneyTransaction)

    @Update
    fun update(transaction: MoneyTransaction)

    @Delete
    fun delete(transaction: MoneyTransaction)

    @Query("DELETE FROM money_transaction_table")
    fun deleteAll()

    @Query("SELECT * FROM money_transaction_table where transaction_date == :date order by transaction_time DESC")
    fun getTransactionByDate(date: Int) : LiveData<List<MoneyTransaction>>

    @Query("SELECT * FROM money_transaction_table where transaction_date BETWEEN :startDate AND :endDate order by transaction_time DESC")
    fun getTransactionByDateRange(startDate: Int, endDate: Int ) : LiveData<List<MoneyTransaction>>

    @Query("SELECT * FROM money_transaction_table where transactionId == :key")
    fun get(key: Long): MoneyTransaction
}