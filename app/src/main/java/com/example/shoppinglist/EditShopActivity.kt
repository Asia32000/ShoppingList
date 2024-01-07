package com.example.shoppinglist

import UserStore
import android.location.Location
import android.location.LocationManager
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.ui.theme.ShoppingListTheme


class EditShopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                val viewModel = ShopViewModel(application)
                val name = intent.getStringExtra("name")
                val description = intent.getStringExtra("description")
                val id = intent.getIntExtra("id", -1)
                val radius = intent.getStringExtra("radius")
                val latitude = intent.getDoubleExtra("latitude", 0.0)
                val longitude = intent.getDoubleExtra("longitude", 0.0)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (name != null && id != null && radius != null) {
                        println("name: $name, id: $id, radius: $radius")
                        EditShopScreen(viewModel, goToPreviousActivity = { finish() }, name = name, description, id, radius, latitude, longitude)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShopScreen(viewModel: ShopViewModel, goToPreviousActivity: () -> Unit, name: String, description: String?, id: Int, radius: String, latitude: Double, longitude: Double) {
    var nameText by remember { mutableStateOf(name) }
    var description by remember { mutableStateOf(description) }
    var radius by remember { mutableStateOf(radius) }
    val shops by viewModel.shops.collectAsState(emptyList())
    val context = LocalContext.current
    val store = UserStore(context)
    val savedColor = store.getColorName.collectAsState(initial = "")
    val savedFontSize = store.getFontSize.collectAsState(initial = "")

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
            text = "Description",
            color = Color.Gray,
            fontSize = infoTextFontSize(savedFontSize.value).sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = description ?: "",
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
            fontSize = infoTextFontSize(savedFontSize.value).sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
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
            })

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 96.dp, end = 96.dp),
            onClick = {
                if (nameText.isNotEmpty() && radius.isNotEmpty()) {
                    val previousShop = shops.first { it.id == id }
                    viewModel.deleteShop(previousShop)

                    val newShop = Shop(id = id, name = nameText, description = description, radius = radius, latitude, longitude)
                    viewModel.insertShop(newShop)

                    goToPreviousActivity()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = (if (nameText.isNotEmpty() && radius.isNotEmpty()) buttonColor(savedColor.value) else Color.Gray.copy(alpha = 0.3f)),
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