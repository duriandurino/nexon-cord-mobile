package com.theateam.nexoncordapp.LoginMVP

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.theateam.nexoncord.R

class LoginActivity : Activity() {

    private lateinit var presenter:LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val user:String = findViewById<EditText>(R.id.etuser).text.toString()
        val password:String = findViewById<EditText>(R.id.etpassword).text.toString()

        val loginbtn:Button = findViewById<Button>(R.id.btnlogin)

        loginbtn.setOnClickListener {

        }
    }
}