package dev.hoangdang.moneykeeper2.screens.transaction

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import dev.hoangdang.moneykeeper2.CategoryUtil
import dev.hoangdang.moneykeeper2.R
import kotlinx.android.synthetic.main.transaction_item.view.*

//TODO Place holder, todo in the future for custom spinner
class TransactionCategorySpinnerAdapter(context: Context, resource: Int, textViewResourceId: Int):
    ArrayAdapter<CategoryUtil.Category>(context, resource, textViewResourceId) {

// NEED ViewHolder?s

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getDropDownView(position, convertView, parent)
    }


}