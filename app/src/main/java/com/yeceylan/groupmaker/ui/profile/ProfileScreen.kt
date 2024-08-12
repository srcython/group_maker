package com.yeceylan.groupmaker.ui.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.components.ProgressBar

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel(), navController: NavController) {

    val context = LocalContext.current

    when (viewModel.userResponse) {
        is Resource.Error -> Toast.makeText(context,"Hata",Toast.LENGTH_SHORT).show()
        is Resource.Loading -> ProgressBar()
        is Resource.Success -> ProfileSucces(
            user = viewModel.userResponse.data!!,
            viewModel = viewModel,
            navController = navController,
        )
    }
}

@Composable
fun ProfileSucces(user: User,viewModel: ProfileViewModel,navController: NavController){
    var showDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF808080),
                        Color(0xFFFFFFFF)
                    ),
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(modifier = Modifier.height(280.dp)) {
            Image(
                painter = painterResource(id = R.drawable.top_background),
                contentDescription = "",
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,

                ) {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = "",

                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .background(Color.Gray),
                )
            }
        }

        Text(
            text = user.userName,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp),
            color = Color(android.graphics.Color.parseColor("#747679")),
        )
        Text(
            text = user.email,
            fontSize = 20.sp,
            color = Color(android.graphics.Color.parseColor("#747679")),
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp, 24.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White,
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Bilgiler",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5),
                    modifier = Modifier.padding(5.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Adı: ${user.firstName}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp),
                )
                Text(
                    text = "Soyadı: ${user.surname}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp),
                )
                Text(
                    text = "Pozisyon: ${user.position}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp),
                )
                Text(
                    text = "Puan: ${user.point}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp),
                )
                Text(
                    text = "Iban: ${user.iban}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }

        Button(
            onClick = { showDialog = true },
            Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)
                .height(55.dp), colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(android.graphics.Color.parseColor("#ffffff"))
            ), shape = RoundedCornerShape(15)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 5.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Profil Ayarları",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }

        Button(
            onClick = {
                viewModel.logout()
                // navController.popBackStack()
                navController.navigate(AuthenticationScreens.LoginScreen) {
                    popUpTo(navController.graph.id) {
                        inclusive = false

                    }
                }
            },
            Modifier
                .padding(start = 32.dp, end = 32.dp, bottom = 10.dp)
                .height(55.dp), colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(android.graphics.Color.parseColor("#ffffff"))
            ), shape = RoundedCornerShape(15)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 5.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Çıkış Yap",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showDialog) {
        ProfileSettingsDialog(onDismiss = { showDialog = false }, user)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileSettingsDialog(
    onDismiss: () -> Unit,
    user: User,
    viewModel: ProfileViewModel = hiltViewModel(),
) {

    var imageUri by remember { mutableStateOf<Uri?>(Uri.parse(user.photoUrl) ) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {resultUri ->
            resultUri?.let {
                viewModel.updatePhoto(user,resultUri)
                imageUri=resultUri
            }}
    )

    var surname by remember { mutableStateOf(user.surname) }
    var position by remember { mutableStateOf(user.position) }
    var firstname by remember { mutableStateOf(user.firstName) }
    var iban by remember { mutableStateOf(user.iban) }

    Dialog(onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium, elevation = 8.dp) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Profili Güncelle")

                GlideImage(
                    model = imageUri, contentDescription = "", modifier = Modifier
                        .padding(4.dp)
                        .size(150.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Button(
                    onClick = {
                        launcher.launch("image/*")
                    }
                ) {
                    Text(text = "Profil Fotoğrafı Değiştir")
                }

                OutlinedTextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    label = { Text(text = "İsim") }
                )

                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text(text = "Soyisim") }
                )

                OutlinedTextField(
                    value = position,
                    onValueChange = { position = it },
                    label = { Text(text = "Pozisyon") }
                )
                OutlinedTextField(
                    value = iban,
                    onValueChange = { iban = it },
                    label = { Text(text = "Iban") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onDismiss) {
                        Text(text = "İptal")
                    }
                    Button(onClick = {

                        user.firstName = firstname
                        user.surname = surname
                        user.position = position
                        user.iban = iban

                        viewModel.updateProfileInfo(user)
                        onDismiss()
                    }) {
                        Text(text = "Ekle")
                    }
                }
            }
        }
    }
}