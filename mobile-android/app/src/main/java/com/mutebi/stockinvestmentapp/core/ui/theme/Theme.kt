package com.mutebi.stockinvestmentapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkScheme = darkColorScheme(
    primary = ElectricBlue,
    secondary = GoldAccent,
    tertiary = EmeraldSignal,
    background = DarkBackground,
    surface = SurfaceDark,
    onPrimary = PureWhite,
    onSecondary = Slate950,
    onBackground = PureWhite,
    onSurface = PureWhite,
    error = RoseAlert
)

private val LightScheme = lightColorScheme(
    primary = ElectricBlue,
    secondary = GoldAccent,
    tertiary = EmeraldSignal,
    background = LightBackground,
    surface = PureWhite,
    onPrimary = PureWhite,
    onSecondary = Slate950,
    onBackground = Slate900,
    onSurface = Slate900,
    error = RoseAlert
)

@Composable
fun StockAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = AppTypography,
        content = content
    )
}