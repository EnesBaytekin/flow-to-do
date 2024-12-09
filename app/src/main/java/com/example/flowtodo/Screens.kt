package com.example.flowtodo

sealed class Screens(val route : String) {
    object ToDo : Screens("To-Do List")
    object Flow : Screens("Weekly To Do Flow")
    object Stats : Screens("Statistics")
}
