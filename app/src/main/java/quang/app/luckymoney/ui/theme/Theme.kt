package quang.app.luckymoney.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TetGold,
    secondary = TetCrimson,
    tertiary = TetMai,
    background = TetBlackRed,
    surface = TetDeepRed,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = TetGold,
    onSurface = TetGold
)

private val LightColorScheme = lightColorScheme(
    primary = TetRed,
    secondary = TetGold,
    tertiary = TetCrimson,
    background = TetDeepRed,
    surface = TetDarkRed,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TetGold,
    onSurface = TetGold
)

@Composable
fun LuckyMoneyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disable dynamic color for brand consistency as per React reference
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
