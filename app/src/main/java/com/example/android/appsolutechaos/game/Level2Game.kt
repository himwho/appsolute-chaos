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
fun Level2Game(
    onLevelComplete: (totalTime: Float, incorrectPresses: Int) -> Unit,
    onBackToMenu: () -> Unit
) {
    var gameTime by remember { mutableStateOf(0f) }
    var incorrectPresses by remember { mutableStateOf(0) }
    var currentCode by remember { mutableStateOf(mutableListOf<Int>()) }
    val targetCode by remember { mutableStateOf(generateTargetCode()) }
    var keypadPosition by remember { mutableStateOf(generateRandomPosition()) }
    var keypadAlpha by remember { mutableStateOf(1f) }
    var keypadFadeStartTime by remember { mutableStateOf(-1f) }
    var numberHints by remember { mutableStateOf(emptyList<NumberHint>()) }
    var soundCues by remember { mutableStateOf(emptyList<SoundCue>()) }
    var resetWindows by remember { mutableStateOf(emptyList<ResetWindow>()) }
    var showKeypad by remember { mutableStateOf(false) }
    var nextSoundTime by remember { mutableStateOf(2f) }
    var nextHintTime by remember { mutableStateOf(5f) }

    // Game timer and logic
    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            gameTime += 0.1f
            nextSoundTime -= 0.1f
            nextHintTime -= 0.1f
            
            // Remove expired number hints
            numberHints = numberHints.map { hint ->
                hint.copy(timeRemaining = hint.timeRemaining - 0.1f)
            }.filter { it.timeRemaining > 0 }
            
            // Trigger sound cues
            if (nextSoundTime <= 0f) {
                soundCues = listOf(generateSoundCue())
                showKeypad = true
                nextSoundTime = Random.nextFloat() * 3f + 2f // 2-5 seconds
            }
            
            // Trigger number hints
            if (nextHintTime <= 0f && showKeypad) {
                val hintPosition = Random.nextInt(6)
                val hintDigit = targetCode[hintPosition]
                numberHints = numberHints + generateNumberHint(hintDigit, hintPosition)
                nextHintTime = Random.nextFloat() * 2f + 1f // 1-3 seconds
            }
            
            // Handle keypad fading
            if (showKeypad && keypadFadeStartTime == -1f && gameTime > 10f) {
                keypadFadeStartTime = gameTime
            }
            
            if (keypadFadeStartTime > 0f) {
                val fadeProgress = (gameTime - keypadFadeStartTime) / 10f // 10 second fade
                keypadAlpha = 1f - fadeProgress.coerceIn(0f, 1f)
                
                // Spawn reset windows when fading starts
                if (resetWindows.isEmpty() && fadeProgress > 0.1f) {
                    resetWindows = listOf(generateResetWindow(0))
                }
                
                if (keypadAlpha <= 0f) {
                    // Game over condition - keypad completely faded
                    showKeypad = false
                }
            }
        }
    }

    Subspace {
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
                                    .background(
                                        if (digit == -1) Color(0xFFFF5722) else Color(0xFF424242)
                                    )
                                    .clickable {
                                        if (digit == -1) {
                                            // Clear button
                                            currentCode.clear()
                                        } else {
                                            if (currentCode.size < 6) {
                                                currentCode.add(digit)
                                                if (currentCode.size == 6) {
                                                    if (currentCode == targetCode) {
                                                        onLevelComplete(gameTime, incorrectPresses)
                                                    } else {
                                                        incorrectPresses++
                                                        currentCode.clear()
                                                    }
                                                }
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
        soundCues.forEach { cue ->
            if (cue.isActive) {
                SpatialPanel(
                    modifier = SubspaceModifier
                        .width(40.dp)
                        .height(40.dp)
                        .offset(
                            x = cue.x.dp,
                            y = cue.y.dp,
                            z = cue.z.dp
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Cyan.copy(alpha = 0.8f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♪",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Game status HUD
        SpatialPanel(
            modifier = SubspaceModifier
                .width(300.dp)
                .height(100.dp)
                .offset(x = 0.dp, y = 350.dp, z = -400.dp)
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
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "CODE",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "${currentCode.size}/6",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Cyan
                        )
                    }
                }
            }
        }

        // Exit button
        SpatialPanel(
            modifier = SubspaceModifier
                .width(100.dp)
                .height(60.dp)
                .offset(x = -400.dp, y = 300.dp, z = -500.dp)
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