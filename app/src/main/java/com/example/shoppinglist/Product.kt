package com.example.shoppinglist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var status: Boolean,
    var amount: String,
    var cost: String?
)
