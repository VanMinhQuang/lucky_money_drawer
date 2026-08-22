/**
 * LÌ XÌ TẾT — Welcome Screen
 * ---------------------------------------------------------------
 * Chuyển đổi 100% từ file HTML/CSS/JS gốc (li-xi-tet-welcome.html)
 * sang Jetpack Compose (Kotlin), giữ nguyên toàn bộ hình ảnh (vẽ lại
 * bằng Canvas dựa trên đúng path/circle/gradient của SVG gốc) và
 * toàn bộ animation (falling petals, twinkle glints, glow pulse,
 * title rise-in, envelope float, tap-wiggle, coin burst, shine sweep,
 * CTA pulse-glow, toast).
 *
 * CÁCH DÙNG
 * ---------------------------------------------------------------
 * 1) Thêm dependency Compose (BOM mới nhất) vào build.gradle.kts (app).
 * 2) Copy file này vào module app, đặt vào Activity:
 *
 *      class MainActivity : ComponentActivity() {
 *          override fun onCreate(savedInstanceState: Bundle?) {
 *              super.onCreate(savedInstanceState)
 *              setContent { LiXiTetWelcomeScreen() }
 *          }
 *      }
 *
 * 3) FONT: bản gốc dùng "Baloo 2" (tiêu đề) và "Be Vietnam Pro" (phần
 *    còn lại) từ Google Fonts. Để đúng 100%, hãy:
 *      - Tải 2 font trên về res/font/ (baloo2_extrabold.ttf,
 *        be_vietnam_pro_medium.ttf, be_vietnam_pro_semibold.ttf, ...)
 *      - Khai báo FontFamily thật và thay thế 2 hằng số
 *        BalooFontFamily / BeVietnamProFontFamily bên dưới.
 *    Mặc định đang tạm dùng FontFamily.Default để code chạy được ngay
 *    kể cả khi bạn chưa thêm font.
 *
 * 4) Không cần file ảnh/SVG nào khác — toàn bộ phong bao lì xì, hoa
 *    mai, đồng xu... được vẽ trực tiếp bằng Canvas (drawScope) dựa
 *    trên đúng toạ độ/path trong SVG gốc, nên luôn sắc nét mọi kích
 *    thước màn hình (vector, không phải bitmap).
 */

package com.example.lixitet.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import quang.app.luckymoney.ui.components.AppButton
import quang.app.luckymoney.ui.components.FallingBlossoms
import quang.app.luckymoney.ui.components.RadialGradientBackground
import quang.app.luckymoney.ui.theme.WelcomeGradient
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// =====================================================================
//  FONTS  (thay bằng font thật khi đã copy .ttf vào res/font)
// =====================================================================
private val BalooFontFamily: FontFamily = FontFamily.Default        // TODO: "Baloo 2"
private val BeVietnamProFontFamily: FontFamily = FontFamily.Default // TODO: "Be Vietnam Pro"

// =====================================================================
//  COLORS  (đúng bằng biến CSS :root trong file gốc)
// =====================================================================
private object LxColors {
    val Red = Color(0xFFC1272D)
    val RedBright = Color(0xFFE4432B)
    val Gold = Color(0xFFE8B84B)
    val GoldLight = Color(0xFFFFE9A8)
    val Cream = Color(0xFFFFF6E8)
    val Mai = Color(0xFFFFCF3F)

    val EnvBack1 = Color(0xFF5C0C10)
    val EnvBack2 = Color(0xFF8E181D)
    val BodyGradTop = Color(0xFFE4432B)
    val BodyGradBottom = Color(0xFFA81E24)
    val FlapGradTop = Color(0xFFC1272D)
    val FlapGradBottom = Color(0xFF8E181D)
    val SealTop = Color(0xFFFFE9A8)
    val SealMid = Color(0xFFE8B84B)
    val SealBottom = Color(0xFFB9852A)
    val ToastBg = Color(0xE1140404)
}

