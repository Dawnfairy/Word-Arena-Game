package com.ft.word_arena_game.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ft.word_arena_game.R


@Composable
fun RoomTypeSelectionScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(), // Box ekranın tamamını kaplasın
        contentAlignment = Alignment.Center // Box içindeki içeriği merkezle
    ) {
        Image(
            painter = painterResource(id = R.drawable.ftk5), // Resmi drawable'dan yükle
            contentDescription = "Background Image", // Erişilebilirlik için açıklama
            modifier = Modifier.fillMaxSize(), // Resmi ekranın tamamına yay
            contentScale = ContentScale.Crop // Resmi ekranın boyutlarına uydur
        )
        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = "Oyun Türü Seçiniz",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { navController.navigate("roomSelection/true") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF552465)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(32.dp, 0.dp)
        ) {
            Text(
                text = "Harf Sabiti Var",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("roomSelection/false") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF552465)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(32.dp, 0.dp)
        ) {
            Text(
                text = "Harf Sabiti Yok",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
}