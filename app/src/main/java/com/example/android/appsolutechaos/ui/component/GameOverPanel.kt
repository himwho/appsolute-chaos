package com.example.android.appsolutechaos.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.appsolutechaos.game.GameScore
import com.example.android.appsolutechaos.ui.theme.AppsoluteChaosTheme

@Composable
fun GameOverPanel(
    score: GameScore,
    onRestartGame: () -> Unit,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "GAME COMPLETE!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700), // Gold
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "FINAL SCORE",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Cyan
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ScoreDisplay(score)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onRestartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green
                )
            ) {
                Text(
                    text = "PLAY AGAIN",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onBackToMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text(
                    text = "MAIN MENU",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ScoreDisplay(score: GameScore) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ScoreItem("Levels", "${score.level}")
            ScoreItem("Total Time", "${score.totalTime.toInt()}s")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ScoreItem("Loops", "${score.loops}")
            ScoreItem("Wrong Presses", "${score.incorrectPresses}")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val finalScore = calculateFinalScore(score)
        Text(
            text = "FINAL SCORE: $finalScore",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700) // Gold
        )
    }
}

@Composable
private fun ScoreItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

private fun calculateFinalScore(score: GameScore): Int {
    // Lower score is better
    // Base score from time, penalties for loops and wrong presses
    val timeScore = score.totalTime.toInt()
    val loopPenalty = score.loops * 10
    val wrongPressPenalty = score.incorrectPresses * 25
    
    return timeScore + loopPenalty + wrongPressPenalty
}

@Preview
@Composable
fun GameOverPanelPreview() {
    AppsoluteChaosTheme {
        GameOverPanel(
            score = GameScore(
                level = 2,
                loops = 5,
                totalTime = 120.5f,
                incorrectPresses = 3
            ),
            onRestartGame = { },
            onBackToMenu = { }
        )
    }
} 