package com.example.nanitbirthdayapp.ui.birthday

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.nanitbirthdayapp.R
import com.example.nanitbirthdayapp.ui.birthday.constants.BirthdayConst
import com.example.nanitbirthdayapp.ui.theme.BirthdayTheme
import com.example.nanitbirthdayapp.ui.theme.getCameraIconResource
import com.example.nanitbirthdayapp.ui.theme.getBabyImageBorderColor
import com.example.nanitbirthdayapp.ui.theme.getDefaultBabyImageResource

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
        BabyImageWithCamera(
            pictureUri = imageUri,
            onCameraClick = onCameraClick,
            theme = theme,
            showCameraIcon = true
        )

        Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenPhotoAndLogo))

        NanitLogo()
    }
}

@Composable
private fun BabyImageWithCamera(
    pictureUri: Uri?,
    onCameraClick: () -> Unit,
    theme: BirthdayTheme,
    showCameraIcon: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(BirthdayConst.Dimens.babyImageSize),
        contentAlignment = Alignment.Center
    ) {
        BabyImage(
            pictureUri = pictureUri,
            theme = theme,
            modifier = Modifier.matchParentSize()
        )

        if (showCameraIcon) {
            val imageRadius = BirthdayConst.Dimens.babyImageSize / 2
            val offSet = imageRadius.value * 0.707f // cos45 degrees

            AddPhotoButton(
                theme = theme,
                onClick = onCameraClick,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = offSet.dp,
                        y = -offSet.dp
                    )
            )
        }
    }
}

@Composable
private fun AddPhotoButton(
    theme: BirthdayTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(BirthdayConst.Dimens.cameraButtonSize),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent
        )
    ) {
        Icon(
            painter = painterResource(id = theme.getCameraIconResource()),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = Color.Unspecified
        )
    }
}

@Composable
private fun BabyImage(
    pictureUri: Uri?,
    theme: BirthdayTheme,
    modifier: Modifier = Modifier
) {
    val borderColor = theme.getBabyImageBorderColor()

    val imageModifier = modifier
        .border(
            width = BirthdayConst.Dimens.babyImageBorderWidth,
            color = borderColor,
            shape = CircleShape
        )
        .clip(CircleShape)

    if (pictureUri != null) {
        key(pictureUri.toString()) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pictureUri)
                    .memoryCachePolicy(CachePolicy.DISABLED)
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .crossfade(true)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = "Baby's photo",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        }
    } else {
        Image(
            painter = painterResource(id = theme.getDefaultBabyImageResource()),
            contentDescription = "Default baby photo",
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun NanitLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.logo_nanit),
        contentDescription = "Nanit Logo",
        modifier = modifier.size(
            width = BirthdayConst.Dimens.nanitLogoWidth,
            height = BirthdayConst.Dimens.nanitLogoHeight
        )
    )
}
