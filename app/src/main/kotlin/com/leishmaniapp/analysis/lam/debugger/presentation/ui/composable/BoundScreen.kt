package com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BoundScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.bound_service_title))
            })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {}
    }
}

@Composable
@Preview
fun BoundScreenPreview() {
    ApplicationTheme {
        BoundScreen()
    }
}