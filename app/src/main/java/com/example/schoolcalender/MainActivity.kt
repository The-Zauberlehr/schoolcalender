package com.example.schoolcalender

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.android.awaitFrame
import okhttp3.FormBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.wait
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

inline fun <reified T> Gson.fromJson(json: JsonElement) = fromJson<T>(json, object: TypeToken<T>() {}.type)
public lateinit var strdate: String
public lateinit var schoolid: String
public lateinit var sessionid: String
public lateinit var traceid:String
public lateinit var authorization:String
public lateinit var user_id:String
public fun String.addCharAtIndex(char: Char,index:Int) =StringBuilder(this).apply { insert(index, char) }.toString()

private lateinit var myToolbar:Toolbar
class MainActivity : AppCompatActivity() {
    private lateinit var mainadapter: Mainrvadapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)


        fun getschoolheadr(){
            // Sitzung und Schulid
            val retrofit = Retrofithelperschool.getClient()
            val userApi = retrofit.create(QuotesApi::class.java)
            val schoolresponse = userApi.getcookies()
            .execute()
            val headers = schoolresponse.headers()
            sessionid = headers.toMutableList().get(5).toString().removeRange(0,13).removeRange(43,93) //entfernt für diese App unnützes


            schoolid= headers.toMutableList().get(6).toString().removeRange(0,13).removeRange(48,137)


        }
        suspend fun gettimetabel() {
            //Funktion um Die relevanten Daten des Staundenplanes zu bekommen
            val quotesApi = RetrofitHelper.getInstance().create(QuotesApi::class.java)
            delay(1000L)
            val result = quotesApi.getQuotes("/WebUntis/api/public/timetable/weekly/data?elementType=5&elementId=${user_id}&date=${strdate}&formatId=13&filter.departmentId=-1")
            //Aufbau der Anfragen
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    val items = result.body().toString()
                    val jso = JsonParser().parse(items).asJsonObject.get("data").asJsonObject.get("result").asJsonObject.get("data").asJsonObject.get("elementPeriods").asJsonObject.get(user_id)
                    val gson = Gson()
                    val data = gson.fromJson<List<jsondata>>(jso)
                    val sortdata = data.sortedWith(compareBy<jsondata>({ it.date }, { it.startTime }).reversed())
                    //sortiert die Liste Primär nach Datum sekundär nach start Zeit, in umgekehreter Reinfolge da die Schleife darunter von hinten anfängt
                    var i = sortdata.count() - 1 //Listen fangen bei 0 an zu zählen
                    var curentday = 0

                    while (i >= 0) { //für jedes Element der Liste wird ein neuer Eintrag im Recylerview gemacht

                        if (curentday != sortdata[i].date) { //fügt die Wochentage ein
                            val dateformatter = sortdata[i].date.toString().addCharAtIndex('-', 4)
                                .addCharAtIndex('-', 7)
                            val dayofweek = LocalDate.parse( //formatiert den String in ein Datum
                                dateformatter,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            )

                            val newday = jsondata(
                                "",
                                "${dayofweek.dayOfWeek}  ${dayofweek.dayOfMonth}.${dayofweek.monthValue}", //Wochentag, Tag des Monats, Monatnummer
                                sortdata[i].date,
                                0, //muss 0 sein da es die unterscheidung zwischen Stunde und Tag ist
                                0,
                                "",
                                ""
                            )
                            mainadapter.addclass(newday, pref.getBoolean("purple_mode", false)) //ruft Fkt auf die ein Element dem RecyclerView hinzufügt siehe Mainrvadapter
                            curentday = sortdata[i].date!!
                        } else {

                            mainadapter.addclass(sortdata[i], pref.getBoolean("purple_mode", false))

                            i -= 1
                        }

                    }


                    }
                }


        }
        fun postlogin(){
            val retrofit = Loginhelper.getInstance()
            val username = pref.getString("user_name","") //holt usere in den Einstellungen gesetzten Preferencen
            val password = pref.getString("user_password","")
            val token = pref.getString("token","")
            val userApi = retrofit.create(QuotesApi::class.java)
            val userresponse = userApi.login("bsz wirtschaft dresden",username.toString(),password.toString(),token.toString())
                .execute()
            if (userresponse.isSuccessful) {
                val headers = userresponse.headers()
                val traceheader = headers["Set-Cookie"] //gibt nur den gewünschten Header zurück
                traceid = traceheader.toString().removeRange(48, 71)
            }


        }
        suspend fun getauthorization(){
            val quotesApi = RetrofitHelper.getInstance().create(QuotesApi::class.java)
            val result = quotesApi.authorizationgetter("/WebUntis/api/token/new")
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    val items = result.body().toString()

                    authorization = "Bearer ${items.substring(1, items.length -1)}"
                }


            }


        }
        suspend fun getid(){
            val quotesApi = IdHelper.getInstance().create(QuotesApi::class.java)
            val result = quotesApi.id("/WebUntis/api/rest/view/v1/app/data")
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    val items = result.body().toString()
                    user_id = JsonParser().parse(items).asJsonObject.get("user").asJsonObject.get("person").asJsonObject.get("id").toString()

                    }
            }


        }
        myToolbar = findViewById(R.id.mytoolbar) //custom Toolbar siehe toolbar
        myToolbar.setTitle("")

        setSupportActionBar(myToolbar)

        var date = LocalDate.now() //heutiger Tag
        strdate = date.toString()
        tvdate.text= "${date.dayOfMonth}.${date.monthValue}.${date.year}" //Tages Anzeige
        mainadapter = Mainrvadapter(mutableListOf())
        rvmain.adapter = mainadapter //weißt Mairvadapter dem Recyclerview zu
        rvmain.layoutManager = LinearLayoutManager(this)

        GlobalScope.launch {
            getschoolheadr()
            postlogin()
            try { //verhindert absturz
                getauthorization()
                getid()
                gettimetabel()
            }
            catch (expection: com.google.gson.JsonIOException){ //für den Fall das die Anmeldedaten nicht stimmen
                runOnUiThread(Runnable {
                    kotlin.run { Toast.makeText(this@MainActivity, "an error occured", Toast.LENGTH_SHORT).show() }
                    //schickt Toast
                })

            }

        }



        bforward.setOnClickListener {
            date = date.plusDays(7) //springt mit dem Datum 7 Tage also in die nächste Woche
            strdate = date.toString()
            tvdate.text= "${date.dayOfMonth}.${date.monthValue}.${date.year}"
            mainadapter.removeclass() //leert RecyclerView siehe Mainrvadapter
            GlobalScope.launch {
                getschoolheadr()
                postlogin()
                try {
                    getauthorization()
                    getid()
                    gettimetabel()
                }
                catch (expection: com.google.gson.JsonIOException){
                    runOnUiThread(Runnable {
                        kotlin.run { Toast.makeText(this@MainActivity, "an error occured", Toast.LENGTH_SHORT).show() }
                    })

                }

            }

        }
        bbackward.setOnClickListener {

            date = date.minusDays(7)
            strdate = date.toString()
            tvdate.text= "${date.dayOfMonth}.${date.monthValue}.${date.year}"


            mainadapter.removeclass()
            GlobalScope.launch {
                getschoolheadr()
                postlogin()
                try {
                    getauthorization()
                    getid()
                    gettimetabel()
                }
                catch (expection: com.google.gson.JsonIOException){
                    runOnUiThread(Runnable {
                        kotlin.run { Toast.makeText(this@MainActivity, "an error occured", Toast.LENGTH_SHORT).show() }
                    })

                }

            }


        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings ->{//startet bei Konopfdruck die Einstellungen
                val intent = Intent(this, SettingsActivity::class.java)

                startActivity(intent)

            }
            R.id.reload ->{ //neuladen Startet Aktivität neu
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

