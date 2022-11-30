package com.saeed.authentication.androidjwt

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saeed.authentication.androidjwt.auth.*
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

class JWT(baseUrl:String,var condition:Condition ,context: Context) {
 var repository:AuthRepository
    lateinit var state :AuthState

    init {
this.repository= authRepository(AuthApi(baseUrl),sharedPref(context))


    CoroutineScope(Dispatchers.Main).launch{
        resultEvents()
    }
        this.state= AuthState()
    authenticate()

}



    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()


     fun signUp() {
        CoroutineScope(Dispatchers.IO).launch {
            state = state.copy(isLoading = true)
            val result = repository.signUp(
                username = state.signUpUsername,
                password = state.signUpPassword
            )
            resultChannel.send(result)
            resultEvents()

            state = state.copy(isLoading = false)
        }
    }

    fun signIn() {
        CoroutineScope(Dispatchers.IO).launch {
            state = state.copy(isLoading = true)
            val result = repository.signIn(
                username = state.signInUsername,
                password = state.signInPassword
            )
            resultChannel.send(result)
                resultEvents()

            state = state.copy(isLoading = false)
        }
    }

    fun authenticate() {
        CoroutineScope(Dispatchers.IO).launch {
            state = state.copy(isLoading = true)
            val result = repository.authenticate()
            resultChannel.send(result)
            resultEvents()

            state = state.copy(isLoading = false)
        }
    }

    suspend fun  resultEvents(){
        CoroutineScope(Dispatchers.Main).launch {

            authResults.collect { result ->
                when (result) {
                    is AuthResult.Authorized -> {
                        condition.Authorized()
                    }
                    is AuthResult.Unauthorized -> {
                        condition.Unauthorized()

                    }
                    is AuthResult.UnknownError -> {
                        condition.UnknownError()
                    }
                }
            }
        }
    }

    private fun AuthApi(BaseURL:String): AuthApi {
        return Retrofit.Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

   private fun sharedPref(context: Context): SharedPreferences {
       val sharedPreference=context.getSharedPreferences("prefs",Context.MODE_PRIVATE)
        return sharedPreference
    }

   private fun authRepository(api: AuthApi, prefs: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(api, prefs)
    }
}