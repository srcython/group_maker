package com.yeceylan.groupmaker.ui.auth.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.bottombar.BottomBarScreen
import com.yeceylan.groupmaker.ui.components.BackButton
import com.yeceylan.groupmaker.ui.components.DButton
import com.yeceylan.groupmaker.ui.components.DGoogleLoginButton
import com.yeceylan.groupmaker.ui.components.DOutlinedTextField
import com.yeceylan.groupmaker.ui.theme.Dimen
import com.yeceylan.groupmaker.ui.theme.GroupMakerTheme

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    with(uiState) {
        if (isLoading) {
            CircularProgressIndicator()
            return@with
        }

        if (isHaveError) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        if (isSuccessGoogleSignup) {
            /* sonar - comment */
        }

        if (isSuccessSignUpWithEmailAndPassword) {
            viewModel.resetUIState()
            navController.navigate(BottomBarScreen.Home.route)
        }

        SignUpScreenUI(
            viewModel = viewModel,
            navController = navController,
            signUpWithGoogle = { viewModel.signUpWithGoogle() },
        )
    }
}

@Composable
private fun SignUpScreenUI(
    viewModel: SignUpViewModel,
    navController: NavController,
    signUpWithGoogle: () -> Unit,
) {
    val verticalScroll = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordsMatch by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.spacing_m1)
            .verticalScroll(verticalScroll),
    ) {
        BackButton(modifier = Modifier.padding(top = Dimen.spacing_xs)) {
            navController.popBackStack()
        }

        Text(
            text = stringResource(R.string.sign_up),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
        )

        DGoogleLoginButton(modifier = Modifier.padding(top = Dimen.spacing_xs)) {
            signUpWithGoogle()
        }

        DividerSignUpWith(
            modifier = Modifier
                .padding(top = Dimen.spacing_xs, bottom = Dimen.spacing_m1)
                .align(Alignment.CenterHorizontally),
        )

        SignUpOutlineTextField(
            value = stringResource(R.string.username),
            hint = stringResource(R.string.username_hint),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = {
                // sonar comment
            },
        )

        SignUpOutlineTextField(
            value = stringResource(R.string.email),
            hint = stringResource(R.string.hint_mail),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it },
            trailingIconContent = {
                if (viewModel.isEmailValid(email = email)) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        tint = Color.Green,
                        contentDescription = stringResource(R.string.content_description_visibility_icon),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        tint = Color.Red,
                        contentDescription = stringResource(R.string.content_description_visibility_icon),
                    )
                }
            }
        )

        SignUpOutlineTextField(
            value = stringResource(R.string.password),
            hint = stringResource(R.string.hint_password),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                password = it
                isPasswordsMatch = viewModel.isPasswordsMatch(password, confirmPassword)
            },
            trailingIconContent = {
                if (viewModel.isPasswordValid(password)) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        tint = Color.Green,
                        contentDescription = stringResource(R.string.content_description_visibility_icon),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        tint = Color.Red,
                        contentDescription = stringResource(R.string.content_description_visibility_icon),
                    )
                }
            },
        )

        SignUpOutlineTextField(
            value = stringResource(R.string.verify_password_text),
            hint = stringResource(R.string.verify_password_hint_text),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {
                confirmPassword = it
                isPasswordsMatch = viewModel.isPasswordsMatch(password, it)
            },
            trailingIconContent = {
                if (isPasswordsMatch) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        tint = Color.Green,
                        contentDescription = stringResource(R.string.content_description_visibility_icon),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        tint = Color.Red,
                        contentDescription = stringResource(R.string.content_description_visibility_icon),
                    )
                }
            },
            supportingText = {
                if (!isPasswordsMatch) {
                    Text(
                        text = stringResource(R.string.password_not_match),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            },
        )

        PrivacyPolicyRow()

        DButton(
            text = stringResource(R.string.button_sign_up),
            modifier = Modifier.padding(top = Dimen.spacing_m1),
        ) {
            viewModel.signUpWithEmailAndPassword(email = email, password = password)
        }

        LoginButtonRow(modifier = Modifier.padding(top = Dimen.spacing_xs)) {
            navController.navigate(AuthenticationScreens.LoginScreen)
        }
    }
}

@Composable
private fun DividerSignUpWith(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        val dividerModifier = Modifier
            .padding(top = Dimen.spacing_s1, start = Dimen.spacing_xs)
            .width(Dimen.spacing_xxl * 2)

        Divider(
            modifier = dividerModifier,
            color = Color.Blue,
        )

        Text(
            text = stringResource(R.string.or_sign_up_with_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        Divider(
            modifier = dividerModifier,
            color = Color.Blue,
        )
    }
}

@Composable
private fun SignUpOutlineTextField(
    modifier: Modifier = Modifier,
    hint: String,
    value: String,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit = { /* sonar-comment */ },
    supportingText: @Composable () -> Unit = { /* sonar-comment */ },
    trailingIconContent: @Composable () -> Unit = { /* sonar-comment */ },
) {
    DOutlinedTextField(
        modifier = modifier,
        textFieldValue = value,
        textFieldHint = hint,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,
            disabledBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
        ),
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        trailingIconContent = trailingIconContent,
        onValueChange = onValueChange,
        supportingText = supportingText,
    )
}

@Composable
private fun PrivacyPolicyRow(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Checkbox(
            checked = true,
            onCheckedChange = { /* sonar-comment */ },
        )

        Text(
            text = stringResource(R.string.by_creating_an_account_i_accept_hiring_hub_terms_of_use_and_privacy_policy),
            modifier = Modifier.padding(top = Dimen.spacing_s1),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun LoginButtonRow(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.already_have_an_account),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
        )

        TextButton(onClick = onClick) {
            Text(text = stringResource(R.string.sign_up_login_text_button_text))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    GroupMakerTheme {
        SignUpScreen(navController = rememberNavController())
    }
}
