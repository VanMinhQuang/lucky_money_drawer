package quang.app.luckymoney.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import quang.app.luckymoney.ui.theme.TetEnvelopeRed
import quang.app.luckymoney.ui.theme.TetFlapRed
import quang.app.luckymoney.ui.theme.TetGold

@Composable
fun Envelope(
    modifier: Modifier = Modifier,
    isOpen: Boolean = false,
    label: String = "Lì Xì",
    content: @Composable BoxScope.() -> Unit = {}
) {
    val flapRotation by animateFloatAsState(
        targetValue = if (isOpen) -185f else 0f,
        animationSpec = tween(durationMillis = 900, easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)),
        label = "flapRotation"
    )

    Box(
        modifier = modifier
            .width(160.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(TetEnvelopeRed),
        contentAlignment = Alignment.Center
    ) {
        // Content (e.g. Money Bill)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }

        // Flap
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    rotationX = flapRotation
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f)
                    cameraDistance = 12f * density
                }
                .clip(RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
                .background(TetFlapRed)
        )

        // Label & Decoration
        if (!isOpen || flapRotation > -90f) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = TetGold,
                    modifier = Modifier.size(24.dp).rotate(45f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TetGold,
                        fontFamily = quang.app.luckymoney.ui.theme.NotoSerifDisplay,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}
