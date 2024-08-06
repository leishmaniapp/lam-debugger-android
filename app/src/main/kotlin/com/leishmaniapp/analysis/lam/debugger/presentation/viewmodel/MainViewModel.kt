package com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leishmaniapp.analysis.lam.debugger.domain.services.ILamConnectionService
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

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
}