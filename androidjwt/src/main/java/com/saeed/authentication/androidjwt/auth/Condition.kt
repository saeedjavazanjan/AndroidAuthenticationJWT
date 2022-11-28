package com.saeed.authentication.androidjwt.auth
interface Condition {
    fun Authorized()
    fun Unauthorized()
    fun UnknownError()
}
