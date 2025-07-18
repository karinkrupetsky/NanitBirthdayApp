package com.example.nanitbirthdayapp.ui.birthday

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.nanitbirthdayapp.R
import com.example.nanitbirthdayapp.util.Age
import com.example.nanitbirthdayapp.ui.birthday.constants.BirthdayConst

@Composable
fun AgeContentSection(
    name: String,
    age: Age,
    @DrawableRes ageNumberRes: Int
) {
    val ageTextRes = when {
        age.unit.name.contains("YEAR", ignoreCase = true) -> if (age.number == 1) R.string.year_old else R.string.years_old
        age.unit.name.contains("MONTH", ignoreCase = true) -> if (age.number == 1) R.string.month_old else R.string.months_old
        else -> R.string.years_old
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.today_birthday, name).uppercase(),
            style = BirthdayConst.TextStyles.ageTitle,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BirthdayConst.Dimens.ageTitleHorizontalPadding)
        )
        
        Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenAgeTitle))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_left_swirls),
                contentDescription = null,
                modifier = Modifier.size(BirthdayConst.Dimens.ageDecorationSize)
            )
            Image(
                painter = painterResource(id = ageNumberRes),
                contentDescription = "Age number",
                modifier = Modifier
                    .size(BirthdayConst.Dimens.ageNumberSize)
                    .padding(horizontal = BirthdayConst.Dimens.ageNumberPadding)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_right_swirls),
                contentDescription = null,
                modifier = Modifier.size(BirthdayConst.Dimens.ageDecorationSize)
            )
        }

        Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenAgeNumber))
        
        Text(
            text = stringResource(id = ageTextRes).uppercase(),
            style = BirthdayConst.TextStyles.ageUnit,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
