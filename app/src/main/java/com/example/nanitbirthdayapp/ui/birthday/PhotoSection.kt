package com.example.nanitbirthdayapp.ui.birthday

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.nanitbirthdayapp.R
import com.example.nanitbirthdayapp.ui.birthday.constants.BirthdayConst
import com.example.nanitbirthdayapp.ui.theme.BirthdayTheme

@Composable
fun PhotoSection(
    imageUri: Uri?,
    theme: BirthdayTheme,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            val imageModifier = Modifier
                .size(BirthdayConst.Dimens.babyImageSize)
                .clip(CircleShape)

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Baby's photo",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = theme.defaultBabyImageRes),
                    contentDescription = "Default baby photo",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }

            Image(
                painter = painterResource(id = theme.addPhotoIconRes),
                contentDescription = "Add picture",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(BirthdayConst.Dimens.cameraButtonSize)
                    .clickable(onClick = onCameraClick)
            )
        }

        Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenPhotoAndLogo))

        Image(
            painter = painterResource(id = R.drawable.logo_nanit),
            contentDescription = null,
            modifier = Modifier
                .size(
                    width = BirthdayConst.Dimens.nanitLogoWidth,
                    height = BirthdayConst.Dimens.nanitLogoHeight
                )
        )
    }
}

