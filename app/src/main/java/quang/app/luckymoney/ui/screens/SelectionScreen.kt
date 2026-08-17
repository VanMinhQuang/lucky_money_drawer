package quang.app.luckymoney.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import quang.app.luckymoney.R
import quang.app.luckymoney.ui.components.*
import quang.app.luckymoney.ui.theme.*
import quang.app.luckymoney.utils.AudioManager
import kotlin.math.absoluteValue

@Composable
fun SelectionScreen(
    envelopeCount: Int,
    shuffledAmounts: List<Long>,
    openedIndices: Set<Int>,
    fullyRevealedIndices: Set<Int>,
    onOpen: (Int) -> Unit,
    onReveal: (Int) -> Unit,
    onReset: () -> Unit
) {
    val context = LocalContext.current
    val audioManager = remember { AudioManager.getInstance(context) }
    val pagerState = rememberPagerState(pageCount = { envelopeCount })
    var lastPage by remember { mutableIntStateOf(0) }
    val allOpened = openedIndices.size == envelopeCount
    var flappedIndices by remember { mutableStateOf(setOf<Int>()) }
    var dismissedPages by remember { mutableStateOf(setOf<Int>()) }

    DisposableEffect(Unit) {
        audioManager.startBgm()
        onDispose {
            // Keep BGM playing if we're just moving between screens? 
            // Usually yes for a small app, but let's be explicit if requested.
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != lastPage) {
            audioManager.playSfx("pop")
            lastPage = pagerState.currentPage
        }
    }

    // Derive revealed page: if current page is fully revealed and not dismissed, show it.
    // This makes it "permanent" in the sense that it persists in state.
    val showOverlay = fullyRevealedIndices.contains(pagerState.currentPage) && 
                     !dismissedPages.contains(pagerState.currentPage)


    RadialGradientBackground(
        brush = SelectionBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (allOpened) "CHÚC MỪNG!" else "CHỌN LÌ XÌ",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TetGold,
                    fontFamily = NotoSerifDisplay,
                    fontSize = 32.sp,
                    letterSpacing = 2.sp
                ),
                modifier = Modifier.padding(top = 40.dp, bottom = 20.dp)
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 48.dp),
                pageSpacing = 24.dp,
                verticalAlignment = Alignment.CenterVertically
            ) { page ->
                val pageOffset = (
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        ).absoluteValue

                val scale = 0.8f + (1f - 0.8f) * (1f - pageOffset.coerceIn(0f, 1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            alpha = 0.6f + (1f - 0.6f) * (1f - pageOffset.coerceIn(0f, 1f))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val isOpened = openedIndices.contains(page)
                    InteractiveEnvelope(
                        amount = shuffledAmounts.getOrNull(page) ?: 0L,
                        isOpened = isOpened,
                        isFlapOpen = flappedIndices.contains(page) || isOpened,
                        onFlapOpen = { 
                            audioManager.playSfx("flap_open")
                            flappedIndices = flappedIndices + page 
                        },
                        onOpen = {
                            onOpen(page)
                            onReveal(page)
                            // Remove from dismissed if it was there
                            dismissedPages = dismissedPages - page
                        }
                    )
                }
            }

            if (allOpened) {
                CapsuleButton(
                    text = "Làm lại",
                    onClick = {
                        flappedIndices = emptySet()
                        onReset()
                    },
                    modifier = Modifier.padding(bottom = 60.dp).width(240.dp)
                )
            } else {
                val currentIsFlapped = flappedIndices.contains(pagerState.currentPage) || 
                                     openedIndices.contains(pagerState.currentPage)
                
                Text(
                    text = if (currentIsFlapped) "Vuốt lên để lấy" else "Chạm để mở",
                    color = TetGold.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 60.dp)
                )
            }
        }

        // Sits on top of the pager, dead-center of the whole screen.
        MoneyRevealOverlay(
            amount = shuffledAmounts.getOrNull(pagerState.currentPage) ?: 0L,
            visible = showOverlay,
            onDismiss = { 
                dismissedPages = dismissedPages + pagerState.currentPage
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SelectionScreenPreview() {
    LuckyMoneyTheme {
        SelectionScreen(
            envelopeCount = 5,
            shuffledAmounts = listOf(1000L, 10000L, 50000L, 100000L, 500000L),
            openedIndices = setOf(1),
            fullyRevealedIndices = setOf(1),
            onOpen = {},
            onReveal = {},
            onReset = {}
        )
    }
}
