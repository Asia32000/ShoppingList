package com.example.shoppinglist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(app: Application) : AndroidViewModel(app) {

    private val productRepo: ProductRepository
    val products: Flow<List<Product>>

    init{
        val productDao = ProductDatabase.getDatabase(app).productDao()
        productRepo = ProductRepository(productDao)
        products = productRepo.allProducts
    }

    fun insertProduct(product: Product){
        viewModelScope.launch {
            productRepo.insert(product)
        }
    }

    fun updateProduct(product: Product){
        viewModelScope.launch {
            productRepo.update(product)
        }
    }

    fun deleteProduct(product: Product){
        viewModelScope.launch {
            productRepo.delete(product)
        }
    }
}