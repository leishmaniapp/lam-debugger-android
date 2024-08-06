package com.leishmaniapp.analysis.lam.debugger.presentation.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable.BoundScreen
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable.NotBoundScreen
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.ExceptionAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.LoadingAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.MainState

@Composable
fun MainView(
    state: MainState,
    onErrorDismiss: () -> Unit,
) {
    Box {
        when (state) {
            MainState.NotBound -> NotBoundScreen()
            is MainState.Bound -> BoundScreen()

            MainState.BusyBound -> LoadingAlertDialog(content = stringResource(id = R.string.loading_lam))
            is MainState.Error -> ExceptionAlertDialog(
                exception = state.e,
                onDismiss = onErrorDismiss
            )
        }
    }
}

@Composable
@Preview
private fun MainViewPreview_NotBound() {
    ApplicationTheme {
        MainView(
            state = MainState.NotBound,
            onErrorDismiss = {},

            )
    }
}

@Composable
@Preview
private fun MainViewPreview_Bound() {
    ApplicationTheme {
        MainView(
            state = MainState.Bound("com.example"),
            onErrorDismiss = {},

            )
    }
}

@Composable
@Preview
private fun MainViewPreview_BusyBound() {
    ApplicationTheme {
        MainView(
            state = MainState.BusyBound,
            onErrorDismiss = {},

            )
    }
}

@Composable
@Preview
private fun MainViewPreview_Error() {
    ApplicationTheme {
        MainView(
            state = MainState.Error(Exception("Lorem ipsum dolor sit Exception")),
            onErrorDismiss = {},

            )
    }
}

