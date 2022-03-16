package com.example.alertdialogdemo

import android.content.Context
import android.content.DialogInterface
import com.example.currentweatherdatabinding.MainActivity

class MyListener(val ctx: Context) : DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface?, choice: Int) {
        if (choice == 0) {
            (ctx as MainActivity).secondFragment = false
        } else {
            (ctx as MainActivity).secondFragment = true
        }
    }
}