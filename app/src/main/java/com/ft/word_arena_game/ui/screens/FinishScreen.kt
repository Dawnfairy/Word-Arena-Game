package com.ft.word_arena_game.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


data class PlayerResult(
    val username: String?,
    val status: String?, // Oyuncunun tahminleri
    val puan: Int, // Tahmin için harcanan süre
    val saripuan: Int, // Puan detayları
    val yesilpuan: Int, // Puan detayları
    val kalansaniye: Int // Puan detayları

)

@Composable
fun FinishScreen(
    navController: NavController,
    selectedGameType: Boolean,
    selectedRoom: String,
    rivalId: String // Düello isteği için callback
) {
    val user = Firebase.auth.currentUser
    val userId = user!!.uid
    var playerOneResult by remember { mutableStateOf<PlayerResult?>(null) }
    var playerTwoResult by remember { mutableStateOf<PlayerResult?>(null) }
    DisposableEffect(key1 = userId) {
        findUsernameById(userId, FirebaseFirestore.getInstance()) { username ->
            // Kullanıcı adı bulunduğunda, diğer verileri çek
            val propertyNames = listOf("status", "puan", "saripuan", "yesilpuan", "kalansaniye")
            fetchDocumentData(selectedGameType, selectedRoom, userId, propertyNames) { playerData ->
                // Tüm veriler toplandığında, `PlayerResult` nesnesini oluştur
                playerOneResult = PlayerResult(
                    username = username ?: "Bilinmiyor",
                    status = playerData["status"] as? String ?: "",
                    puan = (playerData["puan"] as? Number)?.toInt() ?: 0,
                    saripuan = (playerData["saripuan"] as? Number)?.toInt() ?: 0,
                    yesilpuan = (playerData["yesilpuan"] as? Number)?.toInt() ?: 0,
                    kalansaniye = (playerData["kalansaniye"] as? Number)?.toInt() ?: 0
                )
            }
        }
        onDispose {  }
    }

    DisposableEffect(key1 = rivalId) {
        findUsernameById(rivalId, FirebaseFirestore.getInstance()) { username ->
            // Kullanıcı adı bulunduğunda, diğer verileri çek
            val propertyNames = listOf("status", "puan", "saripuan", "yesilpuan", "kalansaniye")
            fetchDocumentData(selectedGameType, selectedRoom, rivalId, propertyNames) { playerData ->
                // Tüm veriler toplandığında, `PlayerResult` nesnesini oluştur
                playerTwoResult = PlayerResult(
                    username = username ?: "Bilinmiyor",
                    status = playerData["status"] as? String ?: "",
                    puan = (playerData["puan"] as? Number)?.toInt() ?: 0,
                    saripuan = (playerData["saripuan"] as? Number)?.toInt() ?: 0,
                    yesilpuan = (playerData["yesilpuan"] as? Number)?.toInt() ?: 0,
                    kalansaniye = (playerData["kalansaniye"] as? Number)?.toInt() ?: 0
                )
            }
        }
        onDispose {  }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Oyun Sonuçları", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(16.dp))
        playerOneResult?.let { PlayerResultSection(playerResult = it) }
        Spacer(modifier = Modifier.height(8.dp))
        playerTwoResult?.let { PlayerResultSection(playerResult = it) }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Düello")
        }
    }
}

@Composable
fun PlayerResultSection(playerResult: PlayerResult) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "${playerResult.status} oyuncu ''${playerResult.username}''", style = MaterialTheme.typography.titleLarge)
        Text(text = "Sonuçlar", style = MaterialTheme.typography.titleMedium)
        if(playerResult.puan != 0){
            Text(text = "Sarı alan: ${playerResult.saripuan} puan", style = MaterialTheme.typography.bodySmall)
            Text(text = "Yeşil alan: ${playerResult.yesilpuan} puan", style = MaterialTheme.typography.bodySmall)
            Text(text = "Kalan süre: ${playerResult.kalansaniye} puan", style = MaterialTheme.typography.bodySmall)
            Text(text = "Skor: ${playerResult.puan} puan", style = MaterialTheme.typography.bodyMedium)
        }else {
            Text(text = "Tahmin süresi: ${60 - playerResult.saripuan} puan", style = MaterialTheme.typography.bodySmall)
        }

        // Tahminler, süre ve puanlar burada listelenecek
        // Örneğin, for döngüsü ile puan detaylarını gösterebilirsiniz
    }
}
fun fetchDocumentData(
    gameType: Boolean,
    roomType: String,
    userId: String,
    propertyNames: List<String>,
    onDataFetched: (Map<String, Any?>) -> Unit // Bu callback eklendi
) {
    val db = FirebaseFirestore.getInstance()
    val documentPath = "game_rooms/$gameType/rooms/$roomType/gamer$roomType/$userId"
    db.document(documentPath).get().addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot.exists()) {
            val data = hashMapOf<String, Any?>()
            for (propertyName in propertyNames) {
                data[propertyName] = documentSnapshot.get(propertyName)
            }
            onDataFetched(data) // Veriler çekildikten sonra callback çağrılıyor
        } else {
            Log.d("fetchDocumentData", "No such document")
            onDataFetched(emptyMap())
        }
    }.addOnFailureListener { exception ->
        Log.e("fetchDocumentData", "Error getting document: ", exception)
        onDataFetched(emptyMap())
    }
}