// =====================================================================
//  ROOT SCREEN
// =====================================================================
@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {
    var toastMessage by remember { mutableStateOf("") }
    var toastVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val toastJobRef = remember { mutableStateOf<Job?>(null) }

    val showToast: (String) -> Unit = { msg ->
        toastMessage = msg
        toastVisible = true
        toastJobRef.value?.cancel()
        toastJobRef.value = scope.launch {
            delay(2200)
            toastVisible = false
        }
    }

    RadialGradientBackground(
        brush = WelcomeGradient
    ) {
        // ---------- ambient background layers ----------
        FallingBlossoms()
        TwinklingGlints()

        // ---------- foreground content ----------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            EyebrowText()
            Spacer(Modifier.height(10.dp))
            TitleBlock()
            Spacer(Modifier.weight(1f))
            EnvelopeStage(onTap = { showToast("🧧 Một phong lì xì đầy lộc!") })
            Spacer(Modifier.weight(1f))
            CtaSection(
                onOpen = { onStartClick() },
            )
            Spacer(Modifier.height(24.dp))
        }

        ToastBubble(
            message = toastMessage,
            visible = toastVisible,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
        )
    }
}

// =====================================================================
//  .glint  ( @keyframes twinkle, 3.4s ease-in-out infinite, 12x random )
// =====================================================================
private data class GlintSpec(val xFraction: Float, val yFraction: Float, val delayMs: Int)

@Composable
private fun TwinklingGlints() {
    val glints = remember {
        List(12) {
            GlintSpec(
                xFraction = 0.05f + Random.nextFloat() * 0.9f,
                yFraction = 0.06f + Random.nextFloat() * 0.7f,
                delayMs = (Random.nextFloat() * 3400).toInt()
            )
        }
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val widthDp = maxWidth
        val heightDp = maxHeight
        glints.forEach { glint -> GlintView(glint, widthDp, heightDp) }
    }
}

@Composable
private fun GlintView(glint: GlintSpec, widthDp: Dp, heightDp: Dp) {
    val infinite = rememberInfiniteTransition(label = "glint")
    val t by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(-glint.delayMs, StartOffsetType.FastForward)
        ),
        label = "glintT"
    )
    val alpha = lerp(0.15f, 0.9f, t)
    val scale = lerp(0.7f, 1.25f, t)
    Box(
        modifier = Modifier
            .size(6.dp)
            .offset(x = widthDp * glint.xFraction, y = heightDp * glint.yFraction)
            .graphicsLayer {
                this.alpha = alpha
                scaleX = scale
                scaleY = scale
            }
            .background(
                Brush.radialGradient(listOf(LxColors.GoldLight, LxColors.Gold, Color.Transparent)),
                shape = CircleShape
            )
    )
}

// =====================================================================
//  .eyebrow  ( @keyframes fadeDown, .7s delay .25s )
// =====================================================================
@Composable
private fun EyebrowText() {
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(-10f) }
    LaunchedEffect(Unit) {
        delay(250)
        val spec = tween<Float>(700, easing = FastOutSlowInEasing)
        launch { alpha.animateTo(1f, spec) }
        offsetY.animateTo(0f, spec)
    }
    androidx.compose.material3.Text(
        text = "✦ Chúc Mừng Năm Mới ✦",
        color = LxColors.GoldLight,
        fontFamily = BeVietnamProFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.5.sp,
        letterSpacing = 2.2.sp,
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha.value
            translationY = offsetY.value
        }
    )
}

// =====================================================================
//  .title / .divider / .tagline
// =====================================================================
@Composable
private fun TitleBlock() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TitleSpan("LÌ", delayMs = 500)
            TitleSpan("XÌ", delayMs = 620)
            TitleSpan("TẾT", delayMs = 740)
        }
        DividerWithDiamond()
        Tagline()
    }
}

