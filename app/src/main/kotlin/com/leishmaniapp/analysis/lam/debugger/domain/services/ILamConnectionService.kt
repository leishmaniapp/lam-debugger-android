package com.leishmaniapp.analysis.lam.debugger.domain.services

import android.content.Context
import android.os.Message
import com.leishmaniapp.analysis.lam.service.LamAnalysisRequest
import com.leishmaniapp.analysis.lam.service.LamAnalysisResponse
import kotlinx.coroutines.channels.Channel

/**
 * Service for connecting to a single LAM module
 */
interface ILamConnectionService {

    /**
     * Attempt to bind to a LAM service in another process
     */
    fun tryBind(context: Context, model: String): Result<Unit>

    /**
     * Attempt to unbind the currently bound LAM service
     */
    fun tryUnbind(context: Context): Result<Unit>

    /**
     * Send a message to the LAM
     */
    fun trySend(request: LamAnalysisRequest): Result<Unit>

    /**
     * Channel from which responses are sent
     */
    val replyChannel: Channel<LamAnalysisResponse>
}