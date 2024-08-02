package com.yeceylan.groupmaker.ui.onboarding

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.domain.model.OnBoardingData
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.theme.GroupMakerTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
private fun OnBoardinPreview() {
    GroupMakerTheme {
        OnBoarding(navController = rememberNavController())
    }
}

fun getData(): List<OnBoardingData> {
    return listOf(
        OnBoardingData(
            titleR = R.string.onBoardingTitle1,
            textR = R.string.onBoardingText1,
            imageR = R.drawable.onboarding1
        ),
        OnBoardingData(
            titleR = R.string.onBoardingTitle2,
            textR = R.string.onBoardingText2,
            imageR = R.drawable.onboarding2
        ),
        OnBoardingData(
            titleR = R.string.onBoardingTitle3,
            textR = R.string.onBoardingText3,
            imageR = R.drawable.onboarding3
        ),
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoarding(navController: NavController) {

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        //Top alanını oluşturan compose çağırıyoruz
        TopSection(navController)
        //OnBoardingData sınıfından OnBoarding ekran sayısını alıyoruz
        val item = getData()
        val state = rememberPagerState(pageCount = item.size)
        //OnBoardingItem'a item resim ve yazıları arayüz elementlerine aktarmasını
        //sağlıyoruz
        HorizontalPager(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.8f)
        ) { page ->
            OnBoardingItem(item = item[page])
        }
        //Ekran sayısını BottomSection compose da kullanarak pager ve scrool işlemi
        BottomSection(size = item.size, index = state.currentPage) {
            if (state.currentPage + 1 < item.size) {
                scope.launch {
                    state.scrollToPage(page = state.currentPage + 1)
                }
            } else {
                navController.popBackStack()
                navController.navigate(AuthenticationScreens.LoginScreen)

            }
        }
    }
}

//Top alanını oluşturan compose
@Composable
fun TopSection(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        //Skip adlı text buttonunu oluşturma
        TextButton(
            onClick = {
                navController.popBackStack()
                navController.navigate(AuthenticationScreens.LoginScreen)
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(
                text = "Skip",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

//Pager görselini ekran sayısı kadar çoğaltan compose
@Composable
fun BoxScope.Indicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.align(Alignment.CenterStart)
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

//Bottom alanındaki FloatingActionButton ve Pager göstergesini oluşturma
@Composable
fun BottomSection(size: Int, index: Int, onNextClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {//Pager bölümünü oluşturan compose çağırıyoruz
        Indicators(size = size, index = index)
        //Sağdaki FloatingActionButton rengini, iconunu tanımalama
        FloatingActionButton(
            onClick = onNextClicked,
            modifier = Modifier.align(Alignment.CenterEnd),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null)
        }
    }
}

//Bir tane Circle şeklinde pager oluşturma
@Composable
fun Indicator(isSelected: Boolean) {
    //pager arasında geçiş yaparkenki animasyonu sağlayan bölüm
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy), label = ""
    )
    //Pager alanı için yükseklik, şekil vb görsel özelliklerini tanımlama
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width = width.value)
            .clip(shape = CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.5f
                )
            )
    ) {}

}

//OnBoardingData sınıfından gelen resim ve yazıların arayüz elementlerine aktarılması
@Composable
fun OnBoardingItem(item: OnBoardingData) {
    //İçereklerin konumu belirleniyorz
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize()
    ) {
        //Resim, image özelliğine atanıyor
        Image(
            painter = painterResource(id = item.imageR), contentDescription = null,
            Modifier.size(300.dp)
        )
        //Bold ana başlık Text özelliğine atanıyor
        Text(
            text = stringResource(id = item.titleR),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        //Açıklama yazısı Text özelliğine atanıyor
        Text(
            text = stringResource(id = item.textR),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}