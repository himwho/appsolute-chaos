package com.example.android.appsolutechaos.game

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.offset
import androidx.xr.compose.subspace.layout.width
import com.example.android.appsolutechaos.ui.component.GameMenuPanel
import com.example.android.appsolutechaos.ui.component.GameDescriptionPanel
import com.example.android.appsolutechaos.ui.component.GameInstructionsPanel
import com.example.android.appsolutechaos.ui.component.GameOverPanel

@Composable
fun GameMenuScreen(
    onStartLevel1: () -> Unit,
    onStartLevel2: () -> Unit,
    onExitGame: () -> Unit
) {
    Subspace {
        // Main menu panel
        SpatialPanel(
            modifier = SubspaceModifier
                .width(500.dp)
                .height(400.dp)
                .offset(x = 0.dp, y = 0.dp, z = -800.dp)
        ) {
            GameMenuPanel(
                onStartLevel1 = onStartLevel1,
                onStartLevel2 = onStartLevel2,
                onExitGame = onExitGame
            )
        }

        // Game description panel
        SpatialPanel(
            modifier = SubspaceModifier
                .width(400.dp)
                .height(300.dp)
                .offset(x = 600.dp, y = 0.dp, z = -600.dp)
        ) {
            GameDescriptionPanel()
        }

        // Instructions panel
        SpatialPanel(
            modifier = SubspaceModifier
                .width(400.dp)
                .height(300.dp)
                .offset(x = -600.dp, y = 0.dp, z = -600.dp)
        ) {
            GameInstructionsPanel()
        }
    }
}

@Composable
fun GameOverScreen(
    score: GameScore,
    onRestartGame: () -> Unit,
    onBackToMenu: () -> Unit
) {
    Subspace {
        SpatialPanel(
            modifier = SubspaceModifier
                .width(500.dp)
                .height(400.dp)
                .offset(x = 0.dp, y = 0.dp, z = -800.dp)
        ) {
            GameOverPanel(
                score = score,
                onRestartGame = onRestartGame,
                onBackToMenu = onBackToMenu
            )
        }
    }
} 