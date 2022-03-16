package com.example.currentweatherdatabinding

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.currentweatherdatabinding.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {
    lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        val view = binding.root
        view.setBackgroundColor(Color.YELLOW)
        return view
    }
}