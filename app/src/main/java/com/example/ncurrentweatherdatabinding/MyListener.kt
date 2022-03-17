package com.example.ncurrentweatherdatabinding

import android.content.Context
import android.content.DialogInterface

class MyListener(private val ctx: Context) : DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface?, choice: Int) {
        when (choice) {
            1 -> (ctx as MainActivity).secondFragment = false
            2 -> (ctx as MainActivity).secondFragment = true
            else -> throw Exception("Unexpected choice")
        }
    }
}