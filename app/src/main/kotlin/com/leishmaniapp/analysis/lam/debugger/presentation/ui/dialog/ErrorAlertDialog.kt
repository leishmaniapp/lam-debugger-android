package com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme

@Composable
fun ErrorAlertDialog(throwable: Throwable, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.action_ok))
            }
        },
        title = { Text(text = stringResource(id = R.string.error_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    8.dp, Alignment.CenterVertically,
                )
            ) {
                Text(
                    text = throwable::class.qualifiedName!!,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = throwable.message
                        ?: stringResource(id = R.string.error_unknown)
                )
            }
        },
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
fun ErrorAlertDialogPreview() {
    ApplicationTheme {
        ErrorAlertDialog(
            throwable = Exception("Lorem ipsum dolor sit amet Exception")
        ) {
        }
    }
}