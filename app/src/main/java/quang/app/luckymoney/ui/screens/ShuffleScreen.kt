package quang.app.luckymoney.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import quang.app.luckymoney.ui.components.Envelope
import quang.app.luckymoney.ui.components.RadialGradientBackground
import quang.app.luckymoney.ui.theme.*
import kotlin.random.Random

@Composable
fun ShuffleScreen(
    onShuffleComplete: () -> Unit
) {
    val duration = 3000L
    
    LaunchedEffect(Unit) {
        delay(duration)
        onShuffleComplete()
    }

    RadialGradientBackground(
        brush = ShuffleBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            
            Text(
                text = "XÁO BÀI...",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TetGold,
                    fontFamily = NotoSerifDisplay,
                    fontSize = 32.sp,
                    letterSpacing = 4.sp
                )
            )
        }

        // Animated Shuffle Pile
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            repeat(8) { index ->
                ShufflingEnvelope(index)
            }
        }
    }
}

@Composable
fun ShufflingEnvelope(index: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "shuffling")
    
    val xOffset by infiniteTransition.animateFloat(
        initialValue = Random.nextInt(-60, 60).toFloat(),
        targetValue = Random.nextInt(-60, 60).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "xOffset"
    )

    val yOffset by infiniteTransition.animateFloat(
        initialValue = Random.nextInt(-40, 40).toFloat(),
        targetValue = Random.nextInt(-40, 40).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = Random.nextInt(-15, 15).toFloat(),
        targetValue = Random.nextInt(-15, 15).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
            .graphicsLayer {
                rotationZ = rotation
            }
    ) {
        Box(
            modifier = Modifier
                .size(100.dp, 140.dp)
                .background(TetEnvelopeRed, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "福",
                    color = TetGold.copy(alpha = 0.5f),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
