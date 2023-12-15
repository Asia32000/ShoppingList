package com.example.shoppinglist

import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Product2(
    val id: Int? = (0..1000000).random(),
    var name: String? = "",
    var status: Boolean? = false,
    var amount: String? = "",
    var cost: String? = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "status" to status,
            "amount" to amount,
            "cost" to cost
        )
    }
}
