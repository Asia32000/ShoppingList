package com.example.shoppinglist

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class AddShopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                val viewModel = ShopViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AddShopScreen(viewModel, goToPreviousActivity = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopScreen(viewModel: ShopViewModel, goToPreviousActivity: () -> Unit) {
    var nameText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp, top = 30.dp),
            text = "Name",
            color = Color.Gray,
            fontSize = 14.sp
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
            text = "Description",
            color = Color.Gray,
            fontSize = 14.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=16.dp, end=16.dp),
            value = description,
            onValueChange = { description = it },
            trailingIcon = {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .clickable {
                            description = ""
                        }
                )
            })

        Text(
            modifier = Modifier
                .padding(16.dp, top = 30.dp),
            text = "Radius",
            color = Color.Gray,
            fontSize = 14.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=16.dp, end=16.dp),
            value = radius,
            onValueChange = { radius = it },
            trailingIcon = {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .clickable {
                            radius = ""
                        }
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start=128.dp, end=128.dp),
            onClick = {
                if (nameText.isNotEmpty() && radius.isNotEmpty()) {
                    val shop = Shop(name = nameText, description = description, radius = radius, latitude = 0f, longitude = 0f)
                    viewModel.insertShop(shop)
                    goToPreviousActivity()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = (if (nameText.isNotEmpty() && radius.isNotEmpty()) Color.Blue.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.3f)),
                contentColor = Color.Black
            )
        )
        {
            Text(
                text = "Add Shop",
                fontSize = 16.sp
            )
        }
    }
}