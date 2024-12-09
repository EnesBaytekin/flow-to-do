package com.example.flowtodo

import androidx.room.*

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM task_table")
    suspend fun getAllTasks(): List<Task>

    @Query("UPDATE task_table SET isCompleted = NOT isCompleted WHERE id = :taskId")
    suspend fun toggleTaskCompletion(taskId: Int)
}
