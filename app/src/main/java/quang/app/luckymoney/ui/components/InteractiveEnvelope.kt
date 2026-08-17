package quang.app.luckymoney.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.launch
import quang.app.luckymoney.R
import quang.app.luckymoney.ui.theme.*
import quang.app.luckymoney.utils.AudioManager
import quang.app.luckymoney.utils.MoneyUtils
import kotlin.math.roundToInt

@OptIn(ExperimentalTextApi::class)
@Composable
fun InteractiveEnvelope(
    amount: Long,
    isOpened: Boolean,
    isFlapOpen: Boolean,
    onFlapOpen: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val audioManager = remember { AudioManager.getInstance(context) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    // Envelope dimensions
    val envelopeWidth = 240.dp
    val envelopeHeight = 360.dp
    val revealThreshold = 0.5f

    val heightPx = with(density) { envelopeHeight.toPx() }
    // Only pull the bill peek partway out of the envelope - the full reveal happens
    // in a screen-centered overlay, so this shouldn't travel far or look "detached".
    val maxOffset = -heightPx * 0.5f
    val hideOffset = with(density) { 100.dp.toPx() } // Sink it deep into the envelope

    // Animation States
    val flapRotation = remember { Animatable(if (isFlapOpen) -180f else 0f) }
    val moneyOffsetY = remember { Animatable(if (isOpened) maxOffset else hideOffset) }
    
    // Calculate alpha based on offset to ensure it's hidden until pulled
    // When at hideOffset (100dp), it should be transparent if needed, 
    // but being behind the front cover might be enough.
    // Let's use alpha to be sure it's "completely hidden" as requested.
    val moneyAlpha by animateFloatAsState(
        targetValue = if (moneyOffsetY.value < hideOffset * 0.8f) 1f else 0f,
        animationSpec = tween(300),
        label = "MoneyAlpha"
    )
    
    var isDragging by remember { mutableStateOf(false) }

    // Sync flap rotation
    LaunchedEffect(isFlapOpen) {
        if (isFlapOpen) {
            flapRotation.animateTo(-180f, tween(600, easing = FastOutSlowInEasing))
        } else {
            flapRotation.animateTo(0f, tween(400))
        }
    }

    // Sync money reveal
    LaunchedEffect(isOpened) {
        if (isOpened) {
            moneyOffsetY.animateTo(
                targetValue = maxOffset,
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
            )
        }
    }

    Box(
        modifier = modifier
            .size(envelopeWidth, envelopeHeight + 120.dp)
            .pointerInput(isOpened, isFlapOpen) {
                if (isFlapOpen && !isOpened) {
                    detectVerticalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = {
                            isDragging = false
                            scope.launch {
                                val thresholdPx = -heightPx * revealThreshold
                                if (moneyOffsetY.value <= thresholdPx) {
                                    audioManager.playSfx("success")
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onOpen()
                                    moneyOffsetY.animateTo(
                                        targetValue = maxOffset,
                                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                                    )
                                } else {
                                    moneyOffsetY.animateTo(hideOffset, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
                                }
                            }
                        },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            scope.launch {
                                val newOffset = (moneyOffsetY.value + dragAmount).coerceIn(maxOffset, hideOffset)
                                moneyOffsetY.snapTo(newOffset)
                                
                                // Dynamic Haptics and Sound
                                val progress = (hideOffset - newOffset) / (hideOffset - maxOffset)
                                if (progress > 0.1f) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        val intensity = (progress * 255).toInt().coerceIn(1, 255)
                                        vibrator.vibrate(VibrationEffect.createOneShot(10, intensity))
                                    } else {
                                        @Suppress("DEPRECATION")
                                        vibrator.vibrate(10)
                                    }
                                }
                                
                                if (dragAmount < -2f) {
                                    audioManager.playSfx("pull")
                                }
                            }
                        }
                    )
                }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        // 1. Envelope Back & Body
        Canvas(modifier = Modifier.size(envelopeWidth, envelopeHeight)) {
            drawEnvelopeBody()
        }

        // 2. Flap (Open state - drawn behind money)
        if (flapRotation.value < -90f) {
            Canvas(modifier = Modifier.size(envelopeWidth, envelopeHeight)) {
                drawEnvelopeFlap(flapRotation.value)
            }
        }

        // 3. Money Bill Peek
        Box(
            modifier = Modifier
                .offset { IntOffset(0, moneyOffsetY.value.roundToInt()) }
                .graphicsLayer { alpha = moneyAlpha }
                .size(envelopeWidth - 20.dp, envelopeHeight * 0.55f)
                .padding(bottom = 20.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isOpened) {
                MoneyBillPeek(amount = amount)
            } else {
                ShiningMoneyIcon(modifier = Modifier.size(170.dp, 85.dp))
            }
        }

        // 4. Envelope Front
        Canvas(
            modifier = Modifier
                .size(envelopeWidth, envelopeHeight)
                .clickable(enabled = !isFlapOpen) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onFlapOpen()
                }
        ) {
            // Draw Front Cover
            drawEnvelopeFront(textMeasurer)
        }

        // 5. Flap (Closed state - drawn on top of front)
        if (flapRotation.value >= -90f) {
            Canvas(modifier = Modifier.size(envelopeWidth, envelopeHeight)) {
                drawEnvelopeFlap(flapRotation.value)
            }
        }

        // Success Feedback
        if (isOpened && moneyOffsetY.value <= maxOffset + 10f) {
            ConfettiEffect()
        }
    }
}

