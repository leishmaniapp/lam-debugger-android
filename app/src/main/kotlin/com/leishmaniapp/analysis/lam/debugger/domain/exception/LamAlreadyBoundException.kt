package com.leishmaniapp.analysis.lam.debugger.domain.exception

/**
 * Thrown when a LAM bound is requested but a service was previously bounded but not released
 */
class LamAlreadyBoundException :
    Exception("A LAM service has already been bounded")