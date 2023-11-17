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
import com.example.shoppinglist.ui.theme.ShoppingListTheme

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.requiredHeight(32.dp))
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
        Spacer(modifier = Modifier.requiredHeight(32.dp))
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

        Spacer(modifier = Modifier.requiredHeight(32.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = cost ?: "Cost",
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
                    val oldProduct = products.first { it.id == id }
                    viewModel.deleteProduct(oldProduct)

                    val newProduct = Product(name = nameText, amount = amount, status = status, id = id, cost = cost)
                    viewModel.insertProduct(newProduct)

                    goToPreviousActivity()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = (if (nameText.isNotEmpty() && amount.isNotEmpty()) buttonColor(savedColor.value) else Color.Gray.copy(alpha = 0.3f)),
                contentColor = Color.Black
            )
        )
        {
            Text(text = "Save Changes")
        }
    }
}