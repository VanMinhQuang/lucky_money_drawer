package quang.app.luckymoney.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import quang.app.luckymoney.ui.theme.PrimaryButtonGradient
import quang.app.luckymoney.ui.theme.TetButtonText
import quang.app.luckymoney.ui.theme.TetGold
import quang.app.luckymoney.ui.theme.TetMai
import quang.app.luckymoney.ui.theme.TetPeach
import quang.app.luckymoney.ui.theme.TetRed
import kotlin.random.Random

val AppEasing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)

@Composable
fun Modifier.shimmerEffect(): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val xOffset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "xOffset"
    )

    return this.drawWithCache {
        val brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.0f),
                Color.White.copy(alpha = 0.3f),
                Color.White.copy(alpha = 0.6f),
                Color.White.copy(alpha = 0.3f),
                Color.White.copy(alpha = 0.0f),
            ),
            start = Offset(xOffset * size.width, 0f),
            end = Offset((xOffset + 1f) * size.width, size.height)
        )
        onDrawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.SrcAtop)
        }
    }
}

@Composable
fun RadialGradientBackground(
    brush: Brush,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush),
        contentAlignment = Alignment.Center
    ) {
        FallingBlossoms()
        
        // Background Decorations
        Lantern(Modifier.align(Alignment.TopStart).padding(horizontal = 24.dp, vertical = 32.dp))
        Lantern(Modifier.align(Alignment.TopEnd).padding(horizontal = 24.dp, vertical = 32.dp))
        
        content()
    }
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    // Pulsing glow animation (inspired by WelcomeScreen design)
    val infiniteTransition = rememberInfiniteTransition(label = "AppButtonGlow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth(0.88f)
            .heightIn(min = 58.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer Glow Layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    alpha = pulseAlpha
                    scaleX = 1.06f
                    scaleY = 1.3f
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(TetGold.copy(alpha = 0.6f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(999.dp)
                )
        )

        // The Main Button Container
        Surface(
            onClick = onClick,
            enabled = enabled && !isLoading,
            shape = RoundedCornerShape(999.dp),
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(999.dp))
                .background(PrimaryButtonGradient)
                .shimmerEffect()
                .border(1.5.dp, TetGold.copy(alpha = 0.5f), RoundedCornerShape(999.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = TetButtonText
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "ĐANG XỬ LÝ...",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TetButtonText,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        )
                    )
                } else {
                    Text(
                        text = text.uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TetButtonText,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = TetButtonText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CapsuleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(58.dp)
            .background(
                brush = PrimaryButtonGradient,
                shape = RoundedCornerShape(999.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(999.dp),
        contentPadding = PaddingValues(horizontal = 40.dp),
        enabled = enabled
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        )
    }
}

@Composable
fun Lantern(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "lantern")
    
    // Bobbing animation
    val translationY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobbing"
    )
    
    // Pendulum (swinging) animation
    val rotationZ by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "swinging"
    )

    Column(
        modifier = modifier.graphicsLayer {
            this.rotationZ = rotationZ
            this.translationY = translationY
            transformOrigin = TransformOrigin(0.5f, 0f)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // String
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(40.dp)
                .background(TetGold)
        )
        
        // Lantern Body
        Box(
            modifier = Modifier
                .size(44.dp, 64.dp)
                .border(2.dp, TetGold, RoundedCornerShape(22.dp))
                .background(TetRed, RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(6.dp)
                        .background(TetGold)
                )
                Text(
                    text = "福",
                    color = TetGold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(6.dp)
                        .background(TetGold)
                )
            }
        }
    }
}

@Composable
fun FallingBlossoms(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        FallingPeachBlossoms()
        FallingApricotBlossoms()
    }
}

@Composable
fun FallingApricotBlossoms(modifier: Modifier = Modifier) {
    FallingBlossomSystem(color = TetMai, modifier = modifier)
}

@Composable
fun FallingPeachBlossoms(modifier: Modifier = Modifier) {
    FallingBlossomSystem(color = TetPeach, modifier = modifier)
}

@Composable
fun FallingBlossomSystem(
    color: Color,
    modifier: Modifier = Modifier,
    petalCount: Int = 50
) {
    val infiniteTransition = rememberInfiniteTransition(label = "petals")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    val petals = remember {
        List(petalCount) {
            Petal(
                xOffset = Random.nextFloat(),
                yOffset = Random.nextFloat(),
                size = Random.nextFloat() * 8f + 4f,
                speed = Random.nextFloat() * 0.4f + 0.2f,
                rotationSpeed = Random.nextFloat() * 720f - 360f,
                opacity = Random.nextFloat() * 0.4f + 0.1f,
                swingAmplitude = Random.nextFloat() * 15f + 5f,
                swingFrequency = Random.nextFloat() * 2f + 1f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        petals.forEach { petal ->
            val yProgress = (petal.yOffset + time * petal.speed) % 1.0f
            val y = yProgress * height
            
            // Add some horizontal swinging
            val xSwing = Math.sin(time.toDouble() * petal.swingFrequency * Math.PI * 2 + petal.yOffset * 10).toFloat() * petal.swingAmplitude.dp.toPx()
            val x = (petal.xOffset * width) + xSwing
            
            withTransform({
                translate(x, y)
                rotate(petal.rotationSpeed * time + (yProgress * 360f))
            }) {
                drawRoundRect(
                    color = color.copy(alpha = petal.opacity),
                    size = Size(petal.size.dp.toPx(), (petal.size * 1.2f).dp.toPx()),
                    cornerRadius = CornerRadius(petal.size.dp.toPx() / 2)
                )
            }
        }
    }
}

private data class Petal(
    val xOffset: Float,
    val yOffset: Float,
    val size: Float,
    val speed: Float,
    val rotationSpeed: Float,
    val opacity: Float,
    val swingAmplitude: Float,
    val swingFrequency: Float
)
