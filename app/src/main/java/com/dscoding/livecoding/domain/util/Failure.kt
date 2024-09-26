package com.dscoding.livecoding.domain.util

sealed interface Failure {
    data object InternetConnection : Failure
    data object ServerError : Failure
    data object Unknown : Failure
}