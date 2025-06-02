package com.example.android.appsolutechaos.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

data class KeypadPosition(
    val x: Float,
    val y: Float,
    val z: Float
)

data class NumberHint(
    val digit: Int,
    val position: Int, // 0-5 for 6 digit code
    val x: Float,
    val y: Float,
    val z: Float,
    val timeRemaining: Float
)

data class SoundCue(
    val x: Float,
    val y: Float,
    val z: Float,
    val isActive: Boolean
)

data class ResetWindow(
    val x: Float,
    val y: Float,
    val z: Float,
    val id: Int
)

@Composable
fun Level2GameContent(
    onLevelComplete: (totalTime: Float, incorrectPresses: Int) -> Unit,
    onBackToMenu: () -> Unit
) {
    var gameTime by remember { mutableStateOf(0f) }
    var incorrectPresses by remember { mutableStateOf(0) }
    var soundCueTimer by remember { mutableStateOf(3f) }
    var currentCode by remember { mutableStateOf(listOf<Int>()) }
    var targetCode by remember { mutableStateOf(generateTargetCode()) }
    var showKeypad by remember { mutableStateOf(false) }
    var keypadPosition by remember { mutableStateOf(generateKeypadPosition()) }
    var keypadFadeStartTime by remember { mutableStateOf(-1f) }
    var keypadAlpha by remember { mutableStateOf(1f) }
    var numberHints by remember { mutableStateOf(listOf<NumberHint>()) }
    var resetWindows by remember { mutableStateOf(listOf<ResetWindow>()) }
    var currentSoundCue by remember { mutableStateOf<SoundCue?>(null) }
    
    // Game timer and logic
    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            gameTime += 0.1f
            soundCueTimer -= 0.1f
            
            // Update number hints
            numberHints = numberHints.map { hint ->
                hint.copy(timeRemaining = hint.timeRemaining - 0.1f)
            }.filter { it.timeRemaining > 0f }
            
            // Handle keypad fading
            if (keypadFadeStartTime > 0) {
                val fadeElapsed = gameTime - keypadFadeStartTime
                keypadAlpha = (1f - fadeElapsed / 10f).coerceAtLeast(0f)
                
                if (fadeElapsed >= 10f) {
                    showKeypad = false
                    resetWindows = List(3) { generateResetWindow(it) }
                    keypadFadeStartTime = -1f
                }
            }
            
            // Generate sound cue
            if (soundCueTimer <= 0f && !showKeypad) {
                currentSoundCue = generateSoundCue()
                soundCueTimer = Random.nextFloat() * 3f + 2f
                
                // Auto-spawn keypad after sound cue (simulated)
                delay(1000)
                showKeypad = true
                keypadPosition = generateKeypadPosition()
                keypadAlpha = 1f
                keypadFadeStartTime = gameTime + 10f
                currentSoundCue = null
                
                // Generate number hints
                targetCode.forEachIndexed { index, digit ->
                    delay((Random.nextFloat() * 2000).toLong())
                    numberHints = numberHints + generateNumberHint(digit, index)
                }
            }
            
            // Check win condition
            if (currentCode == targetCode) {
                onLevelComplete(gameTime, incorrectPresses)
                return@LaunchedEffect
            }
        }
    }

    // Content within existing Subspace
    // Floating 3D keypad - no panel background, just buttons
    if (showKeypad) {
        // Current code display
        SpatialPanel(
            modifier = SubspaceModifier
                .width(300.dp)
                .height(60.dp)
                .offset(
                    x = keypadPosition.x.dp,
                    y = (keypadPosition.y + 120).dp,
                    z = keypadPosition.z.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(keypadAlpha)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentCode.joinToString("") + "_".repeat(6 - currentCode.size),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 4.sp
                )
            }
        }
        
        // 3D floating keypad buttons
        for (row in 0 until 4) {
            for (col in 0 until 3) {
                val buttonIndex = row * 3 + col
                val digit = when (buttonIndex) {
                    in 0..8 -> buttonIndex + 1
                    9 -> -1 // Clear button
                    10 -> 0
                    11 -> -2 // Empty slot
                    else -> -2
                }
                
                if (digit >= 0 || digit == -1) { // Valid button
                    val buttonX = keypadPosition.x + (col - 1) * 70f
                    val buttonY = keypadPosition.y - row * 70f + 40f
                    val buttonZ = keypadPosition.z
                    
                    SpatialPanel(
                        modifier = SubspaceModifier
                            .width(60.dp)
                            .height(60.dp)
                            .offset(
                                x = buttonX.dp,
                                y = buttonY.dp,
                                z = buttonZ.dp
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .alpha(keypadAlpha)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray.copy(alpha = 0.8f))
                                .clickable {
                                    if (digit == -1) {
                                        currentCode = emptyList()
                                    } else if (currentCode.size < 6) {
                                        currentCode = currentCode + digit
                                        if (currentCode.size == 6 && currentCode != targetCode) {
                                            incorrectPresses++
                                            currentCode = emptyList()
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (digit == -1) "C" else digit.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    // Floating number hints as pure 3D objects
    numberHints.forEach { hint ->
        SpatialPanel(
            modifier = SubspaceModifier
                .width(80.dp)
                .height(80.dp)
                .offset(
                    x = hint.x.dp,
                    y = hint.y.dp,
                    z = hint.z.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(40.dp)) // Circular
                    .background(
                        Color.Yellow.copy(
                            alpha = (hint.timeRemaining / 1.5f).coerceIn(0.3f, 1f)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = hint.digit.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Pos ${hint.position + 1}",
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }

    // Reset buttons as floating 3D objects
    resetWindows.forEach { resetWindow ->
        SpatialPanel(
            modifier = SubspaceModifier
                .width(100.dp)
                .height(60.dp)
                .offset(
                    x = resetWindow.x.dp,
                    y = resetWindow.y.dp,
                    z = resetWindow.z.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF4CAF50)) // Green
                    .clickable {
                        keypadFadeStartTime = -1f
                        keypadAlpha = 1f
                        resetWindows = emptyList()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "RESET",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    // Sound cue indicators as glowing orbs
    currentSoundCue?.let { cue ->
        SpatialPanel(
            modifier = SubspaceModifier
                .width(100.dp)
                .height(100.dp)
                .offset(
                    x = cue.x.dp,
                    y = cue.y.dp,
                    z = cue.z.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.Cyan.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "♪",
                    fontSize = 32.sp,
                    color = Color.White
                )
            }
        }
    }

    // Game status HUD
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CODE: ${targetCode.joinToString("")}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
                Text(
                    text = "Errors: $incorrectPresses | Time: ${gameTime.toInt()}s",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }

    // Exit button
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
fun Level2Game(
    onLevelComplete: (totalTime: Float, incorrectPresses: Int) -> Unit,
    onBackToMenu: () -> Unit
) {
    Subspace {
        Level2GameContent(onLevelComplete, onBackToMenu)
    }
}

// Generate random 3D position for keypad
private fun generateRandomPosition(): FloatingButton {
    val angle = Random.nextFloat() * 2 * PI
    val distance = Random.nextFloat() * 300 + 500 // 500-800 units away
    return FloatingButton(
        id = 0,
        x = (distance * cos(angle)).toFloat(),
        y = Random.nextFloat() * 100 - 50, // -50 to 50 height variation
        z = -(distance * sin(angle)).toFloat() - 400,
        color = Color.Transparent,
        shape = ButtonShape.SQUARE,
        isCorrect = false
    )
}

private fun generateSoundCue(): SoundCue {
    val angle = Random.nextFloat() * 2 * PI
    val distance = Random.nextFloat() * 400 + 400
    return SoundCue(
        x = (distance * cos(angle)).toFloat(),
        y = Random.nextFloat() * 200 - 100,
        z = -(distance * sin(angle)).toFloat() - 300,
        isActive = true
    )
}

private fun generateNumberHint(digit: Int, position: Int): NumberHint {
    val angle = Random.nextFloat() * 2 * PI
    val distance = Random.nextFloat() * 300 + 350 // Closer than other elements
    return NumberHint(
        digit = digit,
        position = position,
        x = (distance * cos(angle)).toFloat(),
        y = Random.nextFloat() * 200 - 100,
        z = -(distance * sin(angle)).toFloat() - 250,
        timeRemaining = 1.5f
    )
}

private fun generateResetWindow(id: Int): ResetWindow {
    val angle = Random.nextFloat() * 2 * PI
    val distance = Random.nextFloat() * 350 + 400
    return ResetWindow(
        x = (distance * cos(angle)).toFloat(),
        y = Random.nextFloat() * 150 - 75,
        z = -(distance * sin(angle)).toFloat() - 350,
        id = id
    )
}

private fun generateTargetCode(): List<Int> {
    return listOf(
        Random.nextInt(10),
        Random.nextInt(10),
        Random.nextInt(10),
        Random.nextInt(10),
        Random.nextInt(10),
        Random.nextInt(10)
    )
}

private fun generateKeypadPosition(): KeypadPosition {
    val angle = Random.nextFloat() * 2 * PI
    val distance = Random.nextFloat() * 400 + 400
    return KeypadPosition(
        x = (distance * cos(angle)).toFloat(),
        y = Random.nextFloat() * 200 - 100,
        z = -(distance * sin(angle)).toFloat() - 300
    )
} 