private fun DrawScope.drawEnvelopeBody() {
    val cornerRadius = 16.dp.toPx()
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(Offset.Zero, size),
                cornerRadius = CornerRadius(cornerRadius)
            )
        )
    }
    drawPath(path, color = TetDarkRed)
    
    // Inner shadow/gradient for depth
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent),
            startY = 0f,
            endY = 100f
        )
    )
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawEnvelopeFront(textMeasurer: TextMeasurer) {
    val cornerRadius = 16.dp.toPx()
    
    val path = Path().apply {
        moveTo(0f, size.height * 0.2f)
        lineTo(size.width, size.height * 0.2f)
        lineTo(size.width, size.height - cornerRadius)
        quadraticTo(size.width, size.height, size.width - cornerRadius, size.height)
        lineTo(cornerRadius, size.height)
        quadraticTo(0f, size.height, 0f, size.height - cornerRadius)
        close()
    }
    
    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = listOf(TetRed, TetDarkRed)
        )
    )

    // Draw 'Phúc' Character (福)
    val phucText = "福"
    val textStyle = TextStyle(
        color = TetGold,
        fontSize = 80.sp,
        fontWeight = FontWeight.Bold,
        shadow = Shadow(Color.Black.copy(alpha = 0.3f), Offset(4f, 4f), 8f)
    )
    val textLayoutResult = textMeasurer.measure(phucText, textStyle)
    
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            (size.width - textLayoutResult.size.width) / 2,
            (size.height * 0.6f) - (textLayoutResult.size.height / 2)
        )
    )
    
    // Golden border or patterns
    drawRect(
        color = TetGold.copy(alpha = 0.2f),
        topLeft = Offset(20f, size.height * 0.2f + 20f),
        size = Size(size.width - 40f, size.height * 0.8f - 40f),
        style = Stroke(width = 2f)
    )
}

private fun DrawScope.drawEnvelopeFlap(rotationDegrees: Float) {
    val flapHeight = size.height * 0.25f
    
    // Actually, let's simplify: Draw the flap as a path.
    // If rotationDegrees is 0, it's pointing down.
    // If rotationDegrees is -180, it's pointing up.
    
    val currentFlapHeight = flapHeight * kotlin.math.cos(Math.toRadians(rotationDegrees.toDouble())).toFloat()
    val isBackSide = rotationDegrees < -90f
    
    val flapPath = Path().apply {
        moveTo(0f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width / 2, currentFlapHeight)
        close()
    }
    
    drawPath(
        path = flapPath,
        color = if (isBackSide) TetDarkRed else TetFlapRed
    )
    
    // Gold tip for the flap
    if (!isBackSide) {
        val tipPath = Path().apply {
            moveTo(size.width / 2 - 20f, currentFlapHeight - 20f)
            lineTo(size.width / 2 + 20f, currentFlapHeight - 20f)
            lineTo(size.width / 2, currentFlapHeight)
            close()
        }
        drawPath(tipPath, color = TetGold)
    }
}

/**
 * Just the top bill peeking out of the envelope while dragging - the compact
 * "coming attractions" preview before the full reveal overlay takes over.
 */
@Composable
fun MoneyBillPeek(amount: Long) {
    val topDenomination = MoneyUtils.getMoneyBreakdown(amount.toInt()).firstOrNull()
        ?: R.drawable.money_1k

    Image(
        painter = painterResource(id = topDenomination),
        contentDescription = null,
        modifier = Modifier
            .width(170.dp)
            .height(85.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .shimmerEffect()
            .graphicsLayer { shadowElevation = 6f },
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun ConfettiEffect() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url("https://assets5.lottiefiles.com/packages/lf20_9n66aprx.json")
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .fillMaxSize()
            .scale(2.5f)
    )
}
