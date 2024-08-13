package com.yeceylan.groupmaker.ui.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.user.User
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.components.ProgressBar
import com.yeceylan.groupmaker.ui.theme.Dimen.font_size_18
import com.yeceylan.groupmaker.ui.theme.Dimen.font_size_l
import com.yeceylan.groupmaker.ui.theme.Dimen.font_size_l1
import com.yeceylan.groupmaker.ui.theme.Dimen.font_size_m2
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_cc
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_cclxxx
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_cl
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_cv
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_l
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_m1
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_s1
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_s2
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_xl
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_xs
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_xxs
import com.yeceylan.groupmaker.ui.theme.LightBlue

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel(), navController: NavController) {

    val context = LocalContext.current

    when (viewModel.userResponse) {
        is Resource.Error -> Toast.makeText(
            context,
            stringResource(id = R.string.error),
            Toast.LENGTH_SHORT
        ).show()

        is Resource.Loading -> ProgressBar()
        is Resource.Success -> ProfileSuccess(
            user = viewModel.userResponse.data!!,
            navController = navController,
        )
    }
}

@Composable
fun ProfileSuccess(user: User, navController: NavController) {
    var showSettingDialog by remember { mutableStateOf(false) }
    val openExitDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Gray,
                        White,
                    ),
                ),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(modifier = Modifier.height(spacing_cclxxx)) {
            Image(
                painter = painterResource(id = R.drawable.top_background),
                contentDescription = stringResource(id = R.string.top_background_desc),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,

                ) {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = stringResource(id = R.string.profile_photo_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(spacing_cc)
                        .clip(CircleShape)
                        .border(spacing_xxs, White, CircleShape)
                        .background(Gray),
                )
            }
        }

        Text(
            text = user.userName,
            fontSize = font_size_l,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = spacing_m1),
            color = Color.Black,
        )
        Text(
            text = user.email,
            fontSize = font_size_m2,
            color = Color.Black,
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing_xl, spacing_l),
            elevation = spacing_xxs,
            shape = RoundedCornerShape(spacing_xs),
            backgroundColor = White,
        ) {
            Column(modifier = Modifier.padding(spacing_s2)) {
                Text(
                    text = stringResource(id = R.string.profile_info_title),
                    fontSize = font_size_l,
                    fontWeight = FontWeight.Bold,
                    color = LightBlue,
                    modifier = Modifier.padding(spacing_xs),
                )
                Spacer(modifier = Modifier.height(spacing_xs))
                Text(
                    text = "${stringResource(id = R.string.name)}: ${user.firstName}",
                    fontSize = font_size_m2,
                    modifier = Modifier.padding(spacing_xs),
                )
                Text(
                    text = "${stringResource(id = R.string.lastname)}: ${user.surname}",
                    fontSize = font_size_m2,
                    modifier = Modifier.padding(spacing_xs),
                )
                Text(
                    text = "${stringResource(id = R.string.position)}: ${user.position}",
                    fontSize = font_size_m2,
                    modifier = Modifier.padding(spacing_xs),
                )
                Text(
                    text = "${stringResource(id = R.string.point)}: ${user.point}",
                    fontSize = font_size_m2,
                    modifier = Modifier.padding(spacing_xs),
                )
                Text(
                    text = "${stringResource(id = R.string.profile_iban)}: ${user.iban}",
                    fontSize = font_size_m2,
                    modifier = Modifier.padding(spacing_xs),
                )
            }
        }

        Button(
            onClick = { showSettingDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing_xl, end = spacing_xl, bottom = spacing_s1)
                .height(spacing_cv),
            colors = ButtonDefaults.buttonColors(backgroundColor = White),
            shape = RoundedCornerShape(15),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = stringResource(id = R.string.settings_button_logo_desc),
                    modifier = Modifier
                        .padding(end = spacing_xxs),
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = spacing_m1)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.profile_settings),
                    color = Color.Black,
                    fontSize = font_size_18,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Button(
            onClick = {
                openExitDialog.value = true
            },
            modifier = Modifier
                .padding(start = spacing_xl, end = spacing_xl, bottom = spacing_s1)
                .height(spacing_cv),
            colors = ButtonDefaults.buttonColors(backgroundColor = White),
            shape = RoundedCornerShape(15),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = stringResource(id = R.string.exit_button_logo_desc),
                    modifier = Modifier.padding(end = spacing_xxs),
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = spacing_m1)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.exit_button),
                    color = Color.Black,
                    fontSize = font_size_18,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }

    if (showSettingDialog) {
        ProfileSettingsDialog(onDismiss = { showSettingDialog = false }, user)
    }
    if (openExitDialog.value) {
        ExitDialog(onDismiss = { openExitDialog.value = false }, navController = navController)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileSettingsDialog(
    onDismiss: () -> Unit,
    user: User,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    var imageUri by remember { mutableStateOf<Uri?>(Uri.parse(user.photoUrl)) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { resultUri ->
            resultUri?.let {
                viewModel.updatePhoto(user, resultUri)
                imageUri = resultUri
            }
        }
    )

    var surname by remember { mutableStateOf(user.surname) }
    var position by remember { mutableStateOf(user.position) }
    var firstname by remember { mutableStateOf(user.firstName) }
    var iban by remember { mutableStateOf(user.iban) }

    Dialog(onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium, elevation = spacing_xs) {
            Column(
                modifier = Modifier
                    .padding(spacing_m1)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id = R.string.profile_update_button))

                GlideImage(
                    model = imageUri,
                    contentDescription = stringResource(id = R.string.profile_photo_button),
                    modifier = Modifier
                        .padding(spacing_xxs)
                        .size(spacing_cl)
                        .clip(RoundedCornerShape(spacing_s2)),
                    contentScale = ContentScale.Crop,
                )

                Button(
                    onClick = { launcher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(Blue),
                ) {
                    Text(text = stringResource(id = R.string.profile_photo_button), color = White)
                }

                OutlinedTextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    label = { Text(text = stringResource(id = R.string.name)) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Blue,
                        cursorColor = Blue,
                        focusedLabelColor = Blue,
                    ),
                )

                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text(text = stringResource(id = R.string.lastname)) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Blue,
                        cursorColor = Blue,
                        focusedLabelColor = Blue,
                    ),
                )

                OutlinedTextField(
                    value = position,
                    onValueChange = { position = it },
                    label = { Text(text = stringResource(id = R.string.position)) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Blue,
                        cursorColor = Blue,
                        focusedLabelColor = Blue,
                    ),
                )
                OutlinedTextField(
                    value = iban,
                    onValueChange = { iban = it },
                    label = { Text(text = stringResource(id = R.string.profile_iban)) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Blue,
                        cursorColor = Blue,
                        focusedLabelColor = Blue,
                    ),
                )

                Spacer(modifier = Modifier.height(spacing_s2))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(Blue),
                    ) {
                        Text(text = stringResource(id = R.string.cancel), color = White)
                    }
                    Button(
                        onClick = {
                            user.firstName = firstname
                            user.surname = surname
                            user.position = position
                            user.iban = iban

                            viewModel.updateProfileInfo(user)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(Blue),
                    ) {
                        Text(stringResource(id = R.string.add), color = White)
                    }
                }
            }
        }
    }
}

@Composable
fun ExitDialog(
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {

    Dialog(onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium, elevation = spacing_xs) {
            Column(
                modifier = Modifier
                    .padding(spacing_m1)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(text = stringResource(id = R.string.exit), fontSize = font_size_l1)
                Spacer(modifier = Modifier.height(spacing_xl))
                Text(text = stringResource(id = R.string.exit_detail))
                Spacer(modifier = Modifier.height(spacing_xl))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(Blue),
                    ) {
                        Text(text = stringResource(id = R.string.exit_no), color = White)
                    }
                    Button(
                        onClick = {
                            viewModel.logout()
                            navController.navigate(AuthenticationScreens.LoginScreen) {
                                popUpTo(navController.graph.id) {
                                    inclusive = false
                                }
                            }
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(Blue),
                    ) {
                        Text(stringResource(id = R.string.exit_yes), color = White)
                    }
                }
            }
        }
    }
}