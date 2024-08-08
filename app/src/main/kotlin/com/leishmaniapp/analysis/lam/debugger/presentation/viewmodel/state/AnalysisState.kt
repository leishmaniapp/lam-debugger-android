package com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AnalysisState : Parcelable {
    data object None : AnalysisState()
    data class Error(val e: Throwable) : AnalysisState()
}