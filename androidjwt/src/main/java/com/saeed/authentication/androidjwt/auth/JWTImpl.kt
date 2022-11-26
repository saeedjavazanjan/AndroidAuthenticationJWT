package com.saeed.authentication.androidjwt.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.work.HiltWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject

class JWTImpl @Inject constructor(
    private val repository: AuthRepository
) {

    init {
        authenticate()
            }
    var state by mutableStateOf(AuthState())

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()


    private fun signUp() {
        CoroutineScope(Dispatchers.IO).launch {
            state = state.copy(isLoading = true)
            val result = repository.signUp(
                username = state.signUpUsername,
                password = state.signUpPassword
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun signIn() {
        CoroutineScope(Dispatchers.IO).launch {
            state = state.copy(isLoading = true)
            val result = repository.signIn(
                username = state.signInUsername,
                password = state.signInPassword
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun authenticate() {
        CoroutineScope(Dispatchers.IO).launch {
            state = state.copy(isLoading = true)
            val result = repository.authenticate()
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    suspend fun resultEvents(){
        authResults.collect { result ->
            when(result) {
                is AuthResult.Authorized -> {
                    Condition.Authorized()

                }
                is AuthResult.Unauthorized -> {
                    Condition.Unauthorized()

                }
                is AuthResult.UnknownError -> {
                    Condition.UnknownError()
                }
            }
        }
    }
}