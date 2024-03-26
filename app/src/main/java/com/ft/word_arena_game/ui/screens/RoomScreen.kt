package com.ft.word_arena_game.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RoomScreen(navController: NavController, selectedGameType: String, selectedRoom: String) {
    val user = Firebase.auth.currentUser
    var hasExited by remember { mutableStateOf(false) }

    // Kullanıcı odadan çıktığında tekrar ekleme işlemini önlemek için hasExited durumunu kullanın
    if (user != null && !hasExited) {
        LaunchedEffect(user.uid, selectedGameType, selectedRoom) {
            // Kullanıcı odadan çıkmadıysa odaya ekle
            addUserToRoom(user, selectedGameType, selectedRoom)
        }
    }
    var showConfirmDialog by remember { mutableStateOf(false) }
    if (showConfirmDialog) {
        // Kullanıcı odadan çıkmak istediğinde diyalogu göster
        ConfirmExitDialog(
            onConfirm = {
                showConfirmDialog = false
                if (!hasExited) {
                    hasExited = true // Kullanıcı odadan çıkıyor, tekrar ekleme işlemini önle
                    if (user != null) {
                        removeUserFromRoom(user, selectedGameType, selectedRoom) {
                            // Kullanıcı başarıyla odadan çıkarıldıktan sonra geri dönmek için
                            navController.popBackStack()
                        }
                    }
                }
            },
            onDismiss = { showConfirmDialog = false }
        )
    }

    Column {
        Button(onClick = { showConfirmDialog = true }) {
            Text("Odadan Çık")
        }
        // Kullanıcı odada olduğu sürece oyuncuları listele
        if (!hasExited) {
            val players = remember { mutableStateListOf<String>() }
            LaunchedEffect(selectedGameType, selectedRoom) {
                // Odayı dinliyoruz, değişiklik olursa oyuncu listesini güncelliyoruz
                listenToRoomPlayers(selectedGameType, selectedRoom) { newPlayers ->
                    players.clear()
                    players.addAll(newPlayers)
                }
            }

            LazyColumn {
                items(players) { playerName ->
                    Text(text = "Kullanıcı Adı: $playerName")
                }
            }
        } else {
            Text(text = "Odadan ayrıldınız.")
        }
    }
}


fun addUserToRoom(user: FirebaseUser, gameType: String, roomType: String) {//kullanıcıyı odaya ekle
    val db = FirebaseFirestore.getInstance()

    db.collection("usernames").document(user.uid).get().addOnSuccessListener { document ->
        val username = document.getString("username") ?: "Bilinmeyen Kullanıcı"
        Log.d("Username", "Kullanıcı Adı: $username")

        // Oyuncunun bilgilerini içeren bir map oluşturun
        val userData = hashMapOf(
            "userId" to user.uid,
            "userName" to username,
            "joinedAt" to FieldValue.serverTimestamp() // Katılma zamanı
        )

        // Belirlenen yolda oyuncuyu 'players' koleksiyonuna ekleyin
        val path = "game_rooms/$gameType/rooms/$roomType/players/${user.uid}"
        val userRef = db.document(path)

        // Kullanıcıyı odanın 'players' koleksiyonuna belge olarak ekleyin
        userRef.set(userData)
            .addOnSuccessListener {
                Log.d("AddUserToRoom", "User successfully added to the room's players collection with username: $username")
            }
            .addOnFailureListener { e ->
                Log.e("AddUserToRoom", "Failed to add user to the room's players collection.", e)
            }
    }.addOnFailureListener { exception ->
        Log.d("Username", "get failed with ", exception)
    }
}
fun removeUserFromRoom(user: FirebaseUser, gameType: String, roomType: String, onRemoved: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val path = "game_rooms/$gameType/rooms/$roomType/players/${user.uid}"
    val userRef = db.document(path)

    userRef.delete()
        .addOnSuccessListener {
            Log.d("RemoveUserFromRoom", "User successfully removed from the room.")
            onRemoved() // Çıkarıldıktan sonra callback fonksiyonunu çağır
        }
        .addOnFailureListener { e ->
            Log.e("RemoveUserFromRoom", "Failed to remove user from the room.", e)
        }
}
fun listenToRoomPlayers(gameType: String, roomType: String, onPlayersChanged: (List<String>) -> Unit) {//oyuncu listesini döndürür
    val db = FirebaseFirestore.getInstance()
    val path = "game_rooms/$gameType/rooms/$roomType/players"
    val playersRef = db.collection(path)

    playersRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
            Log.w("ListenToRoomPlayers", "Listen failed.", error)
            return@addSnapshotListener
        }

        val newPlayers = snapshot?.documents?.mapNotNull { it.getString("userName") } ?: listOf()
        onPlayersChanged(newPlayers)
    }
}

@Composable
fun ConfirmExitDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Odadan Çıkış") },
        text = { Text("Odadan çıkmak istediğinize emin misiniz?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Evet")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Hayır")
            }
        }
    )
}
