package com.example.ncurrentweatherdatabinding

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SettingsDialog(private val ctx: Context): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var choice = 0
        return ctx.let { AlertDialog.Builder(it).
        setSingleChoiceItems(ctx.resources.getStringArray(R.array.fragment_types),
            (ctx as MainActivity).secondFragment.toInt()
        ) { _, which -> choice = which }.
        setPositiveButton("Ok", MyListener(ctx)).
        create()
        }
    }
}

private fun Boolean.toInt(): Int {
    return if (this) { 1 } else { 0 }
}