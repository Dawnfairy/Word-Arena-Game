package com.ft.word_arena_game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun RoomTypeSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1E1E1E))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Oyun Türü Seçiniz",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { navController.navigate("roomSelection/Harf Sabiti Var")  },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1363E8)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(32.dp,0.dp)
        ) {
            Text(
                text = "Harf Sabiti Var",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("roomSelection/Harf Sabiti Yok")  },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8135B)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(32.dp,0.dp)
        ) {
            Text(
                text = "Harf Sabiti Yok",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}