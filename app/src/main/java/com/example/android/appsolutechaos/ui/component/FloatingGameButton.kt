package com.example.android.appsolutechaos.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.appsolutechaos.game.ButtonShape
import com.example.android.appsolutechaos.game.FloatingButton
import com.example.android.appsolutechaos.ui.theme.AppsoluteChaosTheme

@Composable
fun FloatingGameButton(
    button: FloatingButton,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonModifier = when (button.shape) {
        ButtonShape.CIRCLE -> modifier.then(Modifier.size(100.dp))
        ButtonShape.SQUARE -> modifier.then(Modifier.size(100.dp))
        ButtonShape.ROUNDED_RECTANGLE -> modifier.then(Modifier.size(120.dp, 80.dp))
    }

    Button(
        onClick = onClick,
        modifier = buttonModifier,
        shape = when (button.shape) {
            ButtonShape.CIRCLE -> CircleShape
            ButtonShape.SQUARE -> RoundedCornerShape(4.dp)
            ButtonShape.ROUNDED_RECTANGLE -> RoundedCornerShape(16.dp)
        },
        colors = ButtonDefaults.buttonColors(containerColor = button.color)
    ) {
        Text(
            text = if (button.isCorrect) "✓" else "✗",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun FloatingGameButtonPreview() {
    AppsoluteChaosTheme {
        FloatingGameButton(
            button = FloatingButton(
                id = 0,
                x = 0f,
                y = 0f,
                z = 0f,
                color = Color.Blue,
                shape = ButtonShape.CIRCLE,
                isCorrect = true
            ),
            onClick = { }
        )
    }
} 