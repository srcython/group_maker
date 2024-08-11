package com.yeceylan.groupmaker.ui.match.matchinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.DeadObjectException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.use_cases.AddOldMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.GetActiveMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.UpdateMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.auth.GetCurrentUserUidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchInfoViewModel @Inject constructor(
    private val addOldMatchUseCase: AddOldMatchUseCase,
    private val getActiveMatchUseCase: GetActiveMatchUseCase,
    private val updateMatchUseCase: UpdateMatchUseCase,
    private val getCurrentUserUidUseCase: GetCurrentUserUidUseCase
) : ViewModel() {

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

    fun finishMatch() {
        viewModelScope.launch {
            try {
                val userId = getCurrentUserUidUseCase()
                val activeMatch = getActiveMatchUseCase(userId)

                if (activeMatch != null) {
                    val updatedMatch = activeMatch.copy(isActive = false)
                    updateMatchUseCase(userId, updatedMatch)
                    addOldMatchUseCase(updatedMatch)
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Exception occurred", e)
            }
        }
    }
}

