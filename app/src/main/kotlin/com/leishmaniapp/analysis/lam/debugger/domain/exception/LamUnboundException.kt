package com.leishmaniapp.analysis.lam.debugger.domain.exception

/**
 * Thrown when a LAM unbound cannot be performed, usually because it was not bounded in first place
 */
class LamUnboundException :
    Exception("LAM service cannot be unbounded")