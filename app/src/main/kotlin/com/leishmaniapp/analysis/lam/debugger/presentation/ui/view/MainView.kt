package com.leishmaniapp.analysis.lam.debugger.presentation.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.analysis.lam.R
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.composable.NotBoundScreen
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.ErrorAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.dialog.LoadingAlertDialog
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.MainState

@Composable
fun MainView(
    state: MainState,
    onErrorDismiss: () -> Unit,
    onBind: (model: String) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        when (state) {
            MainState.NotBound -> NotBoundScreen { model ->
                onBind.invoke(model)
            }

            is MainState.Bound, MainState.BusyBound -> LoadingAlertDialog(
                content = stringResource(
                    id = R.string.loading_lam
                )
            )

            is MainState.Error -> ErrorAlertDialog(
                throwable = state.e,
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
            onBind = {},
        )
    }
}

@Composable
@Preview
private fun MainViewPreview_Bound() {
    ApplicationTheme {
        MainView(
            state = MainState.Bound,
            onErrorDismiss = {},
            onBind = {},
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
            onBind = {},
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
            onBind = {},
        )
    }
}

