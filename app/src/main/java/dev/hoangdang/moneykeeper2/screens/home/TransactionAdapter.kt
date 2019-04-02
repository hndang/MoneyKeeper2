package dev.hoangdang.moneykeeper2.screens.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import dev.hoangdang.moneykeeper2.CategoryUtil
import dev.hoangdang.moneykeeper2.R
import dev.hoangdang.moneykeeper2.database.MoneyTransaction
import dev.hoangdang.moneykeeper2.convertDatePattern
import kotlinx.android.synthetic.main.transaction_item.view.*

class TransactionAdapter(private val transactions : List<MoneyTransaction>):RecyclerView.Adapter<TransactionAdapter.TransactionHolder>(){
    class TransactionHolder(private val view: View, transactions : List<MoneyTransaction>): RecyclerView.ViewHolder(view) {

        init{
            view.setOnClickListener{
                val navigationId = transactions[adapterPosition].transactionId
                Log.d("MyRecyclerView", "Click on view $navigationId")
                Navigation.findNavController(it).navigate(HomeFragmentDirections.actionHomeFragmentToTransactionFragment(navigationId))
            }
        }

        fun bindTransaction(transaction: MoneyTransaction){
            // Put ViewModel in here ?? vs observer from outside
            view.amount_textViewRV.text = String.format(view.resources.getString(R.string.money_format),transaction.transactionAmt)
            //view.date_textViewRV.text = getDateString(transaction.transactionDate, "dd-MMM-yyyy")
            view.date_textViewRV.text = convertDatePattern(transaction.transactionDate.toString(), "yyyyMMdd","dd-MMM-yyyy")
            view.category_textViewRV.text = CategoryUtil.getCategoryName(transaction.transactionCategory)
            view.category_imageViewRV.setImageResource(CategoryUtil.getCategoryDrawable(transaction.transactionCategory))
            Log.d("MyRecyclerView", "Bind transaction")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val inflatedView =  LayoutInflater.from(parent.context).inflate(R.layout.transaction_item,parent,false) // inflate and pass to to viewholder to bind data
        return TransactionHolder(inflatedView, transactions)
    }

    override fun getItemCount() = transactions.size

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        // Put ViewModel in here ??
        Log.d("MyRecyclerView", "onBindviewholder $position")
        val transaction = transactions[position]
        holder.bindTransaction(transaction)
    }


}