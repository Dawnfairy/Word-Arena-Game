package com.ft.word_arena_game.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RoomOptionsScreen(onRoomSelected: (String) -> Unit ,  selectedGameType: String?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Oda Seçin: $selectedGameType")
        // 4 Harfli Oda
        RoomOptionButton("4 Harfli Oda", onRoomSelected)
        Spacer(modifier = Modifier.height(8.dp))

        // 5 Harfli Oda
        RoomOptionButton("5 Harfli Oda", onRoomSelected)
        Spacer(modifier = Modifier.height(8.dp))

        // 6 Harfli Oda
        RoomOptionButton("6 Harfli Oda", onRoomSelected)
        Spacer(modifier = Modifier.height(8.dp))

        // 7 Harfli Oda
        RoomOptionButton("7 Harfli Oda", onRoomSelected)
    }
}

@Composable
fun RoomOptionButton(roomName: String, onRoomSelected: (String) -> Unit) {
    Button(
        onClick = { onRoomSelected(roomName) },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = roomName,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}
