package com.example.shoppinglist

import UserStore
import android.os.Bundle
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
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.google.firebase.Firebase
import com.google.firebase.database.database

class EditProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                val viewModel = ProductViewModel(application)
                val name = intent.getStringExtra("name")
                val amount = intent.getStringExtra("amount")
                val id = intent.getIntExtra("id", -1)
                val cost = intent.getStringExtra("cost")
                val status = intent.getBooleanExtra("status", false)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (name != null && amount != null && id != null && status != null && status != null) {
                        EditProductScreen(viewModel, goToPreviousActivity = { finish() }, name = name, amount = amount, id = id, status = status, cost = cost)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(viewModel: ProductViewModel, goToPreviousActivity: () -> Unit, name: String, amount: String, id: Int, status: Boolean, cost: String?) {
    var nameText by remember { mutableStateOf(name) }
    var amount by remember { mutableStateOf(amount) }
    var cost by remember { mutableStateOf(cost) }
    val products by viewModel.products.collectAsState(emptyList())
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
            })
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
                .padding(start = 16.dp, end = 16.dp),
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
                .padding(start = 16.dp, end = 16.dp),
            value = cost ?: "",
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
                .padding(start = 96.dp, end=96.dp),
            onClick = {
                if (nameText.isNotEmpty() && amount.isNotEmpty()) {

                    myRef.child(id.toString()).child("status").setValue(status)
                    myRef.child(id.toString()).child("name").setValue(nameText)
                    myRef.child(id.toString()).child("amount").setValue(amount)
                    myRef.child(id.toString()).child("cost").setValue(cost)

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
                text = "Save Changes",
                fontSize = buttonFontSize(savedFontSize.value).sp
            )
        }
    }
}

fun infoTextFontSize(name: String): Int {
    when(name) {
        "Small" -> return 10
        "Default" -> return 12
        "Large" -> return 14
    }
    return 24
}

fun buttonFontSize(name: String): Int {
    when(name) {
        "Small" -> return 14
        "Default" -> return 16
        "Large" -> return 18
    }
    return 24
}