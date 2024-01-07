package com.example.shoppinglist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class ShopViewModel(val app: Application) : AndroidViewModel(app) {

    private val shopRepo: ShopRepository
    val shops: Flow<List<Shop>>
    init{
        val shopDao = ShopDatabase.getDatabase(app).shopDao()
        shopRepo = ShopRepository(shopDao)
        shops = shopRepo.allShops
    }

    fun insertShop(shop: Shop){
        viewModelScope.launch {
            shopRepo.insert(shop)
        }
    }

    fun updateShop(shop: Shop){
        viewModelScope.launch {
            shopRepo.update(shop)
        }
    }

    fun deleteShop(shop: Shop){
        viewModelScope.launch {
            shopRepo.delete(shop)
        }
    }
}