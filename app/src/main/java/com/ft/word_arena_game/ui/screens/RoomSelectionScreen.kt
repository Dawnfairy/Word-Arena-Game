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
import androidx.navigation.NavController

@Composable
fun RoomSelectionScreen(navController: NavController, roomType: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Oda Seçin: $roomType")
        // 4 Harfli Oda
        RoomOptionButton(navController,roomType,"4 Harfli Oda")
        Spacer(modifier = Modifier.height(8.dp))

        // 5 Harfli Oda
        RoomOptionButton(navController,roomType,"5 Harfli Oda")
        Spacer(modifier = Modifier.height(8.dp))

        // 6 Harfli Oda
        RoomOptionButton(navController,roomType,"6 Harfli Oda")
        Spacer(modifier = Modifier.height(8.dp))

        // 7 Harfli Oda
        RoomOptionButton(navController,roomType,"7 Harfli Oda")
    }
}

@Composable
fun RoomOptionButton(navController: NavController,roomType: String, roomNumber: String) {
    Button(
        onClick = { navController.navigate("room/$roomType/$roomNumber") },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = roomNumber,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}
