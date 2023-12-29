package com.example.shoppinglist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Shop(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var description: String?,
    var radius: String,
    var latitude: String?,
    var longitude: String?
)
