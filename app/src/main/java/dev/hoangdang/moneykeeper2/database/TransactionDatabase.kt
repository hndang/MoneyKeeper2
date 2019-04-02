package dev.hoangdang.moneykeeper2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MoneyTransaction::class], version = 1, exportSchema = false)
abstract class TransactionDatabase : RoomDatabase(){
    abstract val transactionDatabaseDAO : TransactionDatabaseDAO //associate the DAO

    companion object {
        @Volatile
        private var INSTANCE:TransactionDatabase? = null

        fun getInstance(context: Context) : TransactionDatabase{
            synchronized(this){
                var instance = INSTANCE // for smart cast
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TransactionDatabase::class.java,
                        "money_transaction_table")
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}