package quang.app.luckymoney.ui

object Wishes {
    val tetWishes = listOf(
        "An Khang Thịnh Vượng",
        "Vạn Sự Như Ý",
        "Cung Chúc Tân Xuân",
        "Phát Tài Phát Lộc",
        "Sức Khỏe Dồi Dào",
        "Tấn Tài Tấn Lộc",
        "Tiền Vào Như Nước",
        "Vạn Hạnh Thông",
        "Hạnh Phúc Đong Đầy",
        "Niềm Vui Phơi Phới"
    )

    fun getRandomWish(): String = tetWishes.random()
}
