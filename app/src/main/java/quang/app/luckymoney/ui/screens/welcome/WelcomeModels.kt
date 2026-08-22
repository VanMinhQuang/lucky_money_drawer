package quang.app.luckymoney.ui.screens.welcome

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.Color

internal data class GlintSpec(val xFraction: Float, val yFraction: Float, val delayMs: Int)

internal data class CoinParticleSpec(val id: Long, val angle: Float, val dist: Float, val delayMs: Long)

internal data class BurstFrame(val dx: Float, val dy: Float, val scale: Float, val alpha: Float)
