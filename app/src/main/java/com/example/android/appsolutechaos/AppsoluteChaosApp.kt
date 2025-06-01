package com.example.android.appsolutechaos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
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
import com.example.android.appsolutechaos.ui.component.PrimaryCard
import com.example.android.appsolutechaos.ui.component.SecondaryCardList
import com.example.android.appsolutechaos.ui.component.AppsoluteChaosTopAppBar
import com.example.android.appsolutechaos.ui.layout.CompactLayout
import com.example.android.appsolutechaos.ui.layout.ExpandedLayout

@Composable
fun AppsoluteChaosApp(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
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
                    PrimaryCard()
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
                        modifier = Modifier.verticalScroll(rememberScrollState())
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
    Subspace {
        SpatialCurvedRow(
            curveRadius = 825.dp
        ) {
            SpatialPanel(
                modifier = SubspaceModifier
                    .width(1024.dp)
                    .height(800.dp)
            ) {
                Scaffold(
                    topBar = { AppsoluteChaosTopAppBar() }
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        PrimaryCard(
                            modifier = Modifier
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
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
} 