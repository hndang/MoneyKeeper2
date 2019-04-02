package dev.hoangdang.moneykeeper2.screens.transaction

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dev.hoangdang.moneykeeper2.convertDatePattern
import dev.hoangdang.moneykeeper2.database.MoneyTransaction
import dev.hoangdang.moneykeeper2.database.TransactionDatabaseDAO
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionViewModel(val database : TransactionDatabaseDAO, application: Application) : AndroidViewModel(application){

    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    // Database variables
    private var viewModelJob = Job() // Parent jobs for HomeViewModel coroutine
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val newTransaction = MoneyTransaction()
    private val editTransaction = MutableLiveData<MoneyTransaction>()

    private val transactionAmt = MutableLiveData<Double>()
    private val transactionCategory = MutableLiveData<Int>()
    private val transactionNote = MutableLiveData<String>()
    private val transactionDate = MutableLiveData<Long>()
    private val transactionTime = MutableLiveData<Long>()

    // Setting up the transformation Value for UI ****
    val amount : LiveData<String> = Transformations.map(transactionAmt){
        it.toString()
    }
    val category = Transformations.map(transactionCategory){
        it
    }
    val note = Transformations.map(transactionNote){
        it
    }
    val date = Transformations.map(transactionDate){
        //getDateString(it,"dd-MMM-yyyy")
        convertDatePattern(it.toString(), "yyyyMMdd","dd-MMM-yyyy")
    }
    val time = Transformations.map(transactionTime){
        //getDateString(it,"HH:mm:ss")
        convertDatePattern(it.toString(), "hhmmss","hh:mm:ss")
    }

    //init{
    //    editTransaction.value = MoneyTransaction()
    //}

    fun initalizeNewTransaction(){
        transactionDate.value = SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().time).toLong()
        transactionTime.value = SimpleDateFormat("HHmmss").format(Calendar.getInstance().time).toLong()
    }

    fun initializeTransactionFromDb(id : Long) {
        // Launch coroutine to get Tonight from the Database
        uiScope.launch {
            // Using UI coroutine (as per defined)
            editTransaction.value = getTransactionFromDatabase(id)
            transactionAmt.value = editTransaction.value?.transactionAmt
            transactionCategory.value = editTransaction.value?.transactionCategory
            transactionNote.value = editTransaction.value?.transactionNote
            transactionDate.value = editTransaction.value?.transactionDate
            transactionTime.value = editTransaction.value?.transactionTime
        }
    }

    private suspend fun getTransactionFromDatabase(id : Long): MoneyTransaction {
        return withContext(Dispatchers.IO) {
            // THis is DEFAULT Dispatcher for background Coroutine(oppose to UI coroutines)
            val night = database.get(id)
            night // return
        }
    }

    fun populateNewTransaction(){
        transactionDate.value = SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().time).toLong()
        transactionTime.value = SimpleDateFormat("HHmmss").format(Calendar.getInstance().time).toLong()
    }

    fun doneNavigating(){
        _navigateToHome.value = null
    }

    fun Cancel(){
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
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.insert(newTransaction)
            }
        }
        _navigateToHome.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}