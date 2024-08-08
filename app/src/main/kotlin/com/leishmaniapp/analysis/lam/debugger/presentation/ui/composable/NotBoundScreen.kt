package com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme

/**
 * Show a screen input for the model name
 */
@Composable
fun NotBoundScreen(tryBind: (model: String) -> Unit) {

    var modelInput: String by remember {
        mutableStateOf("")
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp, vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
            ) {
                Icon(
                    painterResource(id = R.drawable.icon_monochrome),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Text(
                text = stringResource(id = R.string.app_description),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )

            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = stringResource(id = R.string.app_detail),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                softWrap = true,
            )

            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            ) {
                OutlinedTextField(
                    singleLine = true,
                    value = modelInput,
                    placeholder = {
                        Text(text = stringResource(id = R.string.not_bound_model_placeholder))
                    },
                    onValueChange = { value ->
                        modelInput = value
                    })

                Button(onClick = { tryBind.invoke(modelInput) }) {
                    Text(text = stringResource(id = R.string.not_bound_try_bind))
                }
            }
        }
    }
}

@Composable
@Preview
fun NotBoundScreenPreview() {
    ApplicationTheme {
        NotBoundScreen {}
    }
}