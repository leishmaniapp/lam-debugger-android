package com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme

@Composable
fun ExceptionAlertDialog(exception: Exception, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.action_ok))
            }
        },
        title = { Text(text = stringResource(id = R.string.error_title)) },
        text = { Text(text = exception.toString()) },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = stringResource(id = R.string.error_title)
            )
        }
    )
}

@Composable
@Preview
fun ExceptionAlertDialogPreview() {
    ApplicationTheme {
        ExceptionAlertDialog(
            exception = Exception("Lorem ipsum dolor sit amet Exception")
        ) {
        }
    }
}