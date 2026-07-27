package quang.app.luckymoney.utils

import quang.app.luckymoney.R

object MoneyUtils {
    /**
     * Breaks down an amount into the largest available denominations.
     * Denominations: 500k, 200k, 100k, 50k, 20k, 10k, 5k, 2k, 1k.
     */
    fun getMoneyBreakdown(amount: Int): List<Int> {
        val denominations = listOf(
            500_000 to R.drawable.money_500k,
            200_000 to R.drawable.money_200k,
            100_000 to R.drawable.money_100k,
            50_000 to R.drawable.money_50k,
            20_000 to R.drawable.money_20k,
            10_000 to R.drawable.money_10k,
            5_000 to R.drawable.money_5k,
            2_000 to R.drawable.money_2k,
            1_000 to R.drawable.money_1k
        )

        val result = mutableListOf<Int>()
        var remaining = amount

        for ((value, drawableRes) in denominations) {
            while (remaining >= value) {
                result.add(drawableRes)
                remaining -= value
            }
        }

        return result
    }

    /**
     * Formats an amount to Vietnamese currency string.
     * Example: 17000 -> 17.000đ
     */
    fun formatCurrency(amount: Int): String {
        return "%,dđ".format(amount).replace(',', '.')
    }
}
