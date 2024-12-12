package com.example.flowtodo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val fromWeekday: Int,
    val fromHour: Int,
    val fromMinute: Int,
    val toWeekday: Int,
    val toHour: Int,
    val toMinute: Int
)
