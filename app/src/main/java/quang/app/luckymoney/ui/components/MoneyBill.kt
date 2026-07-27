package quang.app.luckymoney.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import quang.app.luckymoney.ui.theme.MoneyGreen
import quang.app.luckymoney.ui.theme.TetGold

@Composable
fun MoneyBill(
    amount: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(140.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFE8F5E9))
            .border(1.dp, MoneyGreen.copy(alpha = 0.3f), RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(4.dp)
        ) {
            Text(
                text = "CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM",
                fontSize = 5.sp,
                color = MoneyGreen,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = amount,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = Color(0xFF1B5E20),
                    fontFamily = quang.app.luckymoney.ui.theme.NotoSerifDisplay,
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                )
            )

            Text(
                text = "NGÂN HÀNG NHÀ NƯỚC VIỆT NAM",
                fontSize = 5.sp,
                color = MoneyGreen,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        
        // Circular decoration
        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterStart)
                .offset(x = 10.dp)
                .background(MoneyGreen.copy(alpha = 0.1f), RoundedCornerShape(15.dp))
                .border(0.5.dp, MoneyGreen.copy(alpha = 0.2f), RoundedCornerShape(15.dp))
        )
    }
}
