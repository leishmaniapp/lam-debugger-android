package com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leishmaniapp.analysis.lam.debugger.domain.services.ILamConnectionService
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Handle the application main state
 */
@HiltViewModel
class MainViewModel @Inject constructor(

    @ApplicationContext
    private val context: Context,
    private val savedStateHandle: SavedStateHandle,

    /**
     * Service for connecting with LAM in another process
     */
    private val lamConnectionService: ILamConnectionService,

    ) : ViewModel() {

    companion object {
        /**
         * TAG for using with [Log]
         */
        val TAG: String = MainViewModel::class.simpleName!!
    }

    /**
     * Application state backing field
     */
    private val _state: MutableLiveData<MainState> =
        savedStateHandle.getLiveData("state", MainState.NotBound)

    /**
     * Current application state
     */
    val state: LiveData<MainState> = _state

    /**
     * Restart the state back to [MainState.NotBound]
     */
    fun dismissState() =
        viewModelScope.launch {
            _state.value = MainState.NotBound
        }

    /**
     * Bind a LAM module service from a different process
     */
    fun bindService(model: String) =
        viewModelScope.launch {
            // Set the state to busy
            _state.value = MainState.BusyBound

            // Bind the service
            withContext(Dispatchers.Default) {
                lamConnectionService.tryBind(context, model)
            }.fold(
                onSuccess = {
                    // Service is now bounded
                    Log.i(TAG, "Successfully bound service for ($model)")
                    _state.value = MainState.Bound(model)
                },
                onFailure = { err ->
                    // Failed to bound the service
                    Log.e(TAG, "Failed to bind a service for model ($model)", err)
                    _state.value = MainState.Error(err)
                }
            )
        }
}