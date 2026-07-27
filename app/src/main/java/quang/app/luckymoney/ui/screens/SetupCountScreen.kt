package quang.app.luckymoney.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import quang.app.luckymoney.ui.components.CapsuleButton
import quang.app.luckymoney.ui.components.RadialGradientBackground
import quang.app.luckymoney.ui.theme.*

@Composable
fun SetupCountScreen(
    onConfirm: (Int) -> Unit,
    onBack: () -> Unit
) {
    var count by remember { mutableStateOf(5f) }

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
                text = "Số lượng phong bì",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TetGold,
                    fontFamily = NotoSerifDisplay,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "${count.toInt()}",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = TetGold,
                    fontSize = 120.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Slider(
                value = count,
                onValueChange = { count = it },
                valueRange = 1f..20f,
                steps = 18,
                colors = SliderDefaults.colors(
                    thumbColor = TetGold,
                    activeTrackColor = TetGold,
                    inactiveTrackColor = TetGold.copy(alpha = 0.3f)
                ),
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            CapsuleButton(
                text = "Tiếp tục",
                onClick = { onConfirm(count.toInt()) },
                modifier = Modifier.width(240.dp)
            )
            
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
fun SetupCountScreenPreview() {
    LuckyMoneyTheme {
        SetupCountScreen(onConfirm = {}, onBack = {})
    }
}
