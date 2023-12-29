package com.example.shoppinglist

import kotlinx.coroutines.flow.Flow

class ShopRepository(private val shopDao: ShopDao) {

    val allShops: Flow<List<Shop>> = shopDao.getShops()

    suspend fun insert(shop: Shop) = shopDao.insertShop(shop)

    suspend fun update(shop: Shop) = shopDao.updateShop(shop)

    suspend fun delete(shop: Shop) = shopDao.deleteShop(shop)

}