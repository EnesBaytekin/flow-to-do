package com.example.flowtodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flowtodo.ui.theme.FlowToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowToDoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlowToDo(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FlowToDo(modifier: Modifier = Modifier) {
    BottomNavigationBar()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    FlowToDoTheme(true) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            FlowToDo(Modifier.padding(innerPadding))
        }
    }
}
