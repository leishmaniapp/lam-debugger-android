package com.leishmaniapp.analysis.lam.debugger.presentation.ui.view

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PowerOff
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.analysis.core.AnalysisResultsParcel
import com.leishmaniapp.analysis.core.AnalysisStatus
import com.leishmaniapp.analysis.core.CartesianCoordinatesParcel
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable.AnalysisDataSelection
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.ErrorAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.LoadingAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.AnalysisState
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisView(
    state: AnalysisState,
    onDismiss: () -> Unit,
    onUnbind: () -> Unit,
    onAnalyze: (file: Uri, diagnosis: UUID, sample: Int) -> Unit,
    data: List<AnalysisResultsParcel>,
) {

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
                    AnalysisState.None -> AnalysisDataSelection(
                        onAnalyze = onAnalyze,
                    )

                    is AnalysisState.Error -> ErrorAlertDialog(
                        throwable = state.e,
                        onDismiss = onDismiss
                    )

                    AnalysisState.AwaitData -> LoadingAlertDialog()

                    AnalysisState.HasData -> Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                    ) {

                        Text(
                            text = stringResource(id = R.string.bound_service_results),
                            style = MaterialTheme.typography.titleMedium
                        )

                        ElevatedButton(onClick = onDismiss) {
                            Text(text = stringResource(id = R.string.action_new_request))
                        }

                        LazyColumn {
                            items(data) { results ->
                                Card {
                                    Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = results.toString()
                                    )
                                }
                            }
                        }
                    }
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
            onDismiss = {},
            onUnbind = {},
            onAnalyze = { _, _, _ -> },
            data = listOf()
        )
    }
}

@Composable
@Preview
private fun AnalysisViewPreview_HasData() {
    ApplicationTheme {
        AnalysisView(
            state = AnalysisState.HasData,
            onDismiss = {},
            onUnbind = {},
            onAnalyze = { _, _, _ -> },
            data = listOf(
                AnalysisResultsParcel(
                    model = "lorem.ipsum",
                    status = AnalysisStatus.OK,
                    results = mapOf(
                        "a" to listOf(
                            CartesianCoordinatesParcel(10, 20),
                            CartesianCoordinatesParcel(20, 30),
                        )
                    )
                )
            )
        )
    }
}