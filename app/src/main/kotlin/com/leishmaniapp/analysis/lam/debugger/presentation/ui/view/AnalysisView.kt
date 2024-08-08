package com.leishmaniapp.analysis.lam.debugger.presentation.ui.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PowerOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable.ImageSelection
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable.NotBoundScreen
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.ErrorAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.LoadingAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.AnalysisState
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.MainState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisView(
    state: AnalysisState,
    onErrorDismiss: () -> Unit,
    onUnbind: () -> Unit,
    onImagePick: (Uri) -> Unit,
) {

    val pickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let(onImagePick)
        }

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = stringResource(id = R.string.bound_service_title))
            },
                actions = {
                    IconButton(onClick = onUnbind) {
                        Icon(
                            imageVector = Icons.Rounded.PowerOff,
                            contentDescription = stringResource(
                                id = R.string.action_disconnect
                            )
                        )
                    }
                }
            )
        },

        ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {


            Surface(modifier = Modifier.fillMaxSize()) {
                when (state) {
                    AnalysisState.None -> ImageSelection {
                        pickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }

                    is AnalysisState.Error -> ErrorAlertDialog(
                        throwable = state.e,
                        onDismiss = onErrorDismiss
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun AnalysisViewPreview_None() {
    ApplicationTheme {
        AnalysisView(
            state = AnalysisState.None,
            onErrorDismiss = {},
            onUnbind = {},
            onImagePick = {},
        )
    }
}