package com.ft.word_arena_game.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ft.word_arena_game.ui.components.GameButton
import com.ft.word_arena_game.ui.components.GameTextField
import com.ft.word_arena_game.ui.components.SignUpText
import com.ft.word_arena_game.ui.navigation.Destinations.RoomSelectionRoute
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1E1E1E)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "WordArena'ya Hoş Geldiniz!",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        GameTextField(
            label = "Kullanıcı Adı",
            value = username,
            onValueChange = { username = it }
        )

        GameTextField(
            label = "Şifre",
            value = password,
            onValueChange = { password = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        GameButton(
            text = "Giriş Yap",
            onClick = {
                val formattedUsername = username.replace(" ", "_") // Boşlukları alt çizgi ile değiştir
                val email = "$formattedUsername@wordarena.com"
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Giriş başarılı, ana ekrana yönlendir
                            Toast.makeText(context, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                            navController.navigate(RoomSelectionRoute) // Ana ekran yönlendirmesi güncellenebilir
                        } else {
                            // Giriş başarısız, hata mesajını göster
                            Toast.makeText(context, "Giriş başarısız: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        )

        SignUpText(onClick = { navController.navigate("signup") })
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}