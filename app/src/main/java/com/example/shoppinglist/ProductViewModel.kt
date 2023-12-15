package com.example.shoppinglist

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(val app: Application) : AndroidViewModel(app) {

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