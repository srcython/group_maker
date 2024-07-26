package com.yeceylan.groupmaker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Blue,
    onClickToButton: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClickToButton,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
    ) {
        Text(text = text, color = Color.White)
    }
}