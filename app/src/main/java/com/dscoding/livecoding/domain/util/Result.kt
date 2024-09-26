package com.dscoding.livecoding.domain.util

interface Result<out D, out F: Failure> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out F: Failure>(val failure: F) : Result<Nothing, F>
}