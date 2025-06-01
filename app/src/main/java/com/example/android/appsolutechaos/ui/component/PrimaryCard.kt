package com.example.android.appsolutechaos.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.appsolutechaos.R
import com.example.android.appsolutechaos.ui.theme.AppsoluteChaosTheme

@Composable
fun PrimaryCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.primary_title),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                stringResource(R.string.primary_description),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun PrimaryCardPreview() {
    AppsoluteChaosTheme {
        PrimaryCard()
    }
}