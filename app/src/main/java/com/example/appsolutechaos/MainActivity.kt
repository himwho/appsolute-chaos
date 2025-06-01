/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appsolutechaos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SubspaceModifier
import com.example.appsolutechaos.ui.theme.AppsolutheChaosTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            AppsolutheChaosTheme {
                ChaosGameContent()
            }
        }
    }
}

@Composable
fun ChaosGameContent() {
    val spatialCapabilities = LocalSpatialCapabilities.current
    
    if (spatialCapabilities.isSpatialUiEnabled) {
        // XR spatial content
        Subspace {
            SpatialPanel(modifier = SubspaceModifier) {
                ChaosGameUI()
            }
        }
    } else {
        // 2D fallback content (what we'll see now)
        ChaosGameUI()
    }
}

@Composable
fun ChaosGameUI() {
    var currentLevel by remember { mutableIntStateOf(1) }
    var buttonConfigs by remember { mutableStateOf<List<ButtonConfig>>(generateRandomButtons()) }
    var correctButtonIndex by remember { mutableIntStateOf(0) }
    var instructions by remember { mutableStateOf("") }
    var windowOffsetX by remember { mutableFloatStateOf(0f) }
    var windowOffsetY by remember { mutableFloatStateOf(0f) }
    var movementKey by remember { mutableIntStateOf(0) }

    // Initialize instructions
    LaunchedEffect(Unit) {
        correctButtonIndex = Random.nextInt(buttonConfigs.size)
        instructions = generateInstructions(currentLevel, buttonConfigs[correctButtonIndex])
    }

    // Move window every 3 seconds
    LaunchedEffect(movementKey) {
        while (true) {
            delay(3000)
            
            // Generate new random position
            windowOffsetX = Random.nextFloat() * 400f - 200f // -200 to 200
            windowOffsetY = Random.nextFloat() * 300f - 150f // -150 to 150
            
            // Regenerate buttons and select new correct one
            buttonConfigs = generateRandomButtons()
            correctButtonIndex = Random.nextInt(buttonConfigs.size)
            instructions = generateInstructions(currentLevel, buttonConfigs[correctButtonIndex])
            
            movementKey++
        }
    }

    // Animated movement
    val animatedOffsetX by animateFloatAsState(
        targetValue = windowOffsetX,
        animationSpec = tween(1000, easing = EaseInOutCubic),
        label = "offsetX"
    )
    val animatedOffsetY by animateFloatAsState(
        targetValue = windowOffsetY,
        animationSpec = tween(1000, easing = EaseInOutCubic),
        label = "offsetY"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Moving chaos window
        Card(
            modifier = Modifier
                .size(600.dp, 500.dp)
                .absoluteOffset(animatedOffsetX.dp, animatedOffsetY.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "APPSOLUTE CHAOS",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Level $currentLevel",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                // Instructions
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Blue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = instructions,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Button Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(buttonConfigs) { index, config ->
                        val isCorrect = index == correctButtonIndex
                        
                        Button(
                            onClick = {
                                if (isCorrect) {
                                    currentLevel++
                                    // Don't regenerate immediately, let the timer handle it
                                } else {
                                    // Wrong button - reset to level 1
                                    currentLevel = 1
                                }
                            },
                            modifier = Modifier
                                .aspectRatio(1f)
                                .width(config.size)
                                .height(config.size),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = config.color
                            ),
                            shape = config.shape
                        ) {
                            Text(
                                text = config.text,
                                fontSize = config.textSize,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Game Stats
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Green),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Window moves every 3s • Click correct button to advance!",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

data class ButtonConfig(
    val text: String,
    val color: Color,
    val size: Dp,
    val textSize: androidx.compose.ui.unit.TextUnit,
    val shape: RoundedCornerShape
)

fun generateRandomButtons(): List<ButtonConfig> {
    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow, 
        Color.Magenta, Color.Cyan, Color(0xFF8B4513), Color(0xFF4B0082)
    )
    val texts = listOf("GO", "STOP", "YES", "NO", "OK", "EXIT", "NEXT", "BACK", "START")
    val sizes = listOf(70.dp, 80.dp, 90.dp)
    val textSizes = listOf(10.sp, 12.sp, 14.sp)
    
    return (0..8).map {
        ButtonConfig(
            text = texts.random(),
            color = colors.random(),
            size = sizes.random(),
            textSize = textSizes.random(),
            shape = RoundedCornerShape(Random.nextInt(4, 16).dp)
        )
    }
}

fun generateInstructions(level: Int, correctButton: ButtonConfig): String {
    val colorName = when (correctButton.color) {
        Color.Red -> "RED"
        Color.Green -> "GREEN" 
        Color.Blue -> "BLUE"
        Color.Yellow -> "YELLOW"
        Color.Magenta -> "MAGENTA"
        Color.Cyan -> "CYAN"
        Color(0xFF8B4513) -> "BROWN"
        Color(0xFF4B0082) -> "PURPLE"
        else -> "MYSTERY COLOR"
    }
    
    return "Level $level: Find and click the $colorName button labeled '${correctButton.text}'! Wrong clicks reset you to Level 1!"
}