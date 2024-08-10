package com.leishmaniapp.analysis.lam.debugger.infrastructure.lam

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.core.os.bundleOf
import com.leishmaniapp.analysis.lam.debugger.domain.exception.InvalidLamPackageException
import com.leishmaniapp.analysis.lam.debugger.domain.exception.LamAlreadyBoundException
import com.leishmaniapp.analysis.lam.debugger.domain.exception.LamUnboundException
import com.leishmaniapp.analysis.lam.debugger.domain.services.ILamConnectionService
import com.leishmaniapp.analysis.lam.service.LamAnalysisRequest
import com.leishmaniapp.analysis.lam.service.LamAnalysisResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Bound to a local LAM service
 */
class LamConnectionServiceImpl @Inject constructor(

    @ApplicationContext
    private val context: Context,

    ) : ILamConnectionService {

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
     * Service connection for the current LAM service
     */
    private var currentServiceConnection: CustomServiceConnection? = null

    /**
     * bound service package name
     */
    private var connectionPackage: String? = null

    /**
     * Application messenger
     */
    private var sendMessenger: Messenger? = null

    /**
     * Where the
     */
    override val replyChannel: Channel<LamAnalysisResponse> = Channel()

    private val replyHandler = Handler(Looper.getMainLooper()) { msg ->

        // Get the reply data
        Log.i(TAG, "New reply data arrived")
        val response: LamAnalysisResponse? = msg.data.run {

            // Set the class loader for the bundle
            classLoader = this@LamConnectionServiceImpl.context.classLoader

            // Get the parcel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("LAM_BUNDLE", LamAnalysisResponse::class.java)
            } else {
                @Suppress("DEPRECATION")
                getParcelable("LAM_BUNDLE")
            }
        }

        runBlocking {
            replyChannel.send(response!!)
        }

        // Handler expects more messages
        true
    }

    private val replyMessenger: Messenger = Messenger(replyHandler)

    /**
     * Handle LAM service connections
     */
    inner class CustomServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "Connected to LAM service: $name")

            // Set the package name and messenger
            sendMessenger = Messenger(service)
            connectionPackage = name!!.packageName
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "Disconnected from LAM service: $name")

            // Set variables to null
            currentServiceConnection = null
            connectionPackage = null
            sendMessenger = null
        }
    }

    override fun tryBind(context: Context, model: String): Result<Unit> =
        try {
            // Check if another service was already bound
            if (currentServiceConnection != null) {
                throw LamAlreadyBoundException()
            }

            // Create the service intent
            val intent = Intent(LAM_SERVICE_ACTION).apply {
                setPackage(BASE_LAM_PACKAGE + model)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
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

            // Delete the service connection and return success
            currentServiceConnection = null
            connectionPackage = null
            sendMessenger = null

            // Catch and return the exception
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
            connectionPackage = null
            sendMessenger = null

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun trySend(request: LamAnalysisRequest): Result<Unit> =
        try {
            if (sendMessenger == null || connectionPackage == null) {
                throw LamUnboundException()
            }

            // Grant permission to the package
            context.grantUriPermission(
                connectionPackage,
                request.uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            val message = Message().apply {
                data = bundleOf("LAM_BUNDLE" to request)
                replyTo = replyMessenger
            }

            // Send the message
            sendMessenger!!.send(message)

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
}