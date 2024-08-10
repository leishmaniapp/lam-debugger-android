package com.leishmaniapp.analysis.lam.debugger.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.theme.ApplicationTheme
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.view.AnalysisView
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.view.LoadingView
import com.leishmaniapp.analysis.lam.debugger.presentation.ui.view.MainView
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.AnalysisViewModel
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.MainViewModel
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.MainState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val analysisViewModel: AnalysisViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            // Observe the application state
            val mainState by mainViewModel.state.observeAsState()
            val analysisState by analysisViewModel.state.observeAsState()

            // Get the context
            val context = LocalContext.current

            // Show loading or the main view
            ApplicationTheme {
                // Check for nullity on state
                if (mainState == null || analysisState == null) {
                    LoadingView()
                    return@ApplicationTheme
                }

                // Check the required screen
                if (mainState == MainState.Bound) {

                    val analysisData by analysisViewModel.results.observeAsState()

                    AnalysisView(
                        state = analysisState!!,
                        onDismiss = { analysisViewModel.dismissState() },
                        onUnbind = { mainViewModel.unbindService(context) },
                        onAnalyze = { uri, diagnosis, sample ->
                            analysisViewModel.analyze(context, uri, diagnosis, sample)
                        },
                        data = analysisData ?: listOf()
                    )

                } else {
                    MainView(
                        state = mainState!!,
                        onErrorDismiss = {
                            mainViewModel.dismissState()
                        },
                        onBind = { m ->
                            mainViewModel.bindService(m, context)
                        },
                    )
                }

            }
        }
    }
}