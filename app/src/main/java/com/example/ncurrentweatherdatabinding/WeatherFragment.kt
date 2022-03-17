package com.example.ncurrentweatherdatabinding

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class WeatherFragment(private val wdesc: String, private val windspeed: String) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        view.setBackgroundColor(Color.YELLOW)
        view.findViewById<TextView>(R.id.wdesc).text = wdesc
        view.findViewById<TextView>(R.id.windspeed).text = windspeed
        return view
    }
}