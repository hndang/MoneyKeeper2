package dev.hoangdang.moneykeeper2.screens.transaction

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dev.hoangdang.moneykeeper2.*
import dev.hoangdang.moneykeeper2.database.MoneyTransaction
import dev.hoangdang.moneykeeper2.database.TransactionDatabaseDAO
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionViewModel(val database : TransactionDatabaseDAO, application: Application) : AndroidViewModel(application){


    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    private val _isNegative = MutableLiveData<Boolean?>()
    val isNegative: LiveData<Boolean?>
        get() = _isNegative

    // Database variables
    private var viewModelJob = Job() // Parent jobs for HomeViewModel coroutine
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val newTransaction = MoneyTransaction()
    private var _currentTransaction = MutableLiveData<MoneyTransaction?>()
    val currentTransaction:LiveData<MoneyTransaction?>
        get() = _currentTransaction

    // Setting up the transformation Value for UI ****
    val amount : LiveData<String> = Transformations.map(_currentTransaction){
        when {
            it?.transactionAmt == 0.0 -> ""
            it?.transactionAmt!! < 0.0 -> {
                _isNegative.value = true
                (-it.transactionAmt).toString()
            }
            else -> {
                _isNegative.value = false
                it.transactionAmt.toString()
            }
        }

    }
    val category : LiveData<Int> = Transformations.map(_currentTransaction){
        it?.transactionCategory
    }
    val note : LiveData<String> = Transformations.map(_currentTransaction){
        it?.transactionNote
    }
    val date : LiveData<String> = Transformations.map(_currentTransaction){
        convertDatePattern(it?.transactionDate.toString(), DATE_PATTERN_DB, DATE_PATTERN_VIEW)
    }
    val time : LiveData<String> = Transformations.map(_currentTransaction){
        convertDatePattern(it?.transactionTime.toString().padStart(6,'0'), TIME_PATTERN_DB, TIME_PATTERN_VIEW)
    }


    fun initalizeNewTransaction(){
        uiScope.launch {
            // Using UI coroutine (as per defined)
            _currentTransaction.value = getTransactionFromDatabase(-1)
        }
    }

    fun initializeTransaction(id: Long = -1 ) {
        // Launch coroutine to get Tonight from the Database
        uiScope.launch {
            // Using UI coroutine (as per defined)
            _currentTransaction.value = getTransactionFromDatabase(id)
        }
    }

    private suspend fun getTransactionFromDatabase(id : Long = -1): MoneyTransaction? {
        return withContext(Dispatchers.IO) {
            // THis is DEFAULT Dispatcher for background Coroutine(oppose to UI coroutines)
            var night: MoneyTransaction? = null
            if(id>=0){
                night = database.get(id)
            }
            else{
                night = newTransaction
                night.transactionAmt = 0.0
                //val current = LocalDateTime.now()
                night.transactionTime = SimpleDateFormat(TIME_PATTERN_DB).format(Calendar.getInstance().time).toLong()
                night.transactionDate = SimpleDateFormat(DATE_PATTERN_DB).format(Calendar.getInstance().time).toLong()

            }
            night // return
        }
    }

    fun doneNavigating(){
        _navigateToHome.value = null
    }

    fun cancel(){
        _navigateToHome.value = true
    }

    fun deleteTransaction(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.delete(_currentTransaction.value!!)
            }
        }
        _navigateToHome.value = true
    }

    fun createNewTransaction(transactionAmt:Double, transactionCategory:Int, transactionNote:String, transactionDate:Long, transactionTime:Long){
        newTransaction.let{
            Log.v("Transaction","trans=$transactionAmt cate=$transactionCategory note=$transactionNote date=$transactionDate time=$transactionTime")
            it.transactionAmt = transactionAmt ?: 0.0 //Default is 0
            it.transactionCategory = transactionCategory ?: 0 //Default is 0
            it.transactionNote = transactionNote ?: "" //Default is 0
            it.transactionDate = transactionDate ?: 0
            it.transactionTime = transactionTime ?: 0
        }
        //if(transactionAmt)
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.insert(newTransaction)
            }
        }
        _navigateToHome.value = true
    }

    fun updateNewTransaction(transactionAmt:Double, transactionCategory:Int, transactionNote:String, transactionDate:Long, transactionTime:Long){
        _currentTransaction.value.let{
            Log.v("Transaction","trans=$transactionAmt cate=$transactionCategory note=$transactionNote date=$transactionDate time=$transactionTime")
            it?.transactionAmt = transactionAmt ?: 0.0 //Default is 0
            it?.transactionCategory = transactionCategory ?: 0 //Default is 0
            it?.transactionNote = transactionNote ?: "" //Default is 0
            it?.transactionDate = transactionDate ?: 0
            it?.transactionTime = transactionTime ?: 0
        }

        uiScope.launch {
            withContext(Dispatchers.IO){
                database.update(_currentTransaction.value!!)
            }
        }
        _navigateToHome.value = true
    }

    fun updateCurrentTransaction(amt:Double = 0.0, note:String = "", category:Int = 0, date:Long = -1, time:Long = -1){
        val temp = _currentTransaction.value
        if(amt != 0.0){
            temp?.transactionAmt = amt
        }
        if(note != ""){
            temp?.transactionNote = note
        }
        if(category != 0){
            temp?.transactionCategory = category
        }
        if(date != (-1).toLong()){
            temp?.transactionDate = date
        }
        if(time != (-1).toLong()){
            temp?.transactionTime = time
        }
        _currentTransaction.value = temp
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}