package com.leishmaniapp.analysis.lam.debugger.infrastructure.lam

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.leishmaniapp.analysis.lam.debugger.domain.exception.InvalidLamPackageException
import com.leishmaniapp.analysis.lam.debugger.domain.exception.LamAlreadyBoundException
import com.leishmaniapp.analysis.lam.debugger.domain.exception.LamUnboundException
import com.leishmaniapp.analysis.lam.debugger.domain.services.ILamConnectionService
import javax.inject.Inject

/**
 * Bound to a local LAM service
 */
class LamConnectionServiceImpl @Inject constructor() : ILamConnectionService {

    companion object {
        /**
         * TAG for using with [Log]
         */
        val TAG = LamConnectionServiceImpl::class.simpleName!!

        /**
         * Intent action to grab the service
         */
        const val LAM_SERVICE_ACTION = "com.leishmaniapp.lam.ACTION_ANALYZE"

        /**
         * Base name for the LAM modules installed in device
         */
        const val BASE_LAM_PACKAGE = "com.leishmaniapp.lam."
    }

    /**
     * Handle LAM service connections
     */
    inner class CustomServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "Connected to LAM service: $name")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "Disconnected from LAM service: $name")
        }
    }

    /**
     * Service connection for the current LAM service
     */
    private var currentServiceConnection: CustomServiceConnection? = null

    override fun tryBind(context: Context, model: String): Result<Unit> =
        try {
            // Check if another service was already bound
            if (currentServiceConnection != null) {
                throw LamAlreadyBoundException()
            }

            // Create the service intent
            val intent = Intent(LAM_SERVICE_ACTION).apply {
                setPackage(BASE_LAM_PACKAGE + model)
            }

            // Bind to the service
            if (!context.bindService(
                    intent,
                    CustomServiceConnection().also {
                        currentServiceConnection = it
                    },
                    Context.BIND_AUTO_CREATE
                )
            ) {
                // Failed to bind service, does not exist
                throw InvalidLamPackageException(BASE_LAM_PACKAGE + model)
            }

            // Exit with no errors
            Result.success(Unit)
        } catch (e: Exception) {
            // Catch the exception and return it
            currentServiceConnection = null
            Result.failure(e)
        }

    override fun tryUnbind(context: Context): Result<Unit> =
        try {
            // Check if a service is already bounded
            if (currentServiceConnection == null) {
                throw LamUnboundException()
            }

            // Unbind the service
            context.unbindService(currentServiceConnection!!)

            // Delete the service connection and return success
            currentServiceConnection = null
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
}