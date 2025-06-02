package com.example.android.appsolutechaos.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.offset
import androidx.xr.compose.subspace.layout.width
import com.example.android.appsolutechaos.ui.component.FloatingGameButton
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

data class FloatingButton(
    val id: Int,
    val x: Float,
    val y: Float,
    val z: Float,
    val color: Color,
    val shape: ButtonShape,
    val isCorrect: Boolean
)

enum class ButtonShape {
    CIRCLE, SQUARE, ROUNDED_RECTANGLE
}

@Composable
fun Level1GameContent(
    onLevelComplete: (loops: Int, totalTime: Float, incorrectPresses: Int) -> Unit,
    onBackToMenu: () -> Unit
) {
    var gameTime by remember { mutableStateOf(0f) }
    var loops by remember { mutableStateOf(0) }
    var incorrectPresses by remember { mutableStateOf(0) }
    var repositionTimer by remember { mutableStateOf(3f) }
    var buttons by remember { mutableStateOf(generateRandomButtons()) }
    var showHint by remember { mutableStateOf(true) }
    
    // Hint rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "hint_rotation")
    val hintRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "hint_rotation"
    )

    // Game timer
    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            gameTime += 0.1f
            repositionTimer -= 0.1f
            
            // Reposition buttons every 3 seconds (+ penalty time)
            if (repositionTimer <= 0f) {
                buttons = generateRandomButtons()
                repositionTimer = 3f + (incorrectPresses * 0.25f)
                loops++
            }
        }
    }

    // True 3D floating buttons - content within existing Subspace
    buttons.forEach { button ->
        SpatialPanel(
            modifier = SubspaceModifier
                .width(80.dp)
                .height(80.dp)
                .offset(
                    x = button.x.dp,
                    y = button.y.dp,
                    z = button.z.dp
                )
        ) {
            // No Card wrapper - just the button as a pure 3D object
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(
                        when (button.shape) {
                            ButtonShape.CIRCLE -> CircleShape
                            ButtonShape.SQUARE -> RoundedCornerShape(4.dp)
                            ButtonShape.ROUNDED_RECTANGLE -> RoundedCornerShape(16.dp)
                        }
                    )
                    .background(button.color)
                    .clickable {
                        if (button.isCorrect) {
                            onLevelComplete(loops, gameTime, incorrectPresses)
                        } else {
                            incorrectPresses++
                            repositionTimer += 0.25f
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (button.isCorrect) "✓" else "✗",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // Floating hint text - orbiting around user
    if (showHint) {
        val hintRadius = 600f
        val hintX = (hintRadius * sin(Math.toRadians(hintRotation.toDouble()))).toFloat()
        val hintZ = (-800 + hintRadius * cos(Math.toRadians(hintRotation.toDouble()))).toFloat()
        
        SpatialPanel(
            modifier = SubspaceModifier
                .width(200.dp)
                .height(120.dp)
                .offset(x = hintX.dp, y = 150.dp, z = hintZ.dp)
        ) {
            // Semi-transparent floating hint
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Find ✓",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Yellow
                    )
                    Text(
                        text = "Loop ${loops + 1}",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Text(
                        text = "${repositionTimer.toInt()}s",
                        fontSize = 14.sp,
                        color = Color.Cyan
                    )
                }
            }
        }
    }

    // Game status display - minimal floating HUD
    SpatialPanel(
        modifier = SubspaceModifier
            .width(300.dp)
            .height(100.dp)
            .offset(x = 0.dp, y = 350.dp, z = -500.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.8f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TIME",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${gameTime.toInt()}s",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "LOOPS",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$loops",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Cyan
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ERRORS",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$incorrectPresses",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }
        }
    }

    // Exit button - top corner
    SpatialPanel(
        modifier = SubspaceModifier
            .width(100.dp)
            .height(60.dp)
            .offset(x = -400.dp, y = 300.dp, z = -600.dp)
    ) {
        Button(
            onClick = onBackToMenu,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.8f)
            )
        ) {
            Text(
                text = "EXIT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Level1Game(
    onLevelComplete: (loops: Int, totalTime: Float, incorrectPresses: Int) -> Unit,
    onBackToMenu: () -> Unit
) {
    Subspace {
        Level1GameContent(onLevelComplete, onBackToMenu)
    }
}

// Generate random 3D positions for buttons in a sphere around the user
private fun generateRandomButtons(): List<FloatingButton> {
    val buttons = mutableListOf<FloatingButton>()
    val correctButtonIndex = Random.nextInt(5) // 5 buttons total, one is correct
    
    for (i in 0 until 5) {
        // Spherical coordinates for natural 3D distribution
        val theta = Random.nextFloat() * 2 * PI // Azimuth (0 to 2π)
        val phi = Random.nextFloat() * PI * 0.6f + PI * 0.2f // Elevation (limited range for comfort)
        val distance = Random.nextFloat() * 400 + 400 // 400-800 units away
        
        val x = (distance * sin(phi) * cos(theta)).toFloat()
        val y = (distance * cos(phi) - 100).toFloat() // Slightly below eye level
        val z = -(distance * sin(phi) * sin(theta)).toFloat() - 300 // In front of user
        
        buttons.add(
            FloatingButton(
                id = i,
                x = x,
                y = y,
                z = z,
                color = generateRandomColor(),
                shape = ButtonShape.values()[Random.nextInt(ButtonShape.values().size)],
                isCorrect = i == correctButtonIndex
            )
        )
    }
    
    return buttons
}

private fun generateRandomColor(): Color {
    val colors = listOf(
        Color(0xFF2196F3), // Blue
        Color(0xFF4CAF50), // Green  
        Color(0xFFFF9800), // Orange
        Color(0xFF9C27B0), // Purple
        Color(0xFFE91E63), // Pink
        Color(0xFF00BCD4), // Cyan
        Color(0xFFFFEB3B), // Yellow
        Color(0xFF795548)  // Brown
    )
    return colors[Random.nextInt(colors.size)]
} 