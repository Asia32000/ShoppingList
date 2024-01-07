package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

class MapActivity() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var locations = arrayOf<LatLng>()
        var names = arrayOf<String?>()

        val count = intent.getIntExtra("count", 0)

        (1..count).forEach {
            val latitude = intent.getDoubleExtra("latitude $it", 0.0)
            val longitude = intent.getDoubleExtra("longitude $it", 0.0)
            names += intent.getStringExtra("name $it")
            locations += LatLng(latitude, longitude)
        }

        super.onCreate(savedInstanceState)
        setContent {
            MapScreen(locations, names)
        }
    }
}

@Composable
fun MapScreen(locations: Array<LatLng>, names: Array<String?>) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize()
        ) {
            locations.forEachIndexed { index, latLng ->
                Marker(
                    state = MarkerState(latLng),
                    title = names[index]
                )
            }
        }
    }
}
