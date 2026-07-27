package quang.app.luckymoney.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppState(
    val envelopeCount: Int = 0,
    val envelopeAmounts: List<Long> = emptyList(),
    val currentSetupIndex: Int = 0,
    val shuffledAmounts: List<Long> = emptyList(),
    val openedIndices: Set<Int> = emptySet(),
    val fullyRevealedIndices: Set<Int> = emptySet()
) {
    val isSetupComplete: Boolean get() = envelopeCount > 0 && currentSetupIndex >= envelopeCount
    val allOpened: Boolean get() = envelopeCount > 0 && openedIndices.size == envelopeCount
}

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    fun setEnvelopeCount(count: Int) {
        _uiState.update { 
            it.copy(
                envelopeCount = count,
                envelopeAmounts = List(count) { 0L },
                currentSetupIndex = 0,
                shuffledAmounts = emptyList(),
                openedIndices = emptySet(),
                fullyRevealedIndices = emptySet()
            )
        }
    }

    fun setAmountForCurrentEnvelope(amount: Long) {
        _uiState.update { state ->
            val newAmounts = state.envelopeAmounts.toMutableList()
            if (state.currentSetupIndex < newAmounts.size) {
                newAmounts[state.currentSetupIndex] = amount
            }
            state.copy(
                envelopeAmounts = newAmounts,
                currentSetupIndex = (state.currentSetupIndex + 1).coerceAtMost(state.envelopeCount)
            )
        }
    }

    fun shuffleEnvelopes() {
        _uiState.update { state ->
            state.copy(
                shuffledAmounts = state.envelopeAmounts.shuffled(),
                openedIndices = emptySet(),
                fullyRevealedIndices = emptySet()
            )
        }
    }

    fun openEnvelope(index: Int) {
        _uiState.update { it.copy(openedIndices = it.openedIndices + index) }
    }

    fun revealEnvelope(index: Int) {
        _uiState.update { it.copy(fullyRevealedIndices = it.fullyRevealedIndices + index) }
    }

    fun resetSetup() {
        _uiState.update { AppState() }
    }
}
