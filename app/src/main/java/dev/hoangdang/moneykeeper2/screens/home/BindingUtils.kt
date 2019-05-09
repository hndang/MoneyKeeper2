package dev.hoangdang.moneykeeper2.screens.home

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.hoangdang.moneykeeper2.*
import dev.hoangdang.moneykeeper2.database.MoneyTransaction

@BindingAdapter("transactionAmtFormatted")
fun TextView.setTransactionAmtFormatted(item: MoneyTransaction?){
    item?.let {// only do when not null
        text = String.format(context.resources.getString(R.string.money_format),item.transactionAmt)
    }
}

@BindingAdapter("transactionDateFormatted")
fun TextView.setTransactionDateFormatted(item: MoneyTransaction?){
    item?.let{
        text = convertDatePattern(item.transactionDate.toString(), DATE_PATTERN_DB, DATE_PATTERN_VIEW)
    }
}

@BindingAdapter("categoryName")
fun TextView.setCategoryName(item: MoneyTransaction?){
    item?.let {
        text = CategoryUtil.getCategoryName(item.transactionCategory)
    }
}

@BindingAdapter("categoryImage")
fun ImageView.setCategoryImage(item: MoneyTransaction?){
    item?.let{
        setImageResource(CategoryUtil.getCategoryDrawable(item.transactionCategory))
    }
}