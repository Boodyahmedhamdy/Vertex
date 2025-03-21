package com.iti.vertex.utils


sealed class Result<T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val message: String): Result<Nothing>()
    data object Loading: Result<Nothing>()
}