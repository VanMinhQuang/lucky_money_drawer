package quang.app.luckymoney.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import quang.app.luckymoney.ui.components.CapsuleButton
import quang.app.luckymoney.ui.components.Lantern
import quang.app.luckymoney.ui.components.RadialGradientBackground
import quang.app.luckymoney.ui.theme.*

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {
    RadialGradientBackground(
        brush = WelcomeBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Year Pill
            Surface(
                color = Color.Transparent,
                shape = RoundedCornerShape(999.dp),
                border = border(1.dp, TetGold.copy(alpha = 0.5f)),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "YEAR OF THE SNAKE",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = TetGold,
                        fontSize = 12.sp
                    )
                )
            }

            // Animated Title with Glow
            val infiniteTransition = rememberInfiniteTransition(label = "glow")
            val blurRadius by infiniteTransition.animateFloat(
                initialValue = 12f,
                targetValue = 28f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "blur"
            )
            val glowColor by infiniteTransition.animateColor(
                initialValue = TetGold,
                targetValue = TetDeepOrange,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "color"
            )

            Text(
                text = "Lì Xì\nMay Mắn",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 64.sp,
                    lineHeight = 64.sp,
                    color = TetGold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                modifier = Modifier
                    .drawBehind {
                        val paint = Paint().asFrameworkPaint()
                        paint.color = glowColor.toArgb()
                        paint.setShadowLayer(blurRadius, 0f, 0f, glowColor.toArgb())
                        drawIntoCanvas { canvas ->
                            canvas.nativeCanvas.drawText("", 0f, 0f, paint)
                        }
                    }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Welcome Envelope
            Box(
                modifier = Modifier
                    .size(160.dp, 220.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TetEnvelopeRed)
                    .border(2.dp, TetGold.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            ) {
                Text(
                    text = "🧧",
                    fontSize = 80.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            CapsuleButton(
                text = "Bắt Đầu",
                onClick = onStartClick,
                modifier = Modifier.width(240.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun WelcomeScreenPreview() {
    LuckyMoneyTheme {
        WelcomeScreen(onStartClick = {})
    }
}

@Composable
private fun border(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)
