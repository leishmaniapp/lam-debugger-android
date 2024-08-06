package com.leishmaniapp.analysis.lam.debugger.infrastructure.di

import com.leishmaniapp.analysis.lam.debugger.domain.services.ILamConnectionService
import com.leishmaniapp.analysis.lam.debugger.infrastructure.lam.LamConnectionServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provide the implementation for the [com.leishmaniapp.analysis.lam.debugger.domain.services] layer
 */
@Module
@InstallIn(SingletonComponent::class)
interface ServicesModule {

    /**
     * Provide [LamConnectionServiceImpl] as the [ILamConnectionService] implementation
     */
    @Binds
    @Singleton
    fun provideLamConnectionService(
        lamConnectionService: LamConnectionServiceImpl
    ): ILamConnectionService

}