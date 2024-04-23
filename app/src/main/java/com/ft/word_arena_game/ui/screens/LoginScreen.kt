package com.ft.word_arena_game.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ft.word_arena_game.R
import com.ft.word_arena_game.ui.components.GameButton
import com.ft.word_arena_game.ui.components.GameTextField
import com.ft.word_arena_game.ui.components.SignUpText
import com.ft.word_arena_game.ui.navigation.Destinations.RoomTypeSelectionRoute
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(), // Box ekranın tamamını kaplasın
        contentAlignment = Alignment.Center // Box içindeki içeriği merkezle
    ) {
        Image(
            painter = painterResource(id = R.drawable.loginback), // Resmi drawable'dan yükle
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
                val formattedUsername =
                    username.replace(" ", "_") // Boşlukları alt çizgi ile değiştir
                val email = "$formattedUsername@wordarena.com"
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Giriş başarılı, ana ekrana yönlendir
                            Toast.makeText(context, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                            navController.navigate(RoomTypeSelectionRoute) // Ana ekran yönlendirmesi güncellenebilir
                        } else {
                            // Giriş başarısız, hata mesajını göster
                            Toast.makeText(
                                context,
                                "Giriş başarısız: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        )

        SignUpText(onClick = { navController.navigate("signup") })
    }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}