package com.example.flowtodo

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class Screens(val route : String) {
    object ToDo : Screens("to_do_list")
    object Flow : Screens("weekly_flow")
    object Stats : Screens("statistics")
}

@Composable
fun getScreenTitle(route: String?): String {
    return when (route) {
        "to_do_list" -> stringResource(R.string.to_do_list)
        "weekly_flow" -> stringResource(R.string.weekly_flow)
        "statistics" -> stringResource(R.string.statistics)
        else -> return ""
    }
}