/** span rise-in: @keyframes riseIn, .7s cubic-bezier(.2,.9,.25,1) forwards, staggered delay */
@Composable
private fun TitleSpan(text: String, delayMs: Int) {
    val alpha = remember { Animatable(0f) }
    val translateY = remember { Animatable(22f) }
    val scale = remember { Animatable(0.92f) }
    LaunchedEffect(Unit) {
        delay(delayMs.toLong())
        val spec = tween<Float>(700, easing = CubicBezierEasing(0.2f, 0.9f, 0.25f, 1f))
        launch { alpha.animateTo(1f, spec) }
        launch { translateY.animateTo(0f, spec) }
        scale.animateTo(1f, spec)
    }
    androidx.compose.material3.Text(
        text = text,
        fontFamily = BalooFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 46.sp,
        style = TextStyle(
            brush = Brush.verticalGradient(
                listOf(LxColors.GoldLight, LxColors.Gold, Color(0xFFC9932C))
            )
        ),
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha.value
            translationY = translateY.value
            scaleX = scale.value
            scaleY = scale.value
        }
    )
}

/** .divider — fadeIn .7s delay .95s, "◆" ở giữa */
@Composable
private fun DividerWithDiamond() {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(950)
        alpha.animateTo(1f, tween(700))
    }
    Box(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .width(120.dp)
            .height(1.dp)
            .graphicsLayer { this.alpha = alpha.value }
            .background(
                Brush.horizontalGradient(listOf(Color.Transparent, LxColors.Gold, Color.Transparent))
            ),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "◆",
            fontSize = 8.sp,
            color = LxColors.GoldLight,
            modifier = Modifier
                .background(Color(0xFF6E0F14))
                .padding(horizontal = 6.dp)
        )
    }
}

/** .tagline — fadeIn .7s delay 1.05s */
@Composable
private fun Tagline() {
    val alpha = remember { Animatable(0f) }
    val translateY = remember { Animatable(8f) }
    LaunchedEffect(Unit) {
        delay(1050)
        val spec = tween<Float>(700)
        launch { alpha.animateTo(1f, spec) }
        translateY.animateTo(0f, spec)
    }
    androidx.compose.material3.Text(
        text = "Chạm phong bao để rút lộc may mắn đầu năm",
        fontFamily = BeVietnamProFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.5.sp,
        color = LxColors.Cream.copy(alpha = 0.82f),
        textAlign = TextAlign.Center,
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha.value
            translationY = translateY.value
        }
    )
}

// =====================================================================
//  .envelope-stage / .envelope-float  ( entrance envIntro, float, wiggle,
//  shine sweep, coin burst on tap )
// =====================================================================
@Composable
private fun EnvelopeStage(onTap: () -> Unit) {
    // envIntro: scale .6->1, rotate -8deg->0, 1s delay .95s, overshoot ease
    val entranceProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(950)
        entranceProgress.animateTo(1f, tween(1000, easing = CubicBezierEasing(0.34f, 1.4f, 0.4f, 1f)))
    }

    // float: translateY 0->-14px->0, rotate -1.2deg->1.2deg, 4.2s ease-in-out infinite
    val infinite = rememberInfiniteTransition(label = "envFloat")
    val floatT by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatT"
    )
    val floatTranslateYDp = (-14f * floatT).dp
    val floatRotate = lerp(-1.2f, 1.2f, floatT)

    // wiggle: .5s keyframes, retriggered on every tap
    var wiggleTrigger by remember { mutableStateOf(0) }
    val wiggle = remember { Animatable(0f) }
    LaunchedEffect(wiggleTrigger) {
        if (wiggleTrigger == 0) return@LaunchedEffect
        wiggle.snapTo(0f)
        wiggle.animateTo(1f, tween(500, easing = LinearEasing))
    }
    val wiggleRotation = interpolateKeyframes(
        wiggle.value,
        listOf(0f to 0f, 0.2f to -6f, 0.4f to 5f, 0.6f to -3f, 0.8f to 2f, 1f to 0f)
    )
    val wiggleScale = interpolateKeyframes(
        wiggle.value,
        listOf(0f to 1f, 0.2f to 1.03f, 0.4f to 1.03f, 0.6f to 1f, 0.8f to 1f, 1f to 1f)
    )

    val particles = remember { mutableStateListOf<CoinParticleSpec>() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.size(230.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    wiggleTrigger++
                    spawnCoinBurst(particles, scope)
                    onTap()
                }
                .graphicsLayer {
                    alpha = entranceProgress.value
                    val entranceScale = lerp(0.6f, 1f, entranceProgress.value)
                    val entranceRotate = lerp(-8f, 0f, entranceProgress.value)
                    scaleX = entranceScale * wiggleScale
                    scaleY = entranceScale * wiggleScale
                    rotationZ = entranceRotate + floatRotate + wiggleRotation
                    translationY = floatTranslateYDp.toPx()
                },
            contentAlignment = Alignment.Center
        ) {
            EnvelopeCanvas(modifier = Modifier.fillMaxSize(0.94f))
            ShineSweep(modifier = Modifier.fillMaxSize(0.78f))
        }
        CoinBurstOverlay(particles)
    }
}

private fun interpolateKeyframes(p: Float, stops: List<Pair<Float, Float>>): Float {
    for (i in 0 until stops.size - 1) {
        val (t0, v0) = stops[i]
        val (t1, v1) = stops[i + 1]
        if (p in t0..t1) {
            val local = if (t1 == t0) 0f else (p - t0) / (t1 - t0)
            return lerp(v0, v1, local)
        }
    }
    return stops.last().second
}

/**
 * Vẽ lại chính xác phong bao lì xì SVG gốc (viewBox 0 0 240 280):
 * 2 lớp phong bao phía sau, thân chính, viền trong, nắp phong bì (path
 * bezier bậc 2), con dấu đồng xu cổ, và 2 chùm hoa mai trang trí.
 */
@Composable
private fun EnvelopeCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(240f / 280f)) {
        val sxF = size.width / 240f
        val syF = size.height / 280f
        fun sx(v: Float) = v * sxF
        fun sy(v: Float) = v * syF

        // ---- back envelope peeking out (2 lớp) ----
        rotate(degrees = -7f, pivot = Offset(sx(120f), sy(140f))) {
            drawRoundRect(
                color = LxColors.EnvBack1.copy(alpha = 0.55f),
                topLeft = Offset(sx(34f), sy(46f)),
                size = Size(sx(168f), sy(196f)),
                cornerRadius = CornerRadius(sx(16f), sy(16f))
            )
        }
        rotate(degrees = 4f, pivot = Offset(sx(120f), sy(140f))) {
            drawRoundRect(
                color = LxColors.EnvBack2.copy(alpha = 0.6f),
                topLeft = Offset(sx(40f), sy(52f)),
                size = Size(sx(160f), sy(188f)),
                cornerRadius = CornerRadius(sx(14f), sy(14f))
            )
        }

        // ---- main envelope body ----
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(LxColors.BodyGradTop, LxColors.BodyGradBottom),
                startY = sy(54f), endY = sy(238f)
            ),
            topLeft = Offset(sx(22f), sy(54f)),
            size = Size(sx(196f), sy(184f)),
            cornerRadius = CornerRadius(sx(18f), sy(18f))
        )
        drawRoundRect(
            color = LxColors.Gold,
            topLeft = Offset(sx(22f), sy(54f)),
            size = Size(sx(196f), sy(184f)),
            cornerRadius = CornerRadius(sx(18f), sy(18f)),
            style = Stroke(width = sx(3f))
        )
        drawRoundRect(
            color = LxColors.GoldLight.copy(alpha = 0.25f),
            topLeft = Offset(sx(30f), sy(62f)),
            size = Size(sx(180f), sy(168f)),
            cornerRadius = CornerRadius(sx(12f), sy(12f)),
            style = Stroke(width = sx(1.5f))
        )

        // ---- flap (path bezier bậc 2, đúng d="M22 70 Q120 150 218 70 ..." ) ----
        val flapPath = Path().apply {
            moveTo(sx(22f), sy(70f))
            quadraticBezierTo(sx(120f), sy(150f), sx(218f), sy(70f))
            lineTo(sx(218f), sy(60f))
            quadraticBezierTo(sx(218f), sy(55f), sx(213f), sy(55f))
            lineTo(sx(27f), sy(55f))
            quadraticBezierTo(sx(22f), sy(55f), sx(22f), sy(60f))
            close()
        }
        drawPath(
            path = flapPath,
            brush = Brush.verticalGradient(
                listOf(LxColors.FlapGradTop, LxColors.FlapGradBottom),
                startY = sy(55f), endY = sy(150f)
            )
        )
        drawPath(path = flapPath, color = LxColors.Gold, style = Stroke(width = sx(2.5f)))

        // ---- seal: ancient coin motif ----
        val sealCenter = Offset(sx(120f), sy(150f))
        drawCircle(
            brush = Brush.radialGradient(
                listOf(LxColors.SealTop, LxColors.SealMid, LxColors.SealBottom),
                center = Offset(sealCenter.x - sx(38f) * 0.2f, sealCenter.y - sy(38f) * 0.3f),
                radius = sx(38f) * 1.3f
            ),
            radius = sx(38f),
            center = sealCenter
        )
        drawCircle(color = LxColors.EnvBack2, radius = sx(38f), center = sealCenter, style = Stroke(width = sx(2.5f)))
        drawCircle(color = LxColors.Red, radius = sx(28f), center = sealCenter)
        rotate(degrees = 45f, pivot = sealCenter) {
            drawRect(
                color = LxColors.Gold,
                topLeft = Offset(sealCenter.x - sx(9f), sealCenter.y - sy(9f)),
                size = Size(sx(18f), sy(18f))
            )
        }
        drawCircle(color = LxColors.GoldLight, radius = sx(2.4f), center = Offset(sealCenter.x, sealCenter.y - sy(24f)))
        drawCircle(color = LxColors.GoldLight, radius = sx(2.4f), center = Offset(sealCenter.x, sealCenter.y + sy(24f)))
        drawCircle(color = LxColors.GoldLight, radius = sx(2.4f), center = Offset(sealCenter.x - sx(24f), sealCenter.y))
        drawCircle(color = LxColors.GoldLight, radius = sx(2.4f), center = Offset(sealCenter.x + sx(24f), sealCenter.y))

        // ---- mai blossom flourishes (2 chùm) ----
        drawMaiFlourish(center = Offset(sx(52f), sy(196f)), scale = 1f, alpha = 0.9f, sx = ::sx, sy = ::sy)
        drawMaiFlourish(center = Offset(sx(190f), sy(204f)), scale = 0.75f, alpha = 0.85f, sx = ::sx, sy = ::sy)
    }
}

private fun DrawScope.drawMaiFlourish(
    center: Offset,
    scale: Float,
    alpha: Float,
    sx: (Float) -> Float,
    sy: (Float) -> Float
) {
    val rBig = sx(4.5f) * scale
    val rSmall = sx(4f) * scale
    val petals = listOf(
        Offset(0f, 0f) to rBig,
        Offset(sx(7f) * scale, sy(-2f) * scale) to rSmall,
        Offset(sx(4f) * scale, sy(8f) * scale) to rSmall,
        Offset(sx(-7f) * scale, sy(-2f) * scale) to rSmall,
        Offset(sx(-4f) * scale, sy(8f) * scale) to rSmall
    )
    petals.forEach { (offset, radius) ->
        drawCircle(color = LxColors.Mai.copy(alpha = alpha), radius = radius, center = center + offset)
    }
    drawCircle(color = LxColors.SealBottom.copy(alpha = alpha), radius = sx(2f) * scale, center = center)
}

/** .shine::after — sweep 3.6s ease-in-out infinite, skewX(-18deg) xấp xỉ bằng rotationZ */
@Composable
private fun ShineSweep(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "shine")
    val progress by infinite.animateFloat(
        initialValue = -0.6f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3600
                -0.6f at 0
                1.3f at 1260
                1.3f at 3600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "shineProgress"
    )
    Canvas(
        modifier = modifier.clip(RoundedCornerShape(20.dp))
    ) {
        val shineWidth = size.width * 0.4f
        val x = progress * size.width
        rotate(degrees = -18f, pivot = Offset(x, size.height / 2f)) {
            drawRect(
                brush = Brush.linearGradient(
                    listOf(Color.Transparent, LxColors.GoldLight.copy(alpha = 0.55f), Color.Transparent)
                ),
                topLeft = Offset(x - shineWidth / 2f, -size.height * 0.3f),
                size = Size(shineWidth, size.height * 1.6f)
            )
        }
    }
}

// ---- coin burst particles ( @keyframes burst .9s, spawned x10 on tap ) ----
private data class CoinParticleSpec(val id: Long, val angle: Float, val dist: Float, val delayMs: Long)
private data class BurstFrame(val dx: Float, val dy: Float, val scale: Float, val alpha: Float)

private fun spawnCoinBurst(list: MutableList<CoinParticleSpec>, scope: CoroutineScope) {
    repeat(10) { k ->
        val angle = (2 * PI * k / 10).toFloat() + Random.nextFloat() * 0.3f
        val dist = 60f + Random.nextFloat() * 50f
        val delayMs = (Random.nextFloat() * 80).toLong()
        val id = System.nanoTime() + k
        val particle = CoinParticleSpec(id, angle, dist, delayMs)
        list.add(particle)
        scope.launch {
            delay(1100)
            list.removeAll { it.id == id }
        }
    }
}

@Composable
private fun CoinBurstOverlay(particles: List<CoinParticleSpec>) {
    Box(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            key(particle.id) { CoinParticleView(particle) }
        }
    }
}

@Composable
private fun BoxScope.CoinParticleView(particle: CoinParticleSpec) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(particle.id) {
        delay(particle.delayMs)
        progress.animateTo(1f, tween(900, easing = CubicBezierEasing(0.2f, 0.7f, 0.3f, 1f)))
    }
    val frame = burstKeyframe(progress.value, particle.angle, particle.dist)
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .size(12.dp)
            .graphicsLayer {
                translationX = frame.dx
                translationY = frame.dy
                scaleX = frame.scale
                scaleY = frame.scale
                this.alpha = frame.alpha
            }
            .background(
                Brush.radialGradient(listOf(LxColors.GoldLight, LxColors.Gold, Color(0xFFB9852A))),
                shape = CircleShape
            )
    )
}

private fun burstKeyframe(p: Float, angle: Float, dist: Float): BurstFrame {
    val dxFull = cos(angle) * dist
    val dyFull = sin(angle) * dist
    return if (p <= 0.15f) {
        val local = if (p <= 0f) 0f else p / 0.15f
        BurstFrame(dxFull * 0.35f * local, dyFull * 0.35f * local, local, 1f)
    } else {
        val local = (p - 0.15f) / 0.85f
        BurstFrame(
            dx = lerp(dxFull * 0.35f, dxFull, local),
            dy = lerp(dyFull * 0.35f, dyFull + 40f, local),
            scale = lerp(1f, 0.5f, local),
            alpha = lerp(1f, 0f, local)
        )
    }
}

// =====================================================================
//  .cta-wrap / .cta / .secondary / .footer-note
// =====================================================================
@Composable
private fun CtaSection(onOpen: () -> Unit) {
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val wrapAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(1500)
        wrapAlpha.animateTo(1f, tween(700))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { alpha = wrapAlpha.value }
    ) {
        AppButton(
            text = "Rút Lì Xì Ngay",
            isLoading = isLoading,
            onClick = {
                onOpen()
            }
        )


        Text(
            text = "VẠN SỰ NHƯ Ý · AN KHANG THỊNH VƯỢNG",
            fontSize = 11.sp,
            letterSpacing = 1.4.sp,
            color = LxColors.GoldLight.copy(alpha = 0.45f)
        )
    }
}


@Composable
private fun ToastBubble(message: String, visible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 3 },
        exit = fadeOut(tween(300)) + slideOutVertically(tween(300)) { it / 3 },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(LxColors.ToastBg)
                .border(1.dp, LxColors.Gold.copy(alpha = 0.35f), RoundedCornerShape(999.dp))
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            androidx.compose.material3.Text(
                text = message,
                fontFamily = BeVietnamProFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = LxColors.Cream
            )
        }
    }
}
