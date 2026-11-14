package com.apple.experience

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { DashboardScreen() } }
    }
}

@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    var glassEnabled by remember { mutableStateOf(true) }
    var islandEnabled by remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(colors = listOf(Color(0xFF1C1C1E), Color(0xFF2C2C2E)))

    Column(
        modifier = Modifier.fillMaxSize().background(gradient).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Apple Experience", color = Color.White, fontSize = 28.sp, modifier = Modifier.padding(top = 40.dp, bottom = 20.dp))
        
        Box(
            modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = if (glassEnabled) 0.1f else 0.3f), shape = RoundedCornerShape(24.dp)).padding(16.dp)
        ) {
            Column {
                Text("Personalizzazione", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp))
                
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Effetto Glass", color = Color.White)
                    Switch(checked = glassEnabled, onCheckedChange = { glassEnabled = it })
                }
                
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Dynamic Island", color = Color.White)
                    Switch(checked = islandEnabled, onCheckedChange = { 
                        islandEnabled = it
                        val intent = Intent(context, DynamicIslandService::class.java)
                        if (it) context.startService(intent) else context.stopService(intent)
                    })
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                val pm = context.packageManager
                val componentName = android.content.ComponentName(context, FakeLauncherActivity::class.java)
                pm.setComponentEnabledSetting(componentName, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED, android.content.pm.PackageManager.DONT_KILL_APP)
                val selector = Intent(Intent.ACTION_MAIN)
                selector.addCategory(Intent.CATEGORY_HOME)
                selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(selector)
                pm.setComponentEnabledSetting(componentName, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED, android.content.pm.PackageManager.DONT_KILL_APP)
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
        ) { Text("Ripristina Launcher Originale", color = Color.White) }
    }
}
