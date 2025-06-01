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

@Composable
fun TestLevel(
    onBackToMenu: () -> Unit
) {
    // Match GameMenuScreen's EXACT 3-panel structure
    Subspace {
        // Main menu panel
        SpatialPanel(
            modifier = SubspaceModifier
                .width(500.dp)
                .height(400.dp)
                .offset(x = 0.dp, y = 0.dp, z = -800.dp)
        ) {
            GameMenuPanel(
                onStartLevel1 = { /* Do nothing */ },
                onStartLevel2 = { /* Do nothing */ },
                onExitGame = onBackToMenu
            )
        }

        // Game description panel (exactly like GameMenuScreen)
        SpatialPanel(
            modifier = SubspaceModifier
                .width(400.dp)
                .height(300.dp)
                .offset(x = 600.dp, y = 0.dp, z = -600.dp)
        ) {
            GameDescriptionPanel()
        }

        // Instructions panel (exactly like GameMenuScreen)
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