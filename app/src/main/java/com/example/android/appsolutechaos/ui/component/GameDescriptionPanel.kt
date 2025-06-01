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
fun GameDescriptionPanel(
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
                text = "LEVEL DESCRIPTIONS",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "LEVEL 1",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Cyan
            )
            Text(
                text = "Randomized floating UI modals in 3D space. Find the correct button before time runs out! Buttons reposition every 3 seconds, with penalties for wrong presses.",
                fontSize = 12.sp,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "LEVEL 2",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Magenta
            )
            Text(
                text = "3D audio cues trigger keypad spawning. Listen for number hints and enter the 6-digit code before the keypad fades away. Look for reset buttons when fading begins!",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun GameDescriptionPanelPreview() {
    AppsoluteChaosTheme {
        GameDescriptionPanel()
    }
} 