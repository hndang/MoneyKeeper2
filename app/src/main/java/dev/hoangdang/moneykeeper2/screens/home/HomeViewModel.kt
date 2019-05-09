package dev.hoangdang.moneykeeper2.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dev.hoangdang.moneykeeper2.database.TransactionDatabaseDAO
import dev.hoangdang.moneykeeper2.DATE_PATTERN_DB
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(val database : TransactionDatabaseDAO, application: Application): AndroidViewModel(application){

    // Database variables
    private var viewModelJob = Job() // Parent jobs for HomeViewModel coroutine
    val transactionsToday = database.getTransactionByDate(SimpleDateFormat(DATE_PATTERN_DB).format(Calendar.getInstance().time).toInt())
    val testLatestMoney = Transformations.map(transactionsToday){
        if(it.size >0)
            it.get(0).transactionAmt.toString()
        else
            "0"
    }

    val todaySpending = Transformations.map(transactionsToday){
        var sum:Double = 0.0
        for(item in it){
            sum += item.transactionAmt
        }
        sum
    }

    // LiveData Variables
    private val _moneyTotal = MutableLiveData<Float>()
    val moneyTotal : LiveData<Float>
        get() = _moneyTotal

    private val _test = MutableLiveData<String>()
    val test : LiveData<String>
        get() = _test

    private val _navigateToTransaction = MutableLiveData<Long>()
    val navigateToTransaction: LiveData<Long>
        get() = _navigateToTransaction

    init{
        _moneyTotal.value = 99999f
    }

    fun setMoney(amt:Float){
        _moneyTotal.value = amt
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // cancel all coroutine by this viewModel
    }

    fun onTransactionClicked(id: Long){
        _navigateToTransaction.value = id
    }

    fun onTransactionNavigated(){
        _navigateToTransaction.value = null //reset after navigated
    }
}