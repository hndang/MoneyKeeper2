package dev.hoangdang.moneykeeper2.screens.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.hoangdang.moneykeeper2.database.MoneyTransaction
import dev.hoangdang.moneykeeper2.databinding.TransactionItemBinding

class TransactionAdapter(val clickListener: TransactionListener):ListAdapter<MoneyTransaction, TransactionAdapter.TransactionHolder>(TransactionDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        return TransactionHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        Log.d("MyRecyclerView", "onBindViewHolder $position")
        val transaction = getItem(position)
        holder.bindTransaction(transaction, clickListener)
    }

    class TransactionHolder private constructor(val binding: TransactionItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bindTransaction(
            transaction: MoneyTransaction,
            clickListener: TransactionListener
        ){
            //view.setOnClickListener{
            //    val navigationId = transaction.transactionId
            //    Log.d("MyRecyclerView", "Click on view $navigationId")
            //    Navigation.findNavController(it).navigate(HomeFragmentDirections.actionHomeFragmentToTransactionFragment(navigationId))
            //}

            //binding.amountTextViewRV.text = String.format(binding.root.resources.getString(R.string.money_format),transaction.transactionAmt)
            //binding.dateTextViewRV.text = convertDatePattern(transaction.transactionDate.toString(), DATE_PATTERN_DB, DATE_PATTERN_VIEW)
            //binding.categoryTextViewRV.text = CategoryUtil.getCategoryName(transaction.transactionCategory)
            //binding.categoryImageViewRV.setImageResource(CategoryUtil.getCategoryDrawable(transaction.transactionCategory))
            binding.transaction = transaction
            binding.executePendingBindings()
            binding.clickListener = clickListener
            Log.d("MyRecyclerView", "Bind transaction")
        }

        companion object {
            fun from(parent: ViewGroup): TransactionHolder {
                //val inflatedView = LayoutInflater.from(parent.context)
                //    .inflate(R.layout.transaction_item, parent, false) // inflate and pass to to viewholder to bind data
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TransactionItemBinding.inflate(layoutInflater, parent, false)
                return TransactionHolder(binding)
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

class TransactionListener(val clickListener: (transitionId: Long)->Unit){
    fun onClick(transaction: MoneyTransaction) = clickListener(transaction.transactionId)
}