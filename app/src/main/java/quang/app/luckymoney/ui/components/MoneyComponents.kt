package quang.app.luckymoney.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import quang.app.luckymoney.ui.theme.NotoSerifDisplay
import quang.app.luckymoney.ui.theme.TetGold
import quang.app.luckymoney.ui.theme.TetOrange
import quang.app.luckymoney.utils.MoneyUtils
import kotlin.math.*

/**
 * A text component with a pulsating glow animation.
 */
@Composable
fun GlowingMoneyText(
    text: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "GlowTransition")
    
    val glowColor by infiniteTransition.animateColor(
        initialValue = TetGold.copy(alpha = 0.5f),
        targetValue = TetOrange.copy(alpha = 0.9f),
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowColor"
    )

    val shadowBlur by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ShadowBlur"
    )

    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = TetGold,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            fontFamily = NotoSerifDisplay,
            shadow = Shadow(
                color = glowColor,
                offset = Offset.Zero,
                blurRadius = shadowBlur
            )
        )
    )
}

/**
 * A glowing white money bill icon with a rotating starburst "shining" effect.
 */
@Composable
fun ShiningMoneyIcon(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ShiningMoneyTransition")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing)
        ),
        label = "ShiningRotation"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAlpha"
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        // Rotating Starburst Background
        Canvas(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = glowAlpha }
        ) {
            val rayCount = 12
            val maxRadius = size.maxDimension * 0.8f
            val center = Offset(size.width / 2, size.height / 2)

            rotate(rotation) {
                for (i in 0 until rayCount) {
                    val angle = (360f / rayCount) * i
                    val path = Path().apply {
                        moveTo(center.x, center.y)
                        val x1 = center.x + maxRadius * cos(Math.toRadians(angle.toDouble() - 10)).toFloat()
                        val y1 = center.y + maxRadius * sin(Math.toRadians(angle.toDouble() - 10)).toFloat()
                        val x2 = center.x + maxRadius * cos(Math.toRadians(angle.toDouble() + 10)).toFloat()
                        val y2 = center.y + maxRadius * sin(Math.toRadians(angle.toDouble() + 10)).toFloat()
                        lineTo(x1, y1)
                        lineTo(x2, y2)
                        close()
                    }
                    drawPath(
                        path = path,
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.7f),
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = maxRadius
                        )
                    )
                }
            }
        }

        // The White Money Bill
        Canvas(modifier = Modifier.size(110.dp, 55.dp)) {
            val width = size.width
            val height = size.height
            
            // Main body - Pure white
            drawRoundRect(
                color = Color.White,
                cornerRadius = CornerRadius(8.dp.toPx()),
                size = size
            )
            
            // Inner pattern (emblem area) - Light grey
            drawCircle(
                color = Color(0xFFF5F5F5),
                radius = height * 0.3f,
                center = center
            )
            
            // Border - Very light silver
            drawRoundRect(
                color = Color(0xFFE0E0E0),
                cornerRadius = CornerRadius(8.dp.toPx()),
                size = size,
                style = Stroke(width = 2.dp.toPx())
            )
            
            // Guilloche-like details (simple lines)
            val strokeWidth = 1.dp.toPx()
            drawLine(
                color = Color(0xFFEEEEEE),
                start = Offset(width * 0.2f, height * 0.5f),
                end = Offset(width * 0.35f, height * 0.5f),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color(0xFFEEEEEE),
                start = Offset(width * 0.65f, height * 0.5f),
                end = Offset(width * 0.8f, height * 0.5f),
                strokeWidth = strokeWidth
            )
        }
    }
}

/**
 * A custom Money Icon drawn using Vector-like Canvas.
 */
@Composable
fun CustomMoneyIcon(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(64.dp)) {
        val width = size.width
        val height = size.height
        
        // Draw the bill rectangle
        drawRect(
            color = Color(0xFF2D8B4E),
            topLeft = Offset(width * 0.1f, height * 0.3f),
            size = size.copy(width = width * 0.8f, height = height * 0.4f)
        )
        
        // Draw the central circle (president/emblem area)
        drawCircle(
            color = Color(0xFFE8F5E9),
            radius = width * 0.15f,
            center = Offset(width * 0.5f, height * 0.5f)
        )
        
        // Draw the border
        drawRect(
            color = TetGold,
            topLeft = Offset(width * 0.1f, height * 0.3f),
            size = size.copy(width = width * 0.8f, height = height * 0.4f),
            style = Stroke(width = 2.dp.toPx())
        )
        
        // Dollar/Currency sign simplified
        drawCircle(
            color = TetGold,
            radius = width * 0.05f,
            center = Offset(width * 0.5f, height * 0.5f),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

/**
 * The full stack revealed from the envelope.
 */
@Composable
fun RevealedMoneyStack(
    amount: Int,
    modifier: Modifier = Modifier
) {
    val moneyBreakdown = MoneyUtils.getMoneyBreakdown(amount)
    val formattedAmount = MoneyUtils.formatCurrency(amount)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top: Total amount text with Shining/Glowing animation
        GlowingMoneyText(text = formattedAmount)

        // Middle: Custom Money Icon
        CustomMoneyIcon(modifier = Modifier.size(80.dp))

        // Bottom: Vertical stack or overlapping fan of money images
        MoneyBillFan(amount = amount)
    }
}

/**
 * An overlapping fan of money bill images, sized proportionally to the breakdown.
 */
@Composable
fun MoneyBillFan(
    amount: Int,
    modifier: Modifier = Modifier,
    billWidth: androidx.compose.ui.unit.Dp = 180.dp,
    billHeight: androidx.compose.ui.unit.Dp = 90.dp,
    fanHeight: androidx.compose.ui.unit.Dp = 240.dp
) {
    val moneyBreakdown = MoneyUtils.getMoneyBreakdown(amount)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(fanHeight),
        contentAlignment = Alignment.BottomCenter
    ) {
        moneyBreakdown.forEachIndexed { index, drawableRes ->
            val rotation = (index - (moneyBreakdown.size - 1) / 2f) * 10f
            val offsetX = (billWidth * 0.11f) * (index - (moneyBreakdown.size - 1) / 2f)

            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = null,
                modifier = Modifier
                    .width(billWidth)
                    .height(billHeight)
                    .offset(x = offsetX, y = (-10).dp * index)
                    .rotate(rotation)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                    .graphicsLayer {
                        shadowElevation = 4f
                    },
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF9A0000)
@Composable
fun RevealedMoneyStackPreview() {
    RevealedMoneyStack(amount = 17000)
}
