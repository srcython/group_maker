package com.yeceylan.groupmaker.ui.auth.login


import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.bottombar.BottomBarScreen
import com.yeceylan.groupmaker.ui.components.button.DButton
import com.yeceylan.groupmaker.ui.components.button.DGoogleLoginButton
import com.yeceylan.groupmaker.ui.components.text.DOutlinedTextField
import com.yeceylan.groupmaker.ui.theme.Dimen
import com.yeceylan.groupmaker.ui.theme.GroupMakerTheme

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    viewModel.loginWithGoogle(account)
                }
            } catch (e: ApiException) {
                // sonar - comment
            }
        }
    }

    LaunchedEffect(uiState.isSuccessGoogleLogin) {
        if (uiState.isSuccessGoogleLogin) {
            navController.navigate(BottomBarScreen.Home.route)
            viewModel.resetUIState()
        }
    }

    LaunchedEffect(uiState.isSuccessEmailAndPasswordLogin) {
        if (uiState.isSuccessEmailAndPasswordLogin) {
            navController.popBackStack()
            navController.navigate(BottomBarScreen.Home.route)
            viewModel.resetUIState()
        }
    }

    with(uiState) {
        if (isLoading) {
            CircularProgressIndicator()
            return@with
        }

        LoginScreenUI(
            navController = navController,
            loginWithGoogle = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            },
            errorMessage = errorMessage,
            isError = isHaveError,
            onClickToTextField = {
                viewModel.updateErrorStatesWithDefaultValues()
            },
            loginWithEmailAndPassword = { email, password ->
                viewModel.loginWithEmailAndPassword(email, password, context)
            },
        )
    }
}

@Composable
fun LoginScreenUI(
    navController: NavController,
    isError: Boolean,
    errorMessage: String,
    loginWithGoogle: () -> Unit,
    onClickToTextField: () -> Unit,
    loginWithEmailAndPassword: (String, String) -> Unit,
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val verticalScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.spacing_m1)
            .verticalScroll(verticalScroll),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.login),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.height(Dimen.spacing_xxl))

        DGoogleLoginButton(modifier = Modifier,
            onClick = {
                loginWithGoogle()  // This will trigger the Google login process
            })

        DividerSignInWith(modifier = Modifier.padding(vertical = Dimen.spacing_m1))

        Spacer(modifier = Modifier.height(Dimen.spacing_m1))

        SignInOutlineTextField(
            modifier = Modifier.clickable {
                onClickToTextField()
            },
            value = emailState.value,
            onValueChange = { emailState.value = it },
            isError = isError,
            hint = stringResource(R.string.hint_mail),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        SignInOutlineTextField(
            modifier = Modifier
                .padding(top = Dimen.spacing_m1)
                .clickable {
                    onClickToTextField()
                },
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            hint = stringResource(R.string.hint_password),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = isError,
            columnContent = {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                )
            },
        )

        DButton(
            modifier = Modifier.padding(top = Dimen.spacing_m1),
            text = stringResource(R.string.login),
        ) {
            loginWithEmailAndPassword(emailState.value, passwordState.value)
        }

        SignUpButton(modifier = Modifier.padding(top = Dimen.spacing_xs)) {
            navController.navigate(AuthenticationScreens.SignUpScreen)
        }
    }
}


@Composable
private fun SignInOutlineTextField(
    modifier: Modifier = Modifier,
    hint: String,
    value: String,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit,
    rowContent: @Composable () -> Unit = { /* sonar - comment */ },
    columnContent: @Composable () -> Unit = { /* sonar - comment */ },
) {
    DOutlinedTextField(
        modifier = modifier,
        textFieldValue = value,
        enabled = true,
        isError = isError,
        columnContent = columnContent,
        onValueChange = onValueChange,
        textFieldHint = hint,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,
            disabledBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
        ),
        rowContent = rowContent,
        keyboardOptions = keyboardOptions,
    )
}

@Composable
private fun DividerSignInWith(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Divider(
            modifier = Modifier
                .padding(
                    start = Dimen.spacing_xxxxs,
                    top = Dimen.spacing_s1,
                    end = Dimen.spacing_xs,
                    bottom = Dimen.spacing_xxxxs,
                )
                .width(Dimen.spacing_xxl * 2),
            color = Color.Gray,
        )

        Text(
            text = stringResource(R.string.or_sign_in_with),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        Divider(
            modifier = Modifier
                .padding(top = Dimen.spacing_s1, start = Dimen.spacing_xs)
                .width(Dimen.spacing_xxl * 2),
            color = Color.Gray,
        )
    }
}

@Composable
private fun SignUpButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.don_t_have_an_account),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
        )

        TextButton(
            onClick = onClick,
        ) {
            Text(text = stringResource(R.string.sign_up_here))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    GroupMakerTheme {
        LoginScreen(
            navController = rememberNavController(),
        )
    }
}
