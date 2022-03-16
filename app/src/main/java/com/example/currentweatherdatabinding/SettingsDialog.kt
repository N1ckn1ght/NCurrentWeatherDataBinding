package com.example.alertdialogdemo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.currentweatherdatabinding.R

class SettingsDialog(val ctx: Context): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var choice = 0
        return ctx.let { AlertDialog.Builder(it).
        setSingleChoiceItems(ctx.resources.getStringArray(R.array.fragment_types),
            0, {dialog, which -> choice = which}).
        setPositiveButton("Ok", MyListener(ctx)).
        create()
        }
    }
}