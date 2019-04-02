package dev.hoangdang.moneykeeper2.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.hoangdang.moneykeeper2.database.TransactionDatabaseDAO
import java.lang.IllegalArgumentException

class HomeViewModelFactory(
    private val dataSource:TransactionDatabaseDAO,
    private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}