package dev.hoangdang.moneykeeper2.screens.transaction

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.NavigatorProvider
import com.google.android.material.snackbar.Snackbar
import dev.hoangdang.moneykeeper2.*
import dev.hoangdang.moneykeeper2.database.MoneyTransaction
import dev.hoangdang.moneykeeper2.database.TransactionDatabase
import dev.hoangdang.moneykeeper2.databinding.TransactionFragmentBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionFragment : Fragment() {

    private lateinit var binding: TransactionFragmentBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private val AMT_KEY = "amt"
    private val NOTE_KEY = "note"
    private val CATEGORY_KEY = "category"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myArg = TransactionFragmentArgs.fromBundle(arguments!!)
        var isEditing = false
        binding = DataBindingUtil.inflate(inflater, R.layout.transaction_fragment, container, false)

        // Getting DAO
        val application = requireNotNull(this.activity).application
        val dataSource = TransactionDatabase.getInstance(application).transactionDatabaseDAO

        // Getting the View Model
        val viewModelFactory = TransactionViewModelFactory(dataSource, application)
        transactionViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionViewModel::class.java)

        // if get ID then observe LiveData, for edit ?? Do we need livedata ?
        if (myArg.transactionId > 0) {
            isEditing = true
            binding.deleteButton.visibility = View.VISIBLE

        }
        transactionViewModel.initializeTransaction(myArg.transactionId)

        // Checking logic to navigate back
        transactionViewModel.navigateToHome.observe(this, Observer {
            if (it == true) {
                Navigation.findNavController(binding.root).navigate(R.id.action_transactionFragment_to_homeFragment)
                transactionViewModel.doneNavigating()
                binding.root.hideKeyboard()
            }
        })

        // check Logic for switch
        transactionViewModel.isNegative.observe(this, Observer {
            binding.signSwitch.isChecked = it == true
        })

        // Set up Save button
        binding.saveButton.setOnClickListener {

            if (binding.transactionAmtEditText.text.toString() == "") {
                Snackbar.make(it, "No money Amount entered !!", Snackbar.LENGTH_SHORT).show()
            } else {
                var amtRaw = binding.transactionAmtEditText.text.toString().toDouble()
                if (binding.signSwitch.isChecked && amtRaw > 0) {
                    amtRaw *= -1
                }
                val category = binding.categorySpinner.selectedItemPosition
                val note = binding.noteEditText.text.toString()
                val dateRaw =
                    convertDatePattern(binding.dateEditText.text.toString(), datePatternView, datePatternDB).toLong()
                val timeRaw =
                    convertDatePattern(binding.timeEditText.text.toString(), timePatternView, timePatternDB).toLong()

                if (isEditing) {
                    transactionViewModel.updateNewTransaction(
                        amtRaw,
                        category,
                        note,
                        dateRaw,
                        timeRaw
                    )
                } else {
                    transactionViewModel.createNewTransaction(
                        amtRaw,
                        category,
                        note,
                        dateRaw,
                        timeRaw
                    )
                }
            }
        }

        // Set up Delete button
        binding.deleteButton.setOnClickListener {
            transactionViewModel.deleteTransaction()
        }

        // Set Date button
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val c = Calendar.getInstance()
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sign =
                if (binding.signSwitch.isChecked) {
                    -1
                } else {
                    1
                }
            val date = SimpleDateFormat(datePatternDB).format(c.time).toLong()
            val amt:Double =
                if (binding.transactionAmtEditText.text.toString() != "") {
                    binding.transactionAmtEditText.text.toString().toDouble() * sign
                } else {
                    0.0
                }
            val note = binding.noteEditText.text.toString()
            val category = binding.categorySpinner.selectedItemPosition
            transactionViewModel.updateCurrentTransaction(amt = amt, note = note, category = category, date = date)
        }
        binding.setDateButton.setOnClickListener {
            //TEMP as Navigation doesn't work with DialogFragment yet
            val calendar = getCalendarFromPattern(binding.dateEditText.text.toString(), datePatternView)
            DatePickerDialog(
                context!!,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Set Time button
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val c = Calendar.getInstance()
            c.set(Calendar.HOUR_OF_DAY, hourOfDay)
            c.set(Calendar.MINUTE, minute)
            c.set(Calendar.SECOND, 0)
            val sign =
                if (binding.signSwitch.isChecked) {
                    -1
                } else {
                    1
                }
            val time = SimpleDateFormat(timePatternDB).format(c.time).toLong()
            val amt:Double =
                if (binding.transactionAmtEditText.text.toString() != "") {
                    binding.transactionAmtEditText.text.toString().toDouble() * sign
                } else {
                    0.0
                }
            val note = binding.noteEditText.text.toString()
            val category = binding.categorySpinner.selectedItemPosition
            transactionViewModel.updateCurrentTransaction(amt = amt, note = note, category = category, time = time)
        }

        binding.setTimeButton.setOnClickListener {
            val calendar = getCalendarFromPattern(binding.timeEditText.text.toString(), timePatternView)
            TimePickerDialog(
                context!!,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        // Setup Spinner
        binding.categorySpinner.adapter =
            ArrayAdapter(this.context!!, android.R.layout.simple_list_item_1, CategoryUtil.getNameList())
        // Checking logic to navigate back
        transactionViewModel.category.observe(this, Observer {
            if (it >= 0) {
                binding.categorySpinner.setSelection(it)
            }
        })
        //Binding ViewModel LiveData to XML
        binding.transactionViewModel = transactionViewModel
        binding.lifecycleOwner = this
        binding.transactionAmtEditText.showKeyboard()

        return binding.root
    }

    // TEMP ? Maybe we can do 2way binding to the edit text
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(AMT_KEY, binding.transactionAmtEditText.text.toString())
        outState.putString(NOTE_KEY, binding.noteEditText.text.toString())
        outState.putInt(CATEGORY_KEY, binding.categorySpinner.selectedItemPosition)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            binding.transactionAmtEditText.setText(savedInstanceState?.getString(AMT_KEY, ""))
            binding.noteEditText.setText(savedInstanceState?.getString(NOTE_KEY, ""))
            binding.categorySpinner.setSelection(savedInstanceState!!.getInt(CATEGORY_KEY, 0))
        }

    }


    fun View.hideKeyboard() { // TEMP TO GET ALTERNATE
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun View.showKeyboard() {// TEMP TO GET ALTERNATE
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

}