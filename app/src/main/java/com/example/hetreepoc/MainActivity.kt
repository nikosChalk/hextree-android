package com.example.hetreepoc

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    var counter = 0


    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val homeTextView: TextView = findViewById(R.id.home_textview)

        val clickmeButton: Button = findViewById(R.id.home_button)
        clickmeButton.setOnClickListener {
            counter++
            val s = String.format("Clicked %d times", counter)
            Log.i(TAG, s)
            homeTextView.text = s

            if(counter == 10) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://hextree.io"))
                startActivity(browserIntent) //send the intent
            }
        }
    }
}
