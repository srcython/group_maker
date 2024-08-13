package com.yeceylan.groupmaker.ui.components.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.theme.Dimen

@Composable
fun DGoogleLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Black),
        shape = MaterialTheme.shapes.small,
    ) {
        Image(
            painter = painterResource(R.drawable.google),
            contentDescription = "Continue with Google",
            modifier = Modifier
                .padding(end = Dimen.spacing_xs)
                .size(Dimen.spacing_l),
        )

        Text(
            text = "Continue with Google",
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DGoogleLoginButtonPreview() {
    DGoogleLoginButton(onClick = {})
}
