package com.example.nanitbirthdayapp.ui.birthday

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.nanitbirthdayapp.R
import com.example.nanitbirthdayapp.data.model.BirthdayInfo
import com.example.nanitbirthdayapp.ui.theme.BirthdayTheme
import com.example.nanitbirthdayapp.util.AgeCalculator
import com.example.nanitbirthdayapp.ui.birthday.constants.BirthdayConst

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun BirthdayScreen(
    birthdayInfo: BirthdayInfo,
    imageUri: Uri?,
    onAddPictureClick: () -> Unit
) {
    val age = AgeCalculator.getAge(birthdayInfo.dob)
    val theme = BirthdayTheme.fromString(birthdayInfo.theme)
    val ageNumberRes = getNumberImageRes(age.number)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = BirthdayConst.Dimens.screenPaddingTop,
                    bottom = BirthdayConst.Dimens.screenPaddingBottom
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            AgeContentSection(
                name = birthdayInfo.name,
                age = age,
                ageNumberRes = ageNumberRes
            )

            Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenSections))

            PhotoSection(
                imageUri = imageUri,
                theme = theme,
                onCameraClick = {  },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Image(
            painter = painterResource(theme.overlayImageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun getNumberImageRes(number: Int): Int {
            return when (number) {
                1 -> R.drawable.number_1
                2 -> R.drawable.number_2
                3 -> R.drawable.number_3
                4 -> R.drawable.number_4
                5 -> R.drawable.number_5
                6 -> R.drawable.number_6
                7 -> R.drawable.number_7
                8 -> R.drawable.number_8
                9 -> R.drawable.number_9
                10 -> R.drawable.number_10
                11 -> R.drawable.number_11
                12 -> R.drawable.number_12
                else -> R.drawable.number_0
            }
        }
