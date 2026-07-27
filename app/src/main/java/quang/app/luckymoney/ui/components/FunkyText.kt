package quang.app.luckymoney.ui.components

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.graphics.drawscope.DrawStyle
import quang.app.luckymoney.ui.theme.*
import kotlin.math.*

/**
 * Playful per-character text: each letter bobs, wiggles and shifts color on its own phase,
 * so the whole word feels alive instead of a static label.
 */
@Composable
fun FunkyText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 40.sp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "FunkyTextPhase")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing)
        ),
        label = "FunkyPhase"
    )

    Row(modifier = modifier) {
        text.forEachIndexed { index, char ->
            val charPhase = phase + index * 0.6f
            val bounce = sin(charPhase) * 10f
            val wiggle = sin(charPhase * 0.8f + 1f) * 8f
            
            Box(
                modifier = Modifier.graphicsLayer {
                    translationY = bounce
                    rotationZ = wiggle - 4f // Comic-style tilt
                },
                contentAlignment = Alignment.Center
            ) {
                // Thicker stroke by drawing 4 diagonals
                listOf(
                    Offset(-1f, -1f), Offset(1f, -1f), 
                    Offset(-1f, 1f), Offset(1f, 1f)
                ).forEach { offset ->
                    Text(
                        text = char.toString(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = ComicPurple,
                            fontFamily = NotoSerifDisplay,
                            fontWeight = FontWeight.Black,
                            fontSize = fontSize,
                        ),
                        modifier = Modifier.offset(
                            x = (offset.x * 2.5).dp, 
                            y = (offset.y * 2.5).dp
                        )
                    )
                }

                Text(
                    text = char.toString(),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        brush = Brush.verticalGradient(
                            colors = listOf(ComicCyan, ComicBlue)
                        ),
                        fontFamily = NotoSerifDisplay,
                        fontWeight = FontWeight.Black,
                        fontSize = fontSize
                    )
                )
            }
        }
    }
}

/**
 * Rotating sunburst rays + a pulsing glow, meant to sit behind a big reveal so it
 * reads as "shining" rather than a flat background.
 */
@Composable
fun ShineBurst(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ShineBurst")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing)
        ),
        label = "ShineRotation"
    )

    // Sparkle animations
    val sparkleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "SparkleAlpha"
    )

    val sparkleData = remember {
        val random = java.util.Random(42)
        List(15) {
            val dist = random.nextFloat()
            val angle = random.nextFloat() * 360f
            val sizeMult = random.nextFloat()
            val alphaMult = random.nextFloat()
            Triple(dist, angle, sizeMult to alphaMult)
        }
    }

    Canvas(modifier = modifier) {
        val rayCount = 30
        val maxRadius = size.maxDimension
        val center = Offset(size.width / 2, size.height / 2)

        rotate(rotation) {
            for (i in 0 until rayCount) {
                val angle = (360f / rayCount) * i
                val path = Path().apply {
                    moveTo(center.x, center.y)
                    val x1 = center.x + maxRadius * cos(Math.toRadians(angle.toDouble() - 6)).toFloat()
                    val y1 = center.y + maxRadius * sin(Math.toRadians(angle.toDouble() - 6)).toFloat()
                    val x2 = center.x + maxRadius * cos(Math.toRadians(angle.toDouble() + 6)).toFloat()
                    val y2 = center.y + maxRadius * sin(Math.toRadians(angle.toDouble() + 6)).toFloat()
                    lineTo(x1, y1)
                    lineTo(x2, y2)
                    close()
                }
                drawPath(
                    path = path,
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.8f), 
                            Color.White.copy(alpha = 0.4f), 
                            Color.White.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = maxRadius
                    )
                )
            }
        }

        // Add small, animated sparkles and stars
        sparkleData.forEach { (distFactor: Float, angle: Float, factors: Pair<Float, Float>) ->
            val dist = distFactor * maxRadius * 0.7f
            val x = center.x + dist * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = center.y + dist * sin(Math.toRadians(angle.toDouble())).toFloat()
            val sparkleSize = (factors.first * 6f + 4f).dp.toPx()
            
            val sparklePath = Path().apply {
                moveTo(x, y - sparkleSize)
                lineTo(x + sparkleSize * 0.15f, y - sparkleSize * 0.15f)
                lineTo(x + sparkleSize, y)
                lineTo(x + sparkleSize * 0.15f, y + sparkleSize * 0.15f)
                lineTo(x, y + sparkleSize)
                lineTo(x - sparkleSize * 0.15f, y + sparkleSize * 0.15f)
                lineTo(x - sparkleSize, y)
                lineTo(x - sparkleSize * 0.15f, y - sparkleSize * 0.15f)
                close()
            }
            drawPath(
                path = sparklePath,
                color = Color.White.copy(alpha = sparkleAlpha * (0.5f + 0.5f * factors.second))
            )
        }

        // Bright white center glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, Color.White.copy(alpha = 0.5f), Color.Transparent),
                center = center,
                radius = size.minDimension * 0.45f
            ),
            radius = size.minDimension * 0.45f
        )
    }
}
