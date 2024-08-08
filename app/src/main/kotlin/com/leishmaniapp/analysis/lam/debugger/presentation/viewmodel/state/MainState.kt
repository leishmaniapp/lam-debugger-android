package com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state

import android.os.Parcelable
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.MainViewModel
import kotlinx.parcelize.Parcelize

/**
 * Represents the state of the [MainViewModel]
 */
@Parcelize
sealed class MainState : Parcelable {

    /**
     * No LAM module has been bounded
     */
    data object NotBound : MainState()

    /**
     * A LAM module is bounded
     */
    data class Bound(val model: String) : MainState()

    /**
     * LAM module bound/unbound in progress
     */
    data object BusyBound : MainState()

    /**
     * An error message must be shown
     */
    data class Error(val e: Throwable) : MainState()
}