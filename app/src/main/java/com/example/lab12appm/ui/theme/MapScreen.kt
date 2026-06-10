package com.example.lab12appm.ui.theme

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.lab12appm.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen() {

    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val context = LocalContext.current

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

    // 5.1 – Polilíneas
    val rutaDistritosPolyline = listOf(
        LatLng(-16.433415,  -71.5442652),
        LatLng(-16.4205151, -71.4945209),
        LatLng(-16.3524187, -71.5675994)
    )
    val rutaYuraPolyline = listOf(
        LatLng(-16.4040102, -71.559611),
        LatLng(-16.3200000, -71.600000),
        LatLng(-16.2520984, -71.6836503)
    )

    // Estado cámara
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    // Ejercicio 1 – Tipo de mapa
    var mapType by remember { mutableStateOf(MapType.NORMAL) }

    // Ejercicio 2 – Ubicación actual
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            getCurrentLocation(context) { lat, lng ->
                userLocation = LatLng(lat, lng)
            }
        }
    }

    // 4.1 – Animación de cámara al iniciar
    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(-16.2520984, -71.6836503), 12f
            ),
            durationMs = 3000
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Ejercicio 1 – Botones tipo de mapa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(
                "Normal"   to MapType.NORMAL,
                "Híbrido"  to MapType.HYBRID,
                "Satélite" to MapType.SATELLITE,
                "Terreno"  to MapType.TERRAIN
            ).forEach { (label, type) ->
                Button(
                    onClick = { mapType = type },
                    modifier = Modifier.padding(horizontal = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (mapType == type) Color(0xFF1565C0) else Color.Gray
                    )
                ) {
                    Text(text = label, color = Color.White, fontSize = 11.sp)
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = mapType)
            ) {

                // 3.1 – Marcador con imagen personalizada
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

                // 3.2 – Varios marcadores
                locations.forEach { location ->
                    Marker(
                        state = rememberMarkerState(position = location),
                        title = "Ubicación",
                        snippet = "Punto de interés"
                    )
                }

                // 5.1 – Polígonos
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

                // 5.1 – Polilíneas
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

                userLocation?.let { loc ->
                    Marker(
                        state = rememberMarkerState(position = loc),
                        title = "Mi ubicación",
                        snippet = "Estás aquí",
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                    )
                }
            }

            // Ejercicio 2 – Botón flotante ubicación
            FloatingActionButton(
                onClick = {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Color(0xFF1565C0)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Mi ubicación",
                    tint = Color.White
                )
            }
        }
    }
}