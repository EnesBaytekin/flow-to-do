package com.example.flowtodo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    @Composable
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = stringResource(R.string.todos),
                icon = Icons.Filled.Edit,
                route = Screens.ToDo.route
            ),
            BottomNavigationItem(
                label = stringResource(R.string.flow),
                icon = Icons.Filled.DateRange,
                route = Screens.Flow.route
            ),
            BottomNavigationItem(
                label = stringResource(R.string.stats),
                icon = Icons.Filled.Info,
                route = Screens.Stats.route
            ),
        )
    }
}