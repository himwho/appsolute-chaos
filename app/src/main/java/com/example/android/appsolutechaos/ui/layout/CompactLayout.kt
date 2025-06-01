package com.example.android.appsolutechaos.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CompactLayout(
    modifier: Modifier = Modifier,
    primaryContent: @Composable () -> Unit,
    secondaryContent: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        primaryContent()
        Spacer(modifier = Modifier.height(16.dp))
        secondaryContent()
    }
}