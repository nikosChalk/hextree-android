package com.example.hetreepoc

import android.annotation.SuppressLint
import android.content.ComponentName
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


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

            CoroutineScope(Dispatchers.IO).launch {
                val url = URL("https://www.android.com/")
                val urlConnection = url.openConnection() as HttpURLConnection
                val reader =
                    BufferedReader(InputStreamReader(BufferedInputStream(urlConnection.inputStream)))
                val sb = StringBuilder()
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    sb.append(line).append('\n')
                }
                val result = sb.toString()
                runOnUiThread { homeTextView.text = result }
            }

            if(counter == 10) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://hextree.io"))
                startActivity(browserIntent) //send the intent
            }
        }

        val goToIntentSolverButton: Button = findViewById(R.id.goToIntentSolverButton)
        goToIntentSolverButton.setOnClickListener {
            val intent = Intent(this, IntentSolver::class.java)
            startActivity(intent)
        }
        val goToBroadcastSolverButton: Button = findViewById(R.id.goToBroadcastSolverButton)
        goToBroadcastSolverButton.setOnClickListener {
            val intent = Intent(this, BroadcastSolver::class.java)
            startActivity(intent)
        }
        val goToServiceSolverButton: Button = findViewById(R.id.goToServiceSolverButton)
        goToServiceSolverButton.setOnClickListener {
            val intent = Intent(this, ServiceSolver::class.java)
            startActivity(intent)
        }
        val goToContentProviderSolverButton: Button = findViewById(R.id.goToContentProviderSolverButton)
        goToContentProviderSolverButton.setOnClickListener {
            val intent = Intent(this, ContentProviderSolver::class.java)
            startActivity(intent)
        }

        val webViewSolverButton: Button = findViewById(R.id.webViewSolverButton)
        webViewSolverButton.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.webviewdemo",
                "io.hextree.webviewdemo.MainActivity"
            )
            val maliciousWebPage =
"""
<html>
    <body>
        <p> Hello World! </p>
        <script>
            console.log("Executing javascript")
            //Android.showToast("Javascript can execute Java!")
            Android.showFlag() //HXT{java-in-a-webview}
        </script>
    </body>
</html>
"""
            intent.putExtra("htmlContent", maliciousWebPage)
            startActivity(intent)
        }
    }
}
