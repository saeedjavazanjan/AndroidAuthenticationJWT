package com.saeed.authentication.jwt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.saeed.authentication.androidjwt.JWT
import com.saeed.authentication.androidjwt.auth.Condition

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jwt =JWT("https://www.google.com/" ,object :Condition {
            override fun Authorized() {
                Toast.makeText(this@MainActivity ,"yes",Toast.LENGTH_SHORT).show()
            }

            override fun Unauthorized() {
                Toast.makeText(this@MainActivity ,"no",Toast.LENGTH_SHORT).show()

            }

            override fun UnknownError() {
                Toast.makeText(this@MainActivity ,"error",Toast.LENGTH_SHORT).show()

                          }
        },this)
        val text:TextView=findViewById(R.id.helloWorld)

        text.setOnClickListener {
            Toast.makeText(this@MainActivity ,"sign in",Toast.LENGTH_SHORT).show()
            jwt.signIn()

        }
       // jwt.signIn()
       // jwt.signUp()
      //  jwt.authenticate()





    }
}