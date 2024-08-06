package com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state

import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.MainViewModel

/**
 * Represents the state of the [MainViewModel]
 */
sealed class MainState {

    /**
     * No LAM module has been bounded
     */
    data object NotBound : MainState()

    /**
     * A LAM module is bounded
     */
    data class Bound(val lamPackage: String) : MainState()

    /**
     * LAM module bound/unbound in progress
     */
    data object BusyBound : MainState()

    /**
     * An error message must be shown
     */
    data class Error(val e: Exception) : MainState()
}