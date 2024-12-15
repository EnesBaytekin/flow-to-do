package com.example.flowtodo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimePickerButton(text: String = "time", hour: Int, minute: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 12.dp)
                .clickable(
                    onClick = onClick
                )
        ) {
            Text(
                text = "%02d:%02d".format(hour, minute),
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }
    }
}