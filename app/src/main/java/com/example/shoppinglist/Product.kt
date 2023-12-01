package com.example.shoppinglist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey
    val id: Int = (0..1000000).random(),
    var name: String,
    var status: Boolean,
    var amount: String,
    var cost: String?
)
