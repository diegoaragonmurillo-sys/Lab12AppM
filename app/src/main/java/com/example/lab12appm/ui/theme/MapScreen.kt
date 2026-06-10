package com.example.lab12appm.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.lab12appm.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen() {

    val arequipaLocation = LatLng(-16.4040102, -71.559611)

    // 3.2 – Varios marcadores
    val locations = listOf(
        LatLng(-16.433415,  -71.5442652), // JLByR
        LatLng(-16.4205151, -71.4945209), // Paucarpata
        LatLng(-16.3524187, -71.5675994)  // Zamacola
    )

    // 5.1 – Polígonos
    val mallAventuraPolygon = listOf(
        LatLng(-16.432292, -71.509145),
        LatLng(-16.432757, -71.509626),
        LatLng(-16.433013, -71.509310),
        LatLng(-16.432566, -71.508853)
    )
    val parqueLambramaniPolygon = listOf(
        LatLng(-16.422704, -71.530830),
        LatLng(-16.422920, -71.531340),
        LatLng(-16.423264, -71.531110),
        LatLng(-16.423050, -71.530600)
    )
    val plazaDeArmasPolygon = listOf(
        LatLng(-16.398866, -71.536961),
        LatLng(-16.398744, -71.536529),
        LatLng(-16.399178, -71.536289),
        LatLng(-16.399299, -71.536721)
    )

    // 5.1 – Polilíneas (ruta entre distritos y ruta hacia Yura)
    val rutaDistritosPolyline = listOf(
        LatLng(-16.433415,  -71.5442652), // JLByR
        LatLng(-16.4205151, -71.4945209), // Paucarpata
        LatLng(-16.3524187, -71.5675994)  // Zamacola
    )
    val rutaYuraPolyline = listOf(
        LatLng(-16.4040102, -71.559611),  // Arequipa centro
        LatLng(-16.3200000, -71.600000),  // punto intermedio
        LatLng(-16.2520984, -71.6836503)  // Yura
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    val context = LocalContext.current

    // 4.1 – Mover cámara automáticamente a Yura al iniciar
    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(-16.2520984, -71.6836503), 12f
            ),
            durationMs = 3000
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {

            // 3.1 – Marcador principal con imagen personalizada
            val customIcon = remember {
                BitmapDescriptorFactory.fromBitmap(
                    ContextCompat.getDrawable(context, R.drawable.mai)!!
                        .toBitmap(width = 250, height = 250)
                )
            }
            Marker(
                state = rememberMarkerState(position = arequipaLocation),
                icon = customIcon,
                title = "Arequipa, Perú"
            )

            // 3.2 – Varios marcadores iterando la lista
            locations.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Ubicación",
                    snippet = "Punto de interés"
                )
            }
            
            Polygon(
                points = plazaDeArmasPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )
            Polygon(
                points = parqueLambramaniPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )
            Polygon(
                points = mallAventuraPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )

            Polyline(
                points = rutaDistritosPolyline,
                color = Color.Magenta,
                width = 8f
            )
            Polyline(
                points = rutaYuraPolyline,
                color = Color.Green,
                width = 8f
            )
        }
    }
}