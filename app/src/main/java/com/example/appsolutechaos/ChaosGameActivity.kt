package com.example.appsolutechaos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SubspaceModifier
import kotlinx.coroutines.delay
import kotlin.random.Random
import com.example.appsolutechaos.ui.theme.AppsolutheChaosTheme

class ChaosGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChaosGameApp()
        }
    }
}

@Composable
fun ChaosGameApp() {
    AppsolutheChaosTheme {
        ChaosGameContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaosGameContent() {
    var currentLevel by remember { mutableIntStateOf(1) }
    var buttonConfigs by remember { mutableStateOf(generateRandomButtons()) }
    var correctButtonIndex by remember { mutableIntStateOf(0) }
    var isGameActive by remember { mutableStateOf(true) }
    var instructions by remember { mutableStateOf("Find and click the GREEN button to proceed to level 2!") }
    var panelKey by remember { mutableIntStateOf(0) } // Key to force recomposition for movement

    // Move window and change button configurations every 3 seconds
    LaunchedEffect(isGameActive) {
        if (isGameActive) {
            while (true) {
                delay(3000)
                buttonConfigs = generateRandomButtons()
                correctButtonIndex = Random.nextInt(buttonConfigs.size)
                instructions = generateInstructions(currentLevel, buttonConfigs[correctButtonIndex])
                panelKey++ // Force recomposition to simulate movement
            }
        }
    }

    Subspace {
        // Use key to simulate panel movement - each key change creates a new spatial position
        key(panelKey) {
            SpatialPanel(
                modifier = SubspaceModifier
            ) {
                // Use a bright background and explicit content sizing
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .size(800.dp, 600.dp)
                        .padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Header with bright colors
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Red, RoundedCornerShape(16.dp))
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "APPSOLUTE CHAOS",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Level $currentLevel",
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }
                        }

                        // Instructions with bright background
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Blue, RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = instructions,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Simplified Button Grid with bright colors
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(buttonConfigs.take(6)) { index, config -> // Limit to 6 buttons for better visibility
                                val isCorrect = index == correctButtonIndex
                                
                                Button(
                                    onClick = {
                                        if (isCorrect) {
                                            currentLevel++
                                            // Regenerate buttons immediately when correct
                                            buttonConfigs = generateRandomButtons()
                                            correctButtonIndex = Random.nextInt(6) // Limit to 6 buttons
                                            instructions = generateInstructions(currentLevel, buttonConfigs[correctButtonIndex])
                                        } else {
                                            // Wrong button clicked - reset to level 1
                                            currentLevel = 1
                                            instructions = "Wrong button! Starting over at level 1. Find the GREEN button!"
                                        }
                                    },
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .size(80.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = config.color
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = config.text,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        // Game Stats with bright background
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Green, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Chaos Level: $currentLevel • Window moves every 3s",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ButtonConfig(
    val text: String,
    val color: Color,
    val size: Dp,
    val textSize: TextUnit,
    val shape: RoundedCornerShape
)

fun generateRandomButtons(): List<ButtonConfig> {
    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow, 
        Color.Magenta, Color.Cyan, Color(0xFF8B4513), Color(0xFF4B0082)
    )
    val texts = listOf("GO", "STOP", "YES", "NO", "OK", "EXIT")
    
    return (0..7).map {
        ButtonConfig(
            text = texts.random(),
            color = colors.random(),
            size = 80.dp,
            textSize = 12.sp,
            shape = RoundedCornerShape(8.dp)
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
    
    return "Level $level: Click the $colorName '${correctButton.text}' button!"
} 