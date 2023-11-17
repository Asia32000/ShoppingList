package com.example.shoppinglist

import UserStore
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListsScreen(viewModel: ProductViewModel) {
    val context = LocalContext.current
    val products by viewModel.products.collectAsState(emptyList())
    val store = UserStore(context)
    val savedListName = store.getListName.collectAsState(initial = "")
    val savedColor = store.getColorName.collectAsState(initial = "")
    val savedFontSize = store.getFontSize.collectAsState(initial = "")

    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = savedListName.value,
                modifier = Modifier
                    .align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                fontSize = titleFontSize(savedFontSize.value).sp
            )
            IconButton(
                onClick = {
                    context.startActivity(Intent(context, AddProductActivity::class.java))
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(color = buttonColor(savedColor.value)),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
        Divider()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(products) { product ->
                var checkBoxState by remember(product.id) { mutableStateOf(product.status) }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Checkbox(
                        checked = checkBoxState,
                        onCheckedChange = {
                            checkBoxState = it
                            product.status = it
                            viewModel.updateProduct(product)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = buttonColor(savedColor.value)
                        )
                    )
                    Column {
                        Text(
                            text = product.name,
                            fontSize = textFontSize(savedFontSize.value).sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row {
                            Text(
                                text = "Amount: ${product.amount}",
                                fontSize = detailsFontSize(savedFontSize.value).sp,
                                color = Color.Gray
                            )
                            if (product.cost?.isNotEmpty() == true) {
                                Spacer(modifier = Modifier.requiredWidth(16.dp))
                                Text(
                                    text = "Cost: ${product.cost}",
                                    fontSize = detailsFontSize(savedFontSize.value).sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        
                    }
                    Spacer(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight())
                    IconButton(
                        onClick = {
                            val intent = Intent(context, EditProductActivity::class.java)
                            intent.apply { putExtra("name", product.name) }
                            intent.apply { putExtra("amount", product.amount) }
                            intent.apply { putExtra("id", product.id) }
                            intent.apply { putExtra("status", product.status) }
                            intent.apply { putExtra("cost", product.cost) }
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(
                        onClick = {
                            viewModel.deleteProduct(product)
                        },
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
                Divider()
            }
        }
    }
}

fun buttonColor(name: String): Color {
    when (name) {
        "Blue" -> return Color.Blue.copy(alpha = 0.3f)
        "Red" -> return Color.Red.copy(alpha = 0.3f)
        "Green" -> return Color.Green.copy(alpha = 0.9f, blue = 0.4f, red = 0.2f, green = 0.8f)
        "Magenta" -> return Color.Magenta.copy(alpha = 0.6f)
    }
    return Color.Blue.copy(alpha = 0.3f)
}

fun titleFontSize(name: String): Int {
    when(name) {
        "Small" -> return 18
        "Default" -> return 24
        "Large" -> return 30
    }
    return 24
}

fun textFontSize(name: String): Int {
    when(name) {
        "Small" -> return 16
        "Default" -> return 18
        "Large" -> return 22
    }
    return 24
}

fun detailsFontSize(name: String): Int {
    when(name) {
        "Small" -> return 14
        "Default" -> return 16
        "Large" -> return 20
    }
    return 24
}