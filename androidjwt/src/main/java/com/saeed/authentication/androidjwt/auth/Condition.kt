package com.saeed.authentication.androidjwt.auth

public interface Condition {
    fun Authorized()
    fun Unauthorized()
    fun UnknownError()
}