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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ft.word_arena_game.R

@Composable
fun RoomSelectionScreen(navController: NavController, roomType: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(), // Box ekranın tamamını kaplasın
        contentAlignment = Alignment.Center // Box içindeki içeriği merkezle
    ) {
        Image(
            painter = painterResource(id = R.drawable.ftk1), // Resmi drawable'dan yükle
            contentDescription = "Background Image", // Erişilebilirlik için açıklama
            modifier = Modifier.fillMaxSize(), // Resmi ekranın tamamına yay
            contentScale = ContentScale.Crop // Resmi ekranın boyutlarına uydur
        )
        Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        val roomTypeText = if (roomType) "Harf Sabiti Var" else "Harf Sabiti Yok"
        Text(
            text = "Oda Türü: $roomTypeText",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // 4 Harfli Oda
        RoomOptionButton(navController, roomType, "4")
        Spacer(modifier = Modifier.height(16.dp))

        // 5 Harfli Oda
        RoomOptionButton(navController, roomType, "5")
        Spacer(modifier = Modifier.height(16.dp))

        // 6 Harfli Oda
        RoomOptionButton(navController, roomType, "6")
        Spacer(modifier = Modifier.height(16.dp))

        // 7 Harfli Oda
        RoomOptionButton(navController, roomType, "7")
    }
}
}

@Composable
fun RoomOptionButton(navController: NavController,roomType: Boolean, roomNumber: String) {
    Button(
        onClick = { navController.navigate("room/$roomType/$roomNumber") },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222259)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp,0.dp)
            .height(68.dp)
    ) {
        Text(
            text = "$roomNumber Harfli Oda",
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}
@Preview(showBackground = true)
@Composable
fun RoomSelectionScreenPreview() {
    val navController = rememberNavController()
    RoomSelectionScreen(navController, true)
}