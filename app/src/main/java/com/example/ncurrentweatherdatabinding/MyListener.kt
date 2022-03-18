package com.example.ncurrentweatherdatabinding

import android.content.Context
import android.content.DialogInterface

class MyListener(private val ctx: Context) : DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface?, choice: Int) {
        when (choice) {
			// at this point I literally don't care anymore
            -1 -> (ctx as MainActivity).secondFragment = false
			
            0 -> (ctx as MainActivity).secondFragment = false
            1 -> (ctx as MainActivity).secondFragment = true
            else -> throw Exception("Unexpected choice")
        }
    }
}