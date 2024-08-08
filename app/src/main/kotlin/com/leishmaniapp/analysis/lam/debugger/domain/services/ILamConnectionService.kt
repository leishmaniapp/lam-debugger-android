package com.leishmaniapp.analysis.lam.debugger.domain.services

import android.content.Context

/**
 * Service for connecting to LAM modules
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
}