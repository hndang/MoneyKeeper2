package dev.hoangdang.moneykeeper2.screens.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import dev.hoangdang.moneykeeper2.R
import dev.hoangdang.moneykeeper2.database.TransactionDatabase
import dev.hoangdang.moneykeeper2.databinding.HomeFragmentBinding
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(){

    private lateinit var binding: HomeFragmentBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: TransactionAdapter
    //private lateinit var layout: ConstraintLayout



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Inflate view with binding class
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment,container,false)

        // Getting DAO
        val application = requireNotNull(this.activity).application
        val dataSource = TransactionDatabase.getInstance(application).transactionDatabaseDAO

        // Getting the View Model
        val homeViewModelFactory = HomeViewModelFactory(dataSource,application)
        homeViewModel = ViewModelProviders.of(this,homeViewModelFactory).get(HomeViewModel::class.java)

        //Binding ViewModel LiveData to XML
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        //Set the Add transaction button
        binding.addFloatingActionButton.setOnClickListener {
            Navigation.findNavController(it).navigate(HomeFragmentDirections.actionHomeFragmentToTransactionFragment(-1))
        }

        //Set up the recycler view
        val linearLayoutManager = LinearLayoutManager(this.context)
        binding.transactionsRecyclerView.layoutManager = linearLayoutManager

        homeViewModel.transactionsToday.observe(this, Observer {
            if(binding.transactionsRecyclerView.adapter == null){
                adapter = TransactionAdapter(it)
                binding.transactionsRecyclerView.adapter = adapter
            }else{
                binding.transactionsRecyclerView.adapter!!.notifyDataSetChanged() // Efficient ?
            }

        })

        return binding.root
    }
}