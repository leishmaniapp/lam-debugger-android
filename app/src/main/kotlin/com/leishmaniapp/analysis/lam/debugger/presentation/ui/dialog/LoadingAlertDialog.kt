package com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme

/**
 * Show a non-dismissible AlertDialog when the device is busy
 */
@Composable
fun LoadingAlertDialog(content: String = stringResource(id = R.string.loading_content)) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        title = { Text(text = stringResource(id = R.string.loading_title)) },
        text = { Text(text = content) },
        icon = { CircularProgressIndicator() }
    )
}

@Composable
@Preview
fun LoadingAlertDialogPreview() {
    ApplicationTheme {
        LoadingAlertDialog()
    }
}