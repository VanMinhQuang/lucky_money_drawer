package quang.app.luckymoney.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

// Basic Palette
val TetRed = Color(0xFFB71C1C) // Imperial Red
val TetCrimson = Color(0xFFD32F2F) // Vibrant Crimson
val TetDarkRed = Color(0xFF9A0000)
val TetDeepRed = Color(0xFF6B0000)
val TetDarkestRed = Color(0xFF3D0000)
val TetBlackRed = Color(0xFF1A0000)
val TetShadowRed = Color(0xFF2A0000)

val TetGold = Color(0xFFFFD700) // Premium Gold
val TetGoldDark = Color(0xFFB8860B) // Darker Gold/Bronze
val TetTextWhite = Color(0xFFFFFFFF)
val TetOrange = Color(0xFFFFA500)
val TetDeepOrange = Color(0xFFFF8C00)
val TetMai = Color(0xFFFFEB3B) // Apricot Blossom Yellow

val ComicCyan = Color(0xFF00E5FF)
val ComicBlue = Color(0xFF007BFF)
val ComicPurple = Color(0xFF480082)
val ComicGold = Color(0xFFFFD700)

val TetEnvelopeRed = Color(0xFFB71C1C)
val TetFlapRed = Color(0xFF8B0000)
val MoneyGreen = Color(0xFF2D8B4E)
val TextPeach = Color(0xFFDDB5B5) 
val TetPeach = Color(0xFFFFB7C5) // Cherry Blossom Pink
val TetButtonText = Color(0xFF4A1004) // Deep brown-red for button text

// Gradients
val PrimaryButtonGradient = Brush.linearGradient(
    colors = listOf(TetGold, TetOrange, TetDeepOrange)
)

val GoldTextGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFEE58), TetGold, TetGoldDark)
)

// Background Gradients
val WelcomeBackground = Brush.radialGradient(
    0.0f to TetCrimson,
    0.35f to TetRed,
    1.0f to TetDarkRed
)

val SetupBackground = Brush.radialGradient(
    0.0f to TetRed,
    0.50f to TetDarkRed,
    1.0f to TetDeepRed
)

val ShuffleBackground = Brush.radialGradient(
    0.0f to TetRed,
    0.50f to TetDarkRed,
    1.0f to TetBlackRed
)

val WelcomeGradient = Brush.verticalGradient(
    0f to Color(0xFF6E0F14),
    0.55f to Color(0xFF4A0A0E),
    1f to Color(0xFF35070A)
)

val SelectionBackground = Brush.radialGradient(
    0.0f to TetRed,
    0.60f to TetDeepRed,
    1.0f to TetBlackRed
)
