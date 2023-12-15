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

class Product2ViewModel(val app: Application, val products2: SnapshotStateList<Product2>) : AndroidViewModel(app) {

    private val database = Firebase.database(url = "https://shoppinglist-7fc7a-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("products")
    init{
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(snapshotError: DatabaseError) {
                Toast.makeText(
                    app.applicationContext,
                    "Getting data wasn't successful",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                products2.clear()
                val children = snapshot!!.children
                children.forEach {
                    it.getValue(Product2::class.java)?.let {
                            it1 -> products2.add(it1)
                    }
                }
            }
        })

        childEventListener()
    }

    private fun childEventListener() {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildAdded:" + dataSnapshot.key!!)

                // A new product has been added, add it to the displayed list
                val product = dataSnapshot.getValue<Product2>()
                if (product != null) {
                    if (!products2.contains(product)) {
                        products2.add(product)
                    }
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildChanged: ${dataSnapshot.key}")

                val newProduct = dataSnapshot.getValue<Product2>()
                val productKey = dataSnapshot.key

                if (newProduct != null) {
                    val oldProduct = products2.first { it.id.toString() == productKey }
                    products2.add(newProduct)
                    products2.remove(oldProduct)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val deletedProduct = dataSnapshot.getValue<Product2>()
                println("deleted: $deletedProduct")
//                if (deletedProduct != null) {
//                    val oldProduct = products2.first { it.id == deletedProduct.id }
//                    if (oldProduct != null) {
//                        products2.remove(oldProduct)
//                    }
//                }
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedProduct = dataSnapshot.getValue<Product2>()
                val productKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(
                    app.applicationContext,
                    "Failed to load comments.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
        myRef.addChildEventListener(childEventListener)
    }
}