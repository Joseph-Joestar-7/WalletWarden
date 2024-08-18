package com.example.walletwarden.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.walletwarden.R

val Ga_Maamli = FontFamily(Font(R.font.gamaamli_regular))

val AbrilFatface = FontFamily(
    Font(R.font.abrilfatface_regular)
)
val Cabin_Sketch= FontFamily(
    Font(R.font.cabinsketch_bold,FontWeight.Bold),
)
val Kalam= FontFamily(Font(R.font.kalam_bold,FontWeight.Bold))
// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = AbrilFatface,
        fontWeight = FontWeight.Normal,
    ),
    displayMedium = TextStyle(
        fontFamily = Ga_Maamli,
        fontWeight = FontWeight.Normal,
    ),
    displaySmall= TextStyle(
        fontFamily = Cabin_Sketch,
        fontWeight = FontWeight.Normal
    ),
    headlineLarge=TextStyle(fontFamily = Kalam,
        fontWeight = FontWeight.Bold)

)