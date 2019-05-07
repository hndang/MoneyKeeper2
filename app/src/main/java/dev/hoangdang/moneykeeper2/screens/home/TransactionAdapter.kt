package dev.hoangdang.moneykeeper2.screens.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.hoangdang.moneykeeper2.*
import dev.hoangdang.moneykeeper2.database.MoneyTransaction
import kotlinx.android.synthetic.main.transaction_item.view.*

class TransactionAdapter:ListAdapter<MoneyTransaction, TransactionAdapter.TransactionHolder>(TransactionDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        return TransactionHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        Log.d("MyRecyclerView", "onBindviewholder $position")
        val transaction = getItem(position)
        holder.bindTransaction(transaction)
    }

    class TransactionHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bindTransaction(transaction: MoneyTransaction){
            view.setOnClickListener{
                val navigationId = transaction.transactionId
                Log.d("MyRecyclerView", "Click on view $navigationId")
                Navigation.findNavController(it).navigate(HomeFragmentDirections.actionHomeFragmentToTransactionFragment(navigationId))
            }

            view.amount_textViewRV.text = String.format(view.resources.getString(R.string.money_format),transaction.transactionAmt)
            //view.date_textViewRV.text = getDateString(transaction.transactionDate, "dd-MMM-yyyy")
            view.date_textViewRV.text = convertDatePattern(transaction.transactionDate.toString(), datePatternDB, datePatternView)
            view.category_textViewRV.text = CategoryUtil.getCategoryName(transaction.transactionCategory)
            view.category_imageViewRV.setImageResource(CategoryUtil.getCategoryDrawable(transaction.transactionCategory))
            Log.d("MyRecyclerView", "Bind transaction")
        }

        companion object {
            fun from(parent: ViewGroup): TransactionHolder {
                val inflatedView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.transaction_item, parent, false) // inflate and pass to to viewholder to bind data
                return TransactionHolder(inflatedView)
            }
        }

    }
}

class TransactionDiffCallBack : DiffUtil.ItemCallback<MoneyTransaction>(){
    override fun areItemsTheSame(oldItem: MoneyTransaction, newItem: MoneyTransaction): Boolean {
        return oldItem.transactionId == newItem.transactionId
    }

    override fun areContentsTheSame(oldItem: MoneyTransaction, newItem: MoneyTransaction): Boolean {
        return oldItem == newItem
    }

}