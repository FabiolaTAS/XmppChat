package com.example.xmppchat.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.xmppchat.R
import com.example.xmppchat.viewmodel.XmppViewModel


class MainActivity : AppCompatActivity() {
    lateinit var model: XmppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        model = ViewModelProvider.NewInstanceFactory().create(XmppViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            run {
                connectXmpp();
            }
        }
    }

    fun connectXmpp(){
        var email = findViewById<EditText>(R.id.email).text.toString();
        var password = findViewById<EditText>(R.id.password).text.toString();
        model.connection(email, password).observe(this, Observer { connectSucess ->
            run {
                if (connectSucess) {
                    Toast.makeText(this, "OK", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "N OK", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
