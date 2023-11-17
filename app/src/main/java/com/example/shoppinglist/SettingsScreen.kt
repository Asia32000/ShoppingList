package com.example.shoppinglist

import UserStore
import android.content.Context
import android.util.Log
import androidx.annotation.ColorRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val store = UserStore(context)

    val savedListName = store.getListName.collectAsState(initial = "")
    var listName by remember { mutableStateOf(savedListName.value) }

    val options = listOf("Blue", "Red", "Green", "Magenta")
    val savedColorName = store.getColorName.collectAsState(initial = "")
    var selectedColorName by remember { mutableStateOf(savedColorName.value) }

    val fontSizeOptions = listOf("Small", "Default", "Large")
    val savedFontSize = store.getFontSize.collectAsState(initial = "")
    var selectedFontSize by remember { mutableStateOf(savedFontSize.value) }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(16.dp, top = 30.dp),
                text = "Change shopping list name",
                color = Color.Gray,
                fontSize = infoTextFontSize(savedFontSize.value).sp
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                value = savedListName.value,
                onValueChange = {
                    listName = it
                    CoroutineScope(Dispatchers.IO).launch {
                        store.saveListName(listName)
                    }
                                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "clear text",
                        modifier = Modifier
                            .clickable {
                                listName = ""
                                CoroutineScope(Dispatchers.IO).launch {
                                    store.saveListName(listName)
                                }
                            }
                    )
                },
                textStyle = TextStyle(fontSize = textFontSize(savedFontSize.value).sp)
            )

            Spacer(modifier = Modifier.requiredHeight(32.dp))

            Text(
                modifier = Modifier
                    .padding(start=16.dp, end=16.dp),
                fontSize = infoTextFontSize(savedFontSize.value).sp,
                text = "Choose buttons color",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.requiredWidth(16.dp))

            getColorIndex(savedColorName.value)?.let { it ->
                SegmentedControl(
                    items = options,
                    defaultSelectedItemIndex = it,
                    fontSize = buttonFontSize(savedFontSize.value),
                    color = buttonColor(savedColorName.value)
                ) {
                    selectedColorName = getColorName(it)
                    CoroutineScope(Dispatchers.IO).launch {
                        store.saveColorName(selectedColorName)
                    }
                }
            }

            Spacer(modifier = Modifier.requiredHeight(32.dp))
            Text(
                modifier = Modifier
                    .padding(start=16.dp, end=16.dp),
                fontSize = infoTextFontSize(savedFontSize.value).sp,
                text = "Choose font size",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.requiredWidth(16.dp))
            getFontSizeIndex(savedFontSize.value)?.let { it ->
                SegmentedControl(
                    items = fontSizeOptions,
                    defaultSelectedItemIndex = it,
                    fontSize = buttonFontSize(savedFontSize.value),
                    color = buttonColor(savedColorName.value)
                ) {
                    selectedFontSize = getFontSizeName(it)
                    CoroutineScope(Dispatchers.IO).launch {
                        store.saveFontSize(selectedFontSize)
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentedControl(
    items: List<String>,
    defaultSelectedItemIndex: Int,
    useFixedWidth: Boolean = false,
    itemWidth: Dp = 120.dp,
    cornerRadius: Int = 10,
    fontSize: Int,
    color: Color,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableStateOf(defaultSelectedItemIndex) }
    Log.d("tag", "what i get: $defaultSelectedItemIndex")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        items.forEachIndexed { index, item ->
            OutlinedButton(
                modifier = when (index) {
                    0 -> {
                        if (useFixedWidth) {
                            Modifier
                                .width(itemWidth)
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        } else {
                            Modifier
                                .wrapContentSize()
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        }
                    } else -> {
                        if (useFixedWidth)
                            Modifier
                                .width(itemWidth)
                                .offset((-1 * index).dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        else Modifier
                            .wrapContentSize()
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (selectedIndex.value == index) 1f else 0f)
                    }
                },
                onClick = {
                    selectedIndex.value = index
                    onItemSelection(selectedIndex.value)
                },
                shape = when (index) {
                    /**
                     * left outer button
                     */
                    0 -> RoundedCornerShape(
                        topStartPercent = cornerRadius,
                        topEndPercent = 0,
                        bottomStartPercent = cornerRadius,
                        bottomEndPercent = 0
                    )
                    /**
                     * right outer button
                     */
                    items.size - 1 -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = cornerRadius,
                        bottomStartPercent = 0,
                        bottomEndPercent = cornerRadius
                    )
                    /**
                     * middle button
                     */
                    else -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomStartPercent = 0,
                        bottomEndPercent = 0
                    )
                },
                border = BorderStroke(
                    1.dp, if (selectedIndex.value == index) {
                        color
                    } else {
                        color
                    }
                ),
                colors = if (selectedIndex.value == index) {
                    /**
                     * selected colors
                     */
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = color
                    )
                } else {
                    /**
                     * not selected colors
                     */
                    ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                }
            ) {
                Text(
                    text = item,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedIndex.value == index) {
                        Color.White
                    } else {
                        color
                    },
                    fontSize = fontSize.sp
                )
            }
        }
    }
}

fun getColorName(item: Int): String {
    when(item) {
        0 -> return "Blue"
        1 -> return "Red"
        2 -> return "Green"
        3 -> return "Magenta"
    }
    return ""
}

fun getColorIndex(item: String): Int? {
    when(item) {
        "Blue" -> return 0
        "Red" -> return 1
        "Green" -> return 2
        "Magenta" -> return 3
    }
    return null
}

fun getFontSizeName(item: Int): String {
    when(item) {
        0 -> return "Small"
        1 -> return "Default"
        2 -> return "Large"
    }
    return ""
}

fun getFontSizeIndex(item: String): Int? {
    when(item) {
        "Small" -> return 0
        "Default" -> return 1
        "Large" -> return 2
    }
    return null
}
