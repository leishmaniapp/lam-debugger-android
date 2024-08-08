package com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.AnalysisState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(

    savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    private val _state: MutableLiveData<AnalysisState> =
        savedStateHandle.getLiveData("state", AnalysisState.None)

    val state: MutableLiveData<AnalysisState> = _state
}