package com.example.shoppinglist

import UserStore
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.PreferencesProto.PreferenceMapOrBuilder
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class AddProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                val viewModel = ProductViewModel(application)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AddProductScreen(viewModel, goToPreviousActivity = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(viewModel: ProductViewModel, goToPreviousActivity: () -> Unit) {
    var nameText by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    val context = LocalContext.current
    val store = UserStore(context)
    val savedColor = store.getColorName.collectAsState(initial = "")
    val savedFontSize = store.getFontSize.collectAsState(initial = "")

    val database = Firebase.database(url = "https://shoppinglist-7fc7a-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("products")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp, top = 30.dp),
            text = "Item",
            color = Color.Gray,
            fontSize = infoTextFontSize(savedFontSize.value).sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = nameText,
            onValueChange = { nameText = it },
            trailingIcon = {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .clickable {
                            nameText = ""
                        }
                )
            }
        )
        Text(
            modifier = Modifier
                .padding(16.dp, top = 30.dp),
            text = "Amount",
            color = Color.Gray,
            fontSize = infoTextFontSize(savedFontSize.value).sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=16.dp, end=16.dp),
            value = amount,
            onValueChange = { amount = it },
            trailingIcon = {
                Icon(Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .clickable {
                            amount = ""
                        }
                )
            })

        Text(
            modifier = Modifier
                .padding(16.dp, top = 30.dp),
            text = "Cost",
            color = Color.Gray,
            fontSize = infoTextFontSize(savedFontSize.value).sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=16.dp, end=16.dp),
            value = cost,
            onValueChange = { cost = it },
            trailingIcon = {
                Icon(Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .clickable {
                            cost = ""
                        }
                )
            })
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=128.dp, end=128.dp),
            onClick = {
                if (nameText.isNotEmpty() && amount.isNotEmpty()) {
                    val product = Product(name = nameText, amount = amount, status = false, cost = cost)
                    viewModel.insertProduct(product)
                    // send broadcast
                    Intent().also { intent ->
                        intent.action = "com.example.ShoppingList"
                        intent.putExtra("id", product.id)
                        intent.putExtra("name", product.name)
                        intent.putExtra("amount", product.amount)
                        intent.putExtra("cost", product.cost)
                        intent.putExtra("status", product.status)
                        context.sendBroadcast(intent)
                    }

                    val product2 = Product2(name = nameText, amount = amount, status = false, cost = cost)
                    myRef.child(product2.id.toString()).setValue(product2)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Data added to Firebase Database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Fail to add data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    goToPreviousActivity()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = (if (nameText.isNotEmpty() && amount.isNotEmpty()) buttonColor(savedColor.value) else Color.Gray.copy(alpha = 0.3f)),
                contentColor = Color.Black
            )
        )
        {
            Text(
                text = "Add Item",
                fontSize = buttonFontSize(savedFontSize.value).sp
            )
        }
    }
}
