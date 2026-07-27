package quang.app.luckymoney.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

sealed interface NavRoute : NavKey {
    @Serializable
    data object Welcome : NavRoute

    @Serializable
    data object SetupCount : NavRoute

    @Serializable
    data object SetupAmount : NavRoute

    @Serializable
    data object Shuffle : NavRoute

    @Serializable
    data object Selection : NavRoute
}
