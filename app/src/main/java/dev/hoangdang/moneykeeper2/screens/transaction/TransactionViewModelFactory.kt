package dev.hoangdang.moneykeeper2.screens.transaction

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.hoangdang.moneykeeper2.database.TransactionDatabaseDAO

class TransactionViewModelFactory (
    private val dataSource: TransactionDatabaseDAO,
    private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TransactionViewModel::class.java)){
                return TransactionViewModel(dataSource,application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}
