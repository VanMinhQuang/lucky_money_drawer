package quang.app.luckymoney.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import quang.app.luckymoney.ui.components.*
import quang.app.luckymoney.ui.theme.*

@Composable
fun SetupAmountScreen(
    currentIndex: Int,
    total: Int,
    onAmountConfirm: (Long) -> Unit,
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var isAnimating by remember { mutableStateOf(false) }
    val isAllDone = currentIndex >= total

    RadialGradientBackground(
        brush = SetupBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (!isAllDone) "Phong bì ${currentIndex + 1} / $total" else "Sẵn sàng!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TetGold,
                    fontFamily = NotoSerifDisplay,
                    fontSize = 28.sp
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier.height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                Envelope(
                    isOpen = isAnimating || isAllDone,
                    label = if (isAllDone) "XONG!" else "Lì Xì"
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isAnimating,
                        enter = slideInVertically { it } + fadeIn(),
                        exit = slideOutVertically { -it } + fadeOut()
                    ) {
                        MoneyBill(
                            amount = amountText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            if (!isAllDone) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { if (it.all { char -> char.isDigit() }) amountText = it },
                    placeholder = { Text("Nhập số tiền...", color = TetGold.copy(alpha = 0.5f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TetGold,
                        unfocusedTextColor = TetGold,
                        focusedBorderColor = TetGold,
                        unfocusedBorderColor = TetGold.copy(alpha = 0.3f),
                        cursorColor = TetGold,
                        focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = TetGold
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                CapsuleButton(
                    text = "Bỏ vào phong bì",
                    onClick = {
                        if (amountText.isNotEmpty()) {
                            isAnimating = true
                            // Simulate animation delay
                        }
                    },
                    enabled = amountText.isNotEmpty() && !isAnimating,
                    modifier = Modifier.fillMaxWidth()
                )

                // Handled animation end
                if (isAnimating) {
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(1000)
                        isAnimating = false
                        onAmountConfirm(amountText.toLongOrNull() ?: 0L)
                        amountText = ""
                    }
                }
            } else {
                CapsuleButton(
                    text = "Tiếp tục",
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            TextButton(
                onClick = onBack,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Quay lại",
                    color = TetGold.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SetupAmountScreenPreview() {
    LuckyMoneyTheme {
        SetupAmountScreen(
            currentIndex = 0,
            total = 5,
            onAmountConfirm = {},
            onComplete = {},
            onBack = {}
        )
    }
}
