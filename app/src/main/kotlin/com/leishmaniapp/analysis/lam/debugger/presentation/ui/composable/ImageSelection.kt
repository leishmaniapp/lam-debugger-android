package com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme

@Composable
fun ImageSelection(
    onSelect: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ElevatedButton(onClick = onSelect) {
            Text(text = stringResource(id = R.string.action_pick))
        }
    }
}

@Composable
@Preview
fun ImageSelectionPreview() {
    ApplicationTheme {
        ImageSelection(
            onSelect = {},
        )
    }
}