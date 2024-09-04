package com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.ErrorAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme
import java.util.Random
import java.util.UUID

@Composable
fun AnalysisDataSelection(
    onAnalyze: (file: Uri, diagnosis: UUID, sample: Int) -> Unit,
) {

    var showErrorDialog: Boolean by remember {
        mutableStateOf(false)
    }

    var fileUri: Uri? by remember {
        mutableStateOf(null)
    }

    var diagnosisUuidInput: String? by remember {
        mutableStateOf(null)
    }

    var sampleNumberInput: String? by remember {
        mutableStateOf(null)
    }

    val pickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
            fileUri = uri
        }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),

            ) {

            Text(
                text = stringResource(id = R.string.bound_service_image_title),
                style = MaterialTheme.typography.headlineSmall,
            )

            Card {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = fileUri?.toString()
                        ?: stringResource(id = R.string.bound_service_no_file)
                )
            }

            ElevatedButton(onClick = {
                pickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Text(text = stringResource(id = R.string.action_pick))
            }

            HorizontalDivider()

            Text(
                text = stringResource(id = R.string.bound_service_metadata_title),
                style = MaterialTheme.typography.headlineSmall,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = diagnosisUuidInput ?: "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrect = false
                ),
                onValueChange = { value -> diagnosisUuidInput = value },
                placeholder = { Text(text = stringResource(id = R.string.bound_service_input_diagnosis)) },
                singleLine = true,
                trailingIcon = {
                    FilledIconButton(
                        onClick = { diagnosisUuidInput = UUID.randomUUID().toString() },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(id = R.string.bound_service_refresh)
                        )
                    }
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = sampleNumberInput?.toString() ?: "",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { value -> sampleNumberInput = value },
                placeholder = { Text(text = stringResource(id = R.string.bound_service_input_sample)) },
                trailingIcon = {
                    FilledIconButton(onClick = {
                        sampleNumberInput = (Random().nextDouble() * 100).toInt().toString()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(id = R.string.bound_service_refresh)
                        )
                    }
                }
            )

            Button(onClick = {
                try {
                    onAnalyze.invoke(
                        fileUri!!,
                        UUID.fromString(diagnosisUuidInput!!),
                        sampleNumberInput!!.toInt(),
                    )
                } catch (e: Exception) {
                    showErrorDialog = true
                }
            }) {
                Text(text = stringResource(id = R.string.bound_service_analyze))
            }
        }

        if (showErrorDialog) {
            ErrorAlertDialog(
                throwable = Exception("Invalid input fields, all fields are required")
            ) {
                showErrorDialog = false
            }
        }
    }
}

@Composable
@Preview
fun AnalysisDataSelectionPreview() {
    ApplicationTheme {
        Surface {
            AnalysisDataSelection { _, _, _ -> }
        }
    }
}