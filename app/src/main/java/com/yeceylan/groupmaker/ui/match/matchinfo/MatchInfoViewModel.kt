package com.yeceylan.groupmaker.ui.match.matchinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel

class MatchInfoViewModel : ViewModel() {

    fun copyIbanToClipboard(context: Context, iban: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("IBAN", iban)
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(context, "IBAN kopyalandı", Toast.LENGTH_SHORT).show()
    }

    fun openMapForDirections(context: Context, address: String) {
        val gmmIntentUri = Uri.parse("google.navigation:q=${Uri.encode(address)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "Harita uygulaması bulunamadı", Toast.LENGTH_SHORT).show()
        }
    }


}
