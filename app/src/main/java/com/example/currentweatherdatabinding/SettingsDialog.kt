package com.example.currentweatherdatabinding

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
            (ctx as MainActivity).secondFragment.toInt(), {dialog, which -> choice = which}).
        setPositiveButton("Ok", MyListener(ctx)).
        create()
        }
    }
}

private fun Boolean.toInt(): Int {
    if (this) {
        return 1
    } else {
        return 0
    }
}
