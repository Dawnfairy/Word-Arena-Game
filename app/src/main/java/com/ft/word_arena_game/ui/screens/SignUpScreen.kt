package com.ft.word_arena_game.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ft.word_arena_game.R
import com.ft.word_arena_game.ui.components.GameButton
import com.ft.word_arena_game.ui.components.GameTextField
import com.ft.word_arena_game.ui.navigation.Destinations.LoginRoute
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

fun checkUsernameUnique(username: String, onResult: (Boolean) -> Unit) {
    val db = Firebase.firestore
    val usernamesRef = db.collection("usernames").document(username)
    usernamesRef.get().addOnSuccessListener { document ->
        onResult(!document.exists()) // Eğer döküman mevcut değilse, kullanıcı adı benzersizdir.
    }.addOnFailureListener {
        onResult(false) // Bir hata oluştuğunda varsayılan olarak benzersiz olmadığını varsay.
    }
}

fun registerUser(username: String, password: String, onResult: (Boolean, String) -> Unit) {
    val formattedUsername = username.replace(" ", "_") // Boşlukları alt çizgi ile değiştir
    FirebaseAuth.getInstance().createUserWithEmailAndPassword("$formattedUsername@wordarena.com", password)
        .addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                // Kullanıcı başarıyla oluşturuldu, şimdi kullanıcı adını kaydedelim
                val userId = authTask.result?.user?.uid ?: return@addOnCompleteListener onResult(false, "No user ID found")
                val userMap = hashMapOf("username" to username, "userId" to userId, "password" to password)
                Firebase.firestore.collection("usernames").document(userId)
                    .set(userMap)
                    .addOnSuccessListener {
                        onResult(true, "Kayıt başarılı!")
                    }.addOnFailureListener {
                        onResult(false, it.message ?: "Kayıt Başarısız!")
                    }
            } else {
                // Kullanıcı oluşturma başarısız, hata yönetimi
                onResult(false, authTask.exception?.message ?: "Hata.")
            }
        }
}

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

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
        if (loading) {
            // Burada yükleniyor durumunu göstermek için bir ProgressBar eklenebilir.
            CircularProgressIndicator()
        } else {

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
                text = "Kayıt Ol",
                onClick = {
                    loading = true
                    // Önce kullanıcı adının benzersizliğini kontrol et
                    checkUsernameUnique(username) { isUnique ->
                        if (isUnique) {
                            // Eğer kullanıcı adı benzersiz ise kayıt işlemine devam ediyoruz.
                            registerUser(username, password) { isSuccessful, message ->
                                if (isSuccessful) {
                                    // Kayıt başarılı ise kullanıcıyı bir sonraki ekrana yönlendiriyoruz.
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    navController.navigate(LoginRoute)
                                    loading = false
                                } else {
                                    // Kayıt başarısız ise kullanıcıya bir hata mesajı gösteriyoruz.
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            // Eğer kullanıcı adı benzersiz değilse bir hata mesajı gösteriyoruz.
                            Toast.makeText(
                                context,
                                "Kullanıcı adı zaten alınmış!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        loading = false
                    }
                } // Kayıt işlemi tamamlandığında ne olacağını belirt
            )
            //SignUpText(onClick = { navController.popBackStack() })
        }
    }
}
}

@Composable
@Preview(showBackground = true)
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    SignUpScreen(navController = navController)
}