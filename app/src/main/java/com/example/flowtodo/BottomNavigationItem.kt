package com.example.flowtodo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "ToDo's",
                icon = Icons.Filled.Edit,
                route = Screens.ToDo.route
            ),
            BottomNavigationItem(
                label = "Flow",
                icon = Icons.Filled.DateRange,
                route = Screens.Flow.route
            ),
            BottomNavigationItem(
                label = "Stats",
                icon = Icons.Filled.Info,
                route = Screens.Stats.route
            ),
        )
    }
}