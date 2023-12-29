package com.example.shoppinglist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Shop::class], version = 1)
abstract class ShopDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDao

    companion object{
        private var instance: ShopDatabase? = null

        fun getDatabase(context: Context): ShopDatabase{
            if(instance != null)
                return instance as ShopDatabase
            instance = Room.databaseBuilder(
                context,
                ShopDatabase::class.java,
                "Baza sklepow"
            ).build()
            return instance as ShopDatabase
        }

    }
}