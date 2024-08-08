package com.leishmaniapp.analysis.lam.debugger.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.view.LoadingView
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.view.MainView
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            // Observe the application state
            val state by mainViewModel.state.observeAsState()

            // Show loading or the main view
            ApplicationTheme {
                if (state == null) {
                    LoadingView()
                } else {
                    MainView(
                        state = state!!,
                        onErrorDismiss = {
                            mainViewModel.dismissState()
                        },
                        onBind = { m ->
                            mainViewModel.bindService(m)
                        }
                    )
                }
            }
        }
    }
}