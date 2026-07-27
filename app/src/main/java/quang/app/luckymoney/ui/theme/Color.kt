package quang.app.luckymoney.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

// Basic Palette
val TetRed = Color(0xFFC41E3A)
val TetDarkRed = Color(0xFF9A0000)
val TetDeepRed = Color(0xFF6B0000)
val TetDarkestRed = Color(0xFF3D0000)
val TetBlackRed = Color(0xFF1A0000)
val TetShadowRed = Color(0xFF2A0000)

val TetGold = Color(0xFFFFD700)
val TetOrange = Color(0xFFFFA500)
val TetDeepOrange = Color(0xFFFF8C00)

val ComicCyan = Color(0xFF00E5FF)
val ComicBlue = Color(0xFF007BFF)
val ComicPurple = Color(0xFF480082)
val ComicGold = Color(0xFFFFD700)

val TetEnvelopeRed = Color(0xFFC41E3A)
val TetFlapRed = Color(0xFFB01020)
val MoneyGreen = Color(0xFF2D8B4E)
val TextPeach = Color(0xFFDDB5B5) // Analysis said FFDDB5, which is light peach
val TetPeach = Color(0xFFFFB7C5) // Cherry Blossom Pink

// Gradients
val PrimaryButtonGradient = Brush.linearGradient(
    colors = listOf(TetGold, TetOrange, TetDeepOrange)
)

// Background Gradients
val WelcomeBackground = Brush.radialGradient(
    0.0f to Color(0xFF9A0000),
    0.35f to Color(0xFF6B0000),
    1.0f to Color(0xFF3D0000)
)

val SetupBackground = Brush.radialGradient(
    0.0f to Color(0xFF8B0000),
    0.50f to Color(0xFF4D0000),
    1.0f to Color(0xFF2A0000)
)

val ShuffleBackground = Brush.radialGradient(
    0.0f to Color(0xFF8B0000),
    0.50f to Color(0xFF4D0000),
    1.0f to Color(0xFF1A0000)
)

val SelectionBackground = Brush.radialGradient(
    0.0f to Color(0xFF8B0000),
    0.60f to Color(0xFF3D0000),
    1.0f to Color(0xFF1A0000)
)
