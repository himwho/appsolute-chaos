package com.example.android.appsolutechaos.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.appsolutechaos.ui.theme.AppsoluteChaosTheme

@Composable
fun GameInstructionsPanel(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "HOW TO PLAY",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "CONTROLS",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow
            )
            Text(
                text = "• Look around to find UI elements\n• Tap buttons to interact\n• Watch for hints and timers",
                fontSize = 12.sp,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "TIPS",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow
            )
            Text(
                text = "• Pay attention to audio cues\n• Check all directions in 3D space\n• Speed matters - but accuracy more!",
                fontSize = 12.sp,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "SCORING",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow
            )
            Text(
                text = "Based on completion time, loops survived, and accuracy. Lower is better!",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun GameInstructionsPanelPreview() {
    AppsoluteChaosTheme {
        GameInstructionsPanel()
    }
} 