package com.harsh.shophere.common

sealed class ResultState <out T>{
    data class Success<T>(var result:T): ResultState<T>()
    data class Error<T>(var error: String): ResultState<T>()
    data object Loading: ResultState<Nothing>()
}