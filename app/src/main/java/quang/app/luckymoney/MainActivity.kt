package quang.app.luckymoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.lixitet.ui.WelcomeScreen
import quang.app.luckymoney.ui.navigation.NavRoute
import quang.app.luckymoney.ui.screens.SelectionScreen
import quang.app.luckymoney.ui.screens.SetupAmountScreen
import quang.app.luckymoney.ui.screens.SetupCountScreen
import quang.app.luckymoney.ui.screens.ShuffleScreen
import quang.app.luckymoney.ui.theme.LuckyMoneyTheme
import quang.app.luckymoney.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LuckyMoneyTheme {
                val viewModel: MainViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                val backStack = remember { mutableStateListOf<NavRoute>(NavRoute.Welcome) }
                
                NavDisplay(
                    backStack = backStack,
                    onBack = { 
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1) 
                        } else {
                            finish()
                        }
                    },
                    entryProvider = { key ->
                        when (key) {
                            NavRoute.Welcome -> NavEntry(key) {
                                WelcomeScreen(
                                    onStartClick = {
                                        viewModel.resetSetup()
                                        backStack.add(NavRoute.SetupCount)
                                    }
                                )
                            }
                            NavRoute.SetupCount -> NavEntry(key) {
                                SetupCountScreen(
                                    onConfirm = { count ->
                                        viewModel.setEnvelopeCount(count)
                                        backStack.add(NavRoute.SetupAmount)
                                    },
                                    onBack = { backStack.removeAt(backStack.size - 1) }
                                )
                            }
                            NavRoute.SetupAmount -> NavEntry(key) {
                                SetupAmountScreen(
                                    currentIndex = uiState.currentSetupIndex,
                                    total = uiState.envelopeCount,
                                    onAmountConfirm = { amount ->
                                        viewModel.setAmountForCurrentEnvelope(amount)
                                    },
                                    onComplete = {
                                        backStack.add(NavRoute.Shuffle)
                                    },
                                    onBack = { backStack.removeAt(backStack.size - 1) }
                                )
                            }
                            NavRoute.Shuffle -> NavEntry(key) {
                                ShuffleScreen(
                                    onShuffleComplete = {
                                        viewModel.shuffleEnvelopes()
                                        backStack.removeAt(backStack.size - 1)
                                        backStack.add(NavRoute.Selection)
                                    }
                                )
                            }
                            NavRoute.Selection -> NavEntry(key) {
                                SelectionScreen(
                                    envelopeCount = uiState.envelopeCount,
                                    shuffledAmounts = uiState.shuffledAmounts,
                                    openedIndices = uiState.openedIndices,
                                    fullyRevealedIndices = uiState.fullyRevealedIndices,
                                    onOpen = { index ->
                                        viewModel.openEnvelope(index)
                                    },
                                    onReveal = { index ->
                                        viewModel.revealEnvelope(index)
                                    },
                                    onReset = {
                                        viewModel.resetSetup()
                                        backStack.clear()
                                        backStack.add(NavRoute.Welcome)
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
