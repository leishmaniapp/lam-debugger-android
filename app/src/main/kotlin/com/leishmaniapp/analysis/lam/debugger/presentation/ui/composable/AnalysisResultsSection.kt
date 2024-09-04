package com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import com.leishmaniapp.analysis.core.BoxCoordinatesParcel
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnalysisResultsSection(
    data: List<AnalysisResultsParcel>,
    onDismiss: () -> Unit
) {
    Column(
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

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            items(data) { analysisResultsParcel ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                    ) {
                        OutlinedCard {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = analysisResultsParcel.status.toString()
                                )
                                Text(
                                    text = "(${analysisResultsParcel.model}@${analysisResultsParcel.version})"
                                )
                            }
                        }

                        analysisResultsParcel.results?.forEach { (t, u) ->
                            ElevatedCard {
                                Text(text = t, Modifier.padding(start = 8.dp, top = 4.dp))
                                Text(
                                    text = stringResource(
                                        id = R.string.bound_service_results_total,
                                        u.size
                                    ),
                                    Modifier.padding(start = 8.dp, top = 4.dp)
                                )
                                FlowRow(modifier = Modifier.padding(4.dp)) {
                                    u.forEach { cords ->
                                        Card(modifier = Modifier.padding(4.dp)) {
                                            Text(
                                                modifier = Modifier.padding(4.dp),
                                                text = cords.toString()
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
    }
}

@Composable
@Preview
fun AnalysisResultsSectionPreview() {
    ApplicationTheme {
        Surface {
            AnalysisResultsSection(
                data = listOf(
                    AnalysisResultsParcel(
                        model = "lorem.ipsum",
                        status = AnalysisStatus.OK,
                        version = "Example LAM",
                        results = mapOf(
                            "a" to listOf(
                                BoxCoordinatesParcel(10, 20, 0, 0),
                                BoxCoordinatesParcel(20, 30, 0, 0),
                            )
                        )
                    ),
                    AnalysisResultsParcel(
                        model = "lorem.ipsum",
                        status = AnalysisStatus.OK,
                        version = "Example LAM",
                        results = mapOf(
                            "a" to listOf(
                                BoxCoordinatesParcel(10, 20, 0, 0),
                                BoxCoordinatesParcel(20, 30, 0, 0),
                            )
                        )
                    )
                )
            ) {}
        }
    }
}

