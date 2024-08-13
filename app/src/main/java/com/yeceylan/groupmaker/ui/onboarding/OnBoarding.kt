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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.domain.model.OnBoardingData
import com.yeceylan.groupmaker.ui.auth.navigation.AuthenticationScreens
import com.yeceylan.groupmaker.ui.theme.Dimen.font_size_l
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_ccc
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_l
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_s1
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_s2
import com.yeceylan.groupmaker.ui.theme.Dimen.spacing_xxxvi
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
            imageR = R.drawable.onboarding1,
        ),
        OnBoardingData(
            titleR = R.string.onBoardingTitle2,
            textR = R.string.onBoardingText2,
            imageR = R.drawable.onboarding2,
        ),
        OnBoardingData(
            titleR = R.string.onBoardingTitle3,
            textR = R.string.onBoardingText3,
            imageR = R.drawable.onboarding3,
        ),
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoarding(navController: NavController) {

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        TopSection(navController)
        val item = getData()
        val state = rememberPagerState(pageCount = item.size)

        HorizontalPager(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.8f),

            ) { page ->
            OnBoardingItem(item = item[page])
        }

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

@Composable
fun TopSection(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacing_s2, spacing_xxxvi),
    ) {
        TextButton(
            onClick = {
                navController.popBackStack()
                navController.navigate(AuthenticationScreens.LoginScreen)
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(
                text = stringResource(id = R.string.skip),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun BoxScope.Indicators(size: Int, index: Int) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing_s2),
        modifier = Modifier.align(Alignment.CenterStart),
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
fun BottomSection(size: Int, index: Int, onNextClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacing_s2),
    ) {
        Indicators(size = size, index = index)
        FloatingActionButton(
            onClick = onNextClicked,
            modifier = Modifier.align(Alignment.CenterEnd),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {

    val width = animateDpAsState(
        targetValue = if (isSelected) spacing_l else spacing_s1,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
        label = "",
    )

    Box(
        modifier = Modifier
            .height(spacing_s1)
            .width(width = width.value)
            .clip(shape = CircleShape)
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                }
            ),
    ) {

    }
}

@Composable
fun OnBoardingItem(item: OnBoardingData) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = item.imageR),
            contentDescription = null,
            Modifier.size(spacing_ccc),
        )

        Text(
            text = stringResource(id = item.titleR),
            fontSize = font_size_l,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = stringResource(id = item.textR),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
        )
    }
}