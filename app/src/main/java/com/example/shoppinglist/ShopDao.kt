package com.example.shoppinglist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface ShopDao {

    @Query("SELECT * FROM shop")
    fun getShops(): Flow<List<Shop>>

    @Insert
    suspend fun insertShop(shop: Shop)

    @Update
    suspend fun updateShop(shop: Shop)

    @Delete
    suspend fun deleteShop(shop: Shop)
}