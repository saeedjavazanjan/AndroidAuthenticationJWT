package com.saeed.authentication.jwt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.saeed.authentication.androidjwt.JWT
import com.saeed.authentication.androidjwt.auth.JWTImpl

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JWT("","","","")



    }
}