package quang.app.luckymoney.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import quang.app.luckymoney.ui.theme.NotoSerifDisplay
import quang.app.luckymoney.ui.theme.TetGold
import quang.app.luckymoney.utils.MoneyUtils

/**
 * The "prize moment": whatever was pulled out of an envelope flies to the center of the
 * screen, overlaying the whole grid, with a shining burst behind it and a bouncy entrance.
 * Tap anywhere (or the button) to dismiss and go back to the envelopes.
 */
@Composable
fun MoneyRevealOverlay(
    amount: Long,
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.25f) }
    val rotation = remember { Animatable(-14f) }

    LaunchedEffect(visible) {
        if (visible) {
            scale.snapTo(0.25f)
            rotation.snapTo(-14f)
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = 0.45f,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                rotation.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = 0.45f,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)),
        exit = fadeOut(tween(300)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            ShineBurst(modifier = Modifier.size(550.dp))

            ConfettiEffect(trigger = visible)

            // The Content Stack: Funky Text -> Icon -> Fan
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        rotationZ = rotation.value
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* absorb tap */ }
            ) {
                // 1. Funky Amount Text
                FunkyText(
                    text = MoneyUtils.formatCurrency(amount.toInt()),
                    fontSize = 58.sp,
                    modifier = Modifier.graphicsLayer { translationY = 20f }
                )

                // 2. Money Icon
                CustomMoneyIcon(
                    modifier = Modifier
                        .size(110.dp)
                        .graphicsLayer { translationY = -10f; shadowElevation = 10f }
                )

                // 3. Money Image Stack (Fan)
                MoneyBillFan(
                    amount = amount.toInt(), 
                    fanHeight = 190.dp,
                    modifier = Modifier.graphicsLayer { translationY = -30f }
                )

                CapsuleButton(
                    text = "TUYỆT VỜI!",
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(220.dp)
                        .padding(top = 20.dp)
                )
            }
        }
    }
}
