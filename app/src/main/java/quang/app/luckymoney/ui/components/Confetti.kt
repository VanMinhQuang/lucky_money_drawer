package quang.app.luckymoney.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import quang.app.luckymoney.ui.theme.TetGold
import kotlin.random.Random

data class Spark(
    val x: Float,
    val y: Float,
    val radius: Float,
    val color: Color,
    val velocity: Offset
)

@Composable
fun ConfettiEffect(
    modifier: Modifier = Modifier,
    trigger: Boolean
) {
    if (!trigger) return

    val animatableProgress = remember { Animatable(0f) }
    
    val sparks = remember {
        List(100) {
            Spark(
                x = 0.5f,
                y = 0.2f,
                radius = Random.nextFloat() * 8f + 3f,
                color = when (Random.nextInt(3)) {
                    0 -> TetGold
                    1 -> Color(0xFFFFE082)
                    else -> Color.White
                },
                velocity = Offset(
                    (Random.nextFloat() - 0.5f) * 0.6f,
                    (Random.nextFloat() - 0.9f) * 0.7f
                )
            )
        }
    }

    LaunchedEffect(trigger) {
        if (trigger) {
            animatableProgress.snapTo(0f)
            animatableProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            )
        }
    }

    if (animatableProgress.value < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val progress = animatableProgress.value

            sparks.forEach { spark ->
                val currentX = width * (spark.x + spark.velocity.x * progress)
                val currentY = height * (spark.y + spark.velocity.y * progress + 0.4f * progress * progress)
                val currentAlpha = 1f - (progress * progress)

                drawCircle(
                    color = spark.color.copy(alpha = currentAlpha.coerceIn(0f, 1f)),
                    radius = spark.radius,
                    center = Offset(currentX, currentY)
                )
            }
        }
    }
}
