package com.leishmaniapp.analysis.lam.debugger.domain.exception

/**
 * Thrown when a given LAM could not be found at the given package location
 */
class InvalidLamPackageException(applicationPackage: String) :
    Exception("No LAM module found at ($applicationPackage)")