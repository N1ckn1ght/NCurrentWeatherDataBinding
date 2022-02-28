package com.example.currentweatherdatabinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: show images for the weather, the cloudiness and the wind direction
    }

    suspend fun loadWeather(city: String) {
        val API_KEY = getString(R.string.appid)
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${API_KEY}&units=metric"
        val temp = findViewById<TextView>(R.id.temperature)

        try {
            val stream = URL(weatherURL).getContent() as InputStream
            val rdata = Scanner(stream).nextLine()
            val data = JSONObject(rdata).toMap()

            if (data.containsKey("main")){
                val dataMain = JSONObject(data["main"].toString()).toMap()
                if (dataMain.containsKey("temp")) {

                }
            }
        } catch (e: FileNotFoundException) {
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                Toast.makeText(this, "ERROR 404", Toast.LENGTH_SHORT).show()
            })
        }
    }

    public fun onGetClick(v: View) {
        val city = findViewById<EditText>(R.id.city)
        // Используем IO-диспетчер вместо Main (основного потока)
        GlobalScope.launch (Dispatchers.IO) {
            loadWeather(city.text.toString())
        }
    }

    // https://stackoverflow.com/questions/44870961/how-to-map-a-json-string-to-kotlin-map
    private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }
}