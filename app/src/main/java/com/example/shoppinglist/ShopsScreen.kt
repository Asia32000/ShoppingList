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
import androidx.compose.runtime.mutableStateListOf
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
fun ShopsScreen(viewModel: ShopViewModel) {
    val context = LocalContext.current
    val store = UserStore(context)
    val savedFontSize = store.getFontSize.collectAsState(initial = "")
    val savedColor = store.getColorName.collectAsState(initial = "")
    val shops by viewModel.shops.collectAsState(emptyList())

    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Shops List",
                modifier = Modifier
                    .align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                fontSize = titleFontSize(savedFontSize.value).sp
            )

            IconButton(
                onClick = {
                    context.startActivity(Intent(context, AddShopActivity::class.java))
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
            items(shops) { shop ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = shop.name ?: "",
                            fontSize = textFontSize(savedFontSize.value).sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Description: ${shop.description}",
                            fontSize = detailsFontSize(savedFontSize.value).sp,
                            color = Color.Gray
                        )

                        Row {
                            Text(
                            text = "Radius: ${shop.radius}",
                            fontSize = detailsFontSize(savedFontSize.value).sp,
                            color = Color.Gray
                            )
                            Spacer(modifier = Modifier.requiredWidth(16.dp))
                            Text(
                                text = "Latitude: ${shop.latitude}",
                                fontSize = detailsFontSize(savedFontSize.value).sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.requiredWidth(16.dp))
                            Text(
                                text = "Longitude: ${shop.longitude}",
                                fontSize = detailsFontSize(savedFontSize.value).sp,
                                color = Color.Gray
                            )
                        }
                    }
                    Spacer(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight())
                    IconButton(
                        onClick = {
                            val intent = Intent(context, EditShopActivity::class.java)
                            intent.apply { putExtra("name", shop.name) }
                            intent.apply { putExtra("description", shop.description) }
                            intent.apply { putExtra("id", shop.id) }
                            intent.apply { putExtra("radius", shop.radius) }
                            intent.apply { putExtra("longitude", shop.longitude) }
                            intent.apply { putExtra("latitude", shop.latitude) }
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(
                        onClick = {
                            viewModel.deleteShop(shop)
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