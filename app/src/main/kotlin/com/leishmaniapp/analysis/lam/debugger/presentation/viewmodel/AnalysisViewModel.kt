package com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Message
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.leishmaniapp.analysis.core.AnalysisResultsParcel
import com.leishmaniapp.analysis.lam.debugger.domain.services.ILamConnectionService
import com.leishmaniapp.analysis.lam.debugger.presentation.viewmodel.state.AnalysisState
import com.leishmaniapp.analysis.lam.LamAnalysisRequest
import com.leishmaniapp.analysis.lam.LamAnalysisResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(

    savedStateHandle: SavedStateHandle,

    private val lamConnectionService: ILamConnectionService,

    ) : ViewModel() {

    companion object {
        val TAG = AnalysisViewModel::class.simpleName!!
    }

    private val _state: MutableLiveData<AnalysisState> =
        savedStateHandle.getLiveData("state", AnalysisState.None)

    val state: MutableLiveData<AnalysisState> = _state

    fun dismissState() {
        _state.value = AnalysisState.None
        _results.value = mutableListOf()
    }

    private val _results: MutableLiveData<List<AnalysisResultsParcel>> =
        savedStateHandle.getLiveData("results", mutableListOf())

    val results: LiveData<List<AnalysisResultsParcel>> = _results.map { it.toList() }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            lamConnectionService.replyChannel.consumeAsFlow().collect { r ->
                Log.i(TAG, "Added result ($r)")
                withContext(Dispatchers.Main) {
                    _results.value = listOf(r.results)
                    _state.value = AnalysisState.HasData
                }
            }
        }
    }

    fun analyze(context: Context, file: Uri, diagnosis: UUID, sample: Int) = try {

        // Set the state
        _state.value = AnalysisState.AwaitData

        // Copy file
        val cacheFile = File.createTempFile(
            "${sample}_${diagnosis}_",
            file.path.let { p -> p!!.substring(p.lastIndexOf(".")) },
            context.cacheDir
        )

        // Copy from origin to cacheFile
        context.contentResolver.openInputStream(file)?.use { ins ->
            FileOutputStream(cacheFile).use { outs ->
                ins.copyTo(outs)
            }
        }

        val fileUri = FileProvider.getUriForFile(
            context,
            "com.leishmaniapp.analysis.lam.debugger.provider",
            cacheFile
        )

        // Create the request
        val request = LamAnalysisRequest(
            diagnosis = diagnosis,
            sample = sample,
            uri = fileUri,
        )

        // Send to service
        lamConnectionService.trySend(request)
        Log.i(TAG, "Shared message with LAM: $request")

    } catch (e: Exception) {

        Log.e(TAG, "Failed to send message to LAM", e)
        _state.value = AnalysisState.Error(e)
    }
}