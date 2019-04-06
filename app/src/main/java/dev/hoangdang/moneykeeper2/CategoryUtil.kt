package dev.hoangdang.moneykeeper2

class CategoryUtil{
    companion object {
        val categories : List<Category> = mutableListOf(
            Category("Unknown", R.drawable.ic_category_unknown),
            Category("Food", R.drawable.ic_category_food),
            Category("Grocery", R.drawable.ic_category_grocery),
            Category("Clothing", R.drawable.ic_category_clothing),
            Category("Rent", R.drawable.ic_category_rent),
            Category("Gift", R.drawable.ic_category_gift),
            Category("Entertainment", R.drawable.ic_category_entertainment),
            Category("Income", R.drawable.ic_category_income)
        )

        fun getCategoryName(id: Int): String {
            return if (id in 1..7) {
                categories[id].name
            } else {
                categories[0].name
            }
        }
        fun getCategoryDrawable(id: Int): Int {
            return if (id in 1..7) {
                categories[id].resourceId
            } else {
                categories[0].resourceId
            }
        }

        fun getNameList(): MutableList<String>{
            val nameList = mutableListOf<String>()
            for(category in categories){
                nameList.add(category.name)
            }
            return nameList
        }

    }
    data class Category(val name:String, val resourceId: Int)

}