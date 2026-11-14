package com.apple.experience

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { LauncherScreen() } }
    }
}

@Composable
fun LauncherScreen() {
    val context = LocalContext.current
    val apps = (1..12).map { "App " }
    
    // Stato per l'ora e la batteria
    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }
    var batteryLevel by remember { mutableStateOf(0) }

    // Aggiorna ora e data ogni minuto
    LaunchedEffect(Unit) {
        while (true) {
            val now = Calendar.getInstance().time
            currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
            currentDate = SimpleDateFormat("EEE d", Locale.getDefault()).format(now)
            kotlinx.coroutines.delay(60000)
        }
    }

    // Ascolta la batteria
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                if (level != -1 && scale != -1) {
                    batteryLevel = (level * 100 / scale.toFloat()).toInt()
                }
            }
        }
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        onDispose { context.unregisterReceiver(receiver) }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0A0A))) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // --- SEZIONE WIDGET iOS ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Widget Orologio
                Box(
                    modifier = Modifier.weight(1f).height(100.dp)
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(currentTime, color = Color.White, fontSize = 36.sp)
                        Text("Orologio", color = Color.Gray, fontSize = 12.sp)
                    }
                }

                // Widget Calendario
                Box(
                    modifier = Modifier.weight(1f).height(100.dp)
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(currentDate, color = Color.White, fontSize = 24.sp)
                        Text("Calendario", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Widget Batteria
                Box(
                    modifier = Modifier.weight(1f).height(80.dp)
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("??", fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                        Text("%", color = Color.White, fontSize = 24.sp)
                    }
                }
                
                // Spazio vuoto per bilanciare (puoi mettere un altro widget qui)
                Box(modifier = Modifier.weight(1f).height(80.dp))
            }

            // --- GRIGLIA APP ---
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(apps) { app ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(60.dp).background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)))
                        Text(app, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
        
        // --- DOCK ---
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp).fillMaxWidth(0.9f).height(80.dp)
                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(30.dp))
        )
    }
}
