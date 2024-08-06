package com.leishmaniapp.analysis.lam.debugger.presentation.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.LoadingAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme

/**
 * Show a non-dismissible AlertDialog in the center of the screen when the device is busy
 */
@Composable
fun LoadingView() {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LoadingAlertDialog()
        }
    }
}

@Composable
@Preview
fun LoadingViewPreview() {
    ApplicationTheme {
        LoadingView()
    }
}


