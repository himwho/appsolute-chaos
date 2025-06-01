package com.example.android.appsolutechaos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialCurvedRow
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.movable
import androidx.xr.compose.subspace.layout.width
import com.example.android.appsolutechaos.ui.component.AppsoluteChaosTopAppBar
import com.example.android.appsolutechaos.ui.layout.CompactLayout
import com.example.android.appsolutechaos.ui.layout.ExpandedLayout
import com.example.android.appsolutechaos.game.*

@Composable
fun AppsoluteChaosApp(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val gameState = remember { GameState() }
    val currentLevel by gameState.currentLevel.collectAsState()
    val score by gameState.score.collectAsState()
    var showGame by remember { mutableStateOf(true) } // Start in game mode for Full Space

    if (showGame) {
        // Game Mode - Show XR Game
        when (currentLevel) {
            GameLevel.MENU -> {
                GameMenuScreen(
                    onStartLevel1 = { gameState.startLevel1() },
                    onStartLevel2 = { gameState.startLevel2() },
                    onExitGame = { 
                        showGame = false
                        gameState.resetGame()
                    }
                )
            }
            
            GameLevel.LEVEL_1 -> {
                Level1Game(
                    onLevelComplete = { loops, totalTime, incorrectPresses ->
                        gameState.completeLevel1(loops, totalTime, incorrectPresses)
                    },
                    onBackToMenu = { 
                        showGame = false
                        gameState.resetGame()
                    }
                )
            }
            
            GameLevel.LEVEL_2 -> {
                Level2Game(
                    onLevelComplete = { totalTime, incorrectPresses ->
                        gameState.completeLevel2(totalTime, incorrectPresses)
                    },
                    onBackToMenu = { 
                        showGame = false
                        gameState.resetGame()
                    }
                )
            }
            
            GameLevel.GAME_OVER -> {
                GameOverScreen(
                    score = score,
                    onRestartGame = { gameState.startLevel1() },
                    onBackToMenu = { 
                        showGame = false
                        gameState.resetGame()
                    }
                )
            }
        }
    } else {
        // Traditional UI disabled for XR-first experience
        // TODO: Restore traditional UI if needed in the future
        /*
        // Default Mode - Show Traditional 2D UI
        Scaffold(
            topBar = { AppsoluteChaosTopAppBar() }
        ) { innerPadding ->

            val modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

            if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                CompactLayout(
                    modifier = modifier,
                    primaryContent = {
                        PrimaryCard(
                            onLaunchGame = { showGame = true }
                        )
                    },
                    secondaryContent = {
                        SecondaryCardList()
                    }
                )
            } else {
                ExpandedLayout(
                    modifier = modifier,
                    primaryContent = {
                        PrimaryCard(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            onLaunchGame = { showGame = true }
                        )
                    },
                    secondaryContent = {
                        SecondaryCardList(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        )
                    }
                )
            }
        }
        
        // Subspace content for XR features
        Subspace {
            SpatialCurvedRow(
                curveRadius = 825.dp
            ) {
                SpatialPanel(
                    modifier = SubspaceModifier
                        .width(1024.dp)
                        .height(800.dp)
                        .movable(true)
                ) {
                    Scaffold(
                        topBar = { AppsoluteChaosTopAppBar() }
                    ) { innerPadding ->
                        Box(Modifier.padding(innerPadding)) {
                            PrimaryCard(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                                onLaunchGame = { showGame = true }
                            )
                        }
                    }
                }
                SpatialPanel(
                    modifier = SubspaceModifier
                        .width(340.dp)
                        .height(800.dp)
                        .movable(true)
                ) {
                    Surface {
                        SecondaryCardList(
                            modifier = Modifier
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        )
                    }
                }
            }
        }
        */
    }
} 