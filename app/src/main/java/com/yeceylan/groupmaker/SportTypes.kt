package com.yeceylan.groupmaker

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.firestore.FirebaseFirestore

data class SportType2(val title: String, val image: String)

@Preview(showBackground = true)
@Composable
fun SportTypes() {

    val myList = remember {
        mutableStateListOf<SportType2>()
    }

    LaunchedEffect(key1 = true) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("typeCollection")
        docRef.get().addOnSuccessListener {

            val docList = it.documents
            Log.e("docList",docList.toString())

            for (document in docList){

                val docMap = document.data!!

                val image =docMap.get("image").toString()
                val title = docMap.get("title").toString()

                myList.add(SportType2(title,image))

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // Text(text = "Select a sport type ", Modifier.fillMaxWidth(), fontSize = 24.sp, textAlign = TextAlign.Center)
        LazyColumn() {

            items(myList) {

                ImageCard(painter = it.image, contentDescription = "", title = it.title)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageCard(
    painter: String, contentDescription: String, title: String, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                Log.e("card", "click")
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(20.dp),

        ) {
        Box(modifier = Modifier.height(200.dp)) {

            GlideImage(
                model = painter,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            // startY = 100f,
                        )
                    )
            ) {

            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}