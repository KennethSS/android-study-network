package com.study.network.retrofit.exception

sealed class NetworkException(msg: String) : Exception(msg){
    data class BadRequestException(val msg: String) : NetworkException(msg)
    data class UnauthorizedException(val msg: String) : NetworkException(msg)
    data class ForbiddenException(val msg: String) : NetworkException(msg)
    data class NotFoundException(val msg: String) : NetworkException(msg)
    data class UnknownException(val msg: String) : NetworkException(msg)
}