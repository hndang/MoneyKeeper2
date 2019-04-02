package dev.hoangdang.moneykeeper2

class CategoryUtil{
    companion object {
        private val categories : List<Category> = mutableListOf(
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
            if (id in 1..7) {
                return categories[id].name
            } else {
                return categories[0].name
            }
        }
        fun getCategoryDrawable(id: Int): Int {
            if (id in 1..7) {
                return categories[id].resourceId
            } else {
                return categories[0].resourceId
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