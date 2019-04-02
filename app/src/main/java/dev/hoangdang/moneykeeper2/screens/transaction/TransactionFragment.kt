package dev.hoangdang.moneykeeper2.screens.transaction

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import dev.hoangdang.moneykeeper2.CategoryUtil
import dev.hoangdang.moneykeeper2.R
import dev.hoangdang.moneykeeper2.convertDatePattern
import dev.hoangdang.moneykeeper2.database.TransactionDatabase
import dev.hoangdang.moneykeeper2.databinding.TransactionFragmentBinding

class TransactionFragment: Fragment(){

    private lateinit var binding: TransactionFragmentBinding
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myArg = TransactionFragmentArgs.fromBundle(arguments!!)
        binding = DataBindingUtil.inflate(inflater, R.layout.transaction_fragment,container,false)

        // Getting DAO
        val application = requireNotNull(this.activity).application
        val dataSource = TransactionDatabase.getInstance(application).transactionDatabaseDAO

        // Getting the View Model
        val viewModelFactory = TransactionViewModelFactory(dataSource, application)
        transactionViewModel = ViewModelProviders.of(this,viewModelFactory).get(TransactionViewModel::class.java)

        // if get ID then observe LiveData, for edit ?? Do we need livedata ?
        if(myArg.transactionId < 0){
            transactionViewModel.initalizeNewTransaction()
        }else{        // else just it's new transaction
            transactionViewModel.initializeTransactionFromDb(myArg.transactionId)
        }

        // Checking logic to navigate back
        transactionViewModel.navigateToHome.observe(this, Observer {
            if(it == true){
                Navigation.findNavController(binding.root).navigate(R.id.action_transactionFragment_to_homeFragment)
                transactionViewModel.doneNavigating()
                binding.root.hideKeyboard()
            }
        })

        // Set up Save button
        binding.saveButton.setOnClickListener{
            val amtRaw = binding.transactionAmtEditText.text.toString()
            val category = binding.categorySpinner.selectedItemPosition
            val note = binding.noteEditText.text.toString()
            val dateRaw = convertDatePattern(binding.dateEditText.text.toString(),"dd-MMM-yyyy","yyyyMMdd")
            val timeRaw = convertDatePattern(binding.timeEditText.text.toString(),"HH:mm:ss","HHmmss")

            transactionViewModel.createNewTransaction(amtRaw.toDouble(),category,note,dateRaw.toLong(),timeRaw.toLong())
        }

        // Setup Spinner
        binding.categorySpinner.adapter = ArrayAdapter(this.context!!,android.R.layout.simple_list_item_1,CategoryUtil.getNameList())
        // Checking logic to navigate back
        transactionViewModel.category.observe(this, Observer {
            if(it >= 0){
                binding.categorySpinner.setSelection(it)
            }
        })
        //Binding ViewModel LiveData to XML
        binding.transactionViewModel = transactionViewModel
        binding.lifecycleOwner = this
        binding.transactionAmtEditText.showKeyboard()

        return binding.root
    }

    fun View.hideKeyboard() { // TEMP TO GET ALTERNATE
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun View.showKeyboard() {// TEMP TO GET ALTERNATE
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

}