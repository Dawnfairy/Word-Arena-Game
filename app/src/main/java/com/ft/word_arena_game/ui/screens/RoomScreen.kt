package com.ft.word_arena_game.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


data class User(
    val userId: String,
    val username: String,
    var status: String
)

data class ChallengeRequest(

    val id: String = "",
    val fromUserId: String = "",
    val toUserId: String = "",
    val timestamp: Timestamp? = null,
    val status: String = ""
)


@Composable
fun RoomScreen(navController: NavController, selectedGameType: Boolean, selectedRoom: String) {
    val context = LocalContext.current // Context'i al
    val user = Firebase.auth.currentUser
    var hasExited by remember { mutableStateOf(false) }


    // Kullanıcı odadan çıktığında tekrar ekleme işlemini önlemek için hasExited durumunu kullanın//odaya ekleme
    if (user != null && !hasExited) {
        LaunchedEffect(user.uid, selectedGameType, selectedRoom) {
            // Kullanıcı odadan çıkmadıysa odaya ekle
            addUserToRoom(user, selectedGameType, selectedRoom)
        }
    }
    var showConfirmDialog by remember { mutableStateOf(false) }
    //odadan çıkarma
    if (showConfirmDialog) {
        ConfirmExitDialog(
            onConfirm = {
                showConfirmDialog = false
                if (!hasExited) {
                    hasExited = true // Kullanıcı odadan çıkıyor, tekrar ekleme işlemini önle
                    if (user != null) {
                        removeUserFromRoom(user, selectedGameType, selectedRoom) {
                            navController.popBackStack()
                        }
                    }
                }
            },
            onDismiss = { showConfirmDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
        //.background(color = Color(0xFF1E1E1E)),
        ,
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = { showConfirmDialog = true }) {
            Text("Odadan Çık")
        }
        // Kullanıcı odada olduğu sürece oyuncuları listele
        var remainingTime by remember { mutableStateOf<Long?>(null) }
        var remainingTime1 by remember { mutableStateOf<Long?>(null) }

        if (!hasExited) {
            var currentRequest by remember { mutableStateOf<ChallengeRequest?>(null) }
            var showDialog by remember { mutableStateOf(false) }
            var showDialog1 by remember { mutableStateOf(false) }

            val players = remember { mutableStateListOf<User>() }
            // Odayı dinliyoruz, değişiklik olursa oyuncu listesini güncelliyoruz
            LaunchedEffect(selectedGameType, selectedRoom) {
                listenToRoomPlayers(selectedGameType, selectedRoom) { newPlayers ->
                    players.clear()
                    players.addAll(newPlayers)
                }
            }
            LaunchedEffect(Firebase.auth.currentUser?.uid) { //istekleri dinliyor
                Firebase.auth.currentUser?.uid?.let { userId ->
                    listenForChallengeRequests(userId, FirebaseFirestore.getInstance()) { request ->
                        currentRequest = request
                        showDialog1 = true
                        remainingTime1 = null
                        if (user != null) {
                            startCountdown(
                                FirebaseFirestore.getInstance(),
                                user.uid,
                                currentRequest!!.id,
                                onCountdownUpdate = { timeLeft1 ->
                                    remainingTime1 = timeLeft1
                                })
                        }
                    }
                }
            }
            if (showDialog1 && currentRequest != null) {
                Log.d(
                    "hataa",
                    "show diyolag1 truueeeeeee"
                )
                var currentRequestUsername by remember { mutableStateOf<String?>(null) }
                findUsernameById(
                    currentRequest!!.fromUserId,
                    FirebaseFirestore.getInstance()
                ) { username ->
                    currentRequestUsername = username
                }

                remainingTime1?.let {
                    if (remainingTime1!! > 0L && showDialog1) {
                        AlertDialog(
                            onDismissRequest = {
                                // İsteği reddet
                                showDialog1 = false
                                respondToChallengeRequest(
                                    request = currentRequest!!,
                                    response = "rejected",
                                    db = FirebaseFirestore.getInstance(),
                                    onSuccess = {
                                        Log.d(
                                            "ChallengeResponse",
                                            "Challenge request rejected successfully."
                                        )
                                    },
                                    onFailure = { e ->
                                        Log.e(
                                            "ChallengeResponse",
                                            "Error rejecting challenge request",
                                            e
                                        )
                                    }
                                )
                            },
                            title = { Text("Meydan Okuma İsteği") },
                            text = {
                                Text("$currentRequestUsername tarafından meydan okuma isteği geldi. \nCevap için kalan süre: $remainingTime1 saniye")
                            },

                            confirmButton = {
                                Button(onClick = {
                                    // İsteği kabul et
                                    showDialog1 = false
                                    respondToChallengeRequest(
                                        request = currentRequest!!,
                                        response = "accepted",
                                        db = FirebaseFirestore.getInstance(),
                                        onSuccess = {
                                            Log.d(
                                                "ChallengeResponse",
                                                "Challenge request accepted successfully."
                                            )
                                        },
                                        onFailure = { e ->
                                            Log.e(
                                                "ChallengeResponse",
                                                "Error accepting challenge request",
                                                e
                                            )
                                        }
                                    )
                                    user?.let { it1 ->
                                        val (randomLetter, wordIndex) = getRandomTurkishLetterAndWordIndex(selectedRoom)
                                        updateUserStatus(
                                            selectedGameType,
                                            selectedRoom,
                                            it1.uid,
                                            "oyunda"
                                        )
                                        updateGamerInfoInRoom(selectedGameType, selectedRoom, it1.uid, false)
                                        val rivalId = currentRequest!!.fromUserId
                                        println("rakip ıdddddddddddddddddd $rivalId")
                                        navController.navigate("game/$selectedGameType/$selectedRoom/$randomLetter/$wordIndex/$rivalId/false")
                                    }


                                }) {
                                    Text("Kabul Et")
                                }
                            },
                            dismissButton = {
                                Button(onClick = {
                                    // İsteği reddet
                                    showDialog1 = false
                                    respondToChallengeRequest(
                                        request = currentRequest!!,
                                        response = "rejected",
                                        db = FirebaseFirestore.getInstance(),
                                        onSuccess = {
                                            Log.d(
                                                "ChallengeResponse",
                                                "Challenge request rejected successfully."
                                            )
                                        },
                                        onFailure = { e ->
                                            Log.e(
                                                "ChallengeResponse",
                                                "Error rejecting challenge request",
                                                e
                                            )
                                        }
                                    )
                                }) {
                                    Text("Reddet")
                                }
                            }
                        )
                    } else if (remainingTime1!! <= 0L && showDialog1) {

                        showDialog1 = false
                        respondToChallengeRequest(
                            request = currentRequest!!,
                            response = "rejected",
                            db = FirebaseFirestore.getInstance(),
                            onSuccess = {
                                Log.d(
                                    "ChallengeResponse",
                                    "Challenge request rejected successfully."
                                )
                            },
                            onFailure = { e ->
                                Log.e("ChallengeResponse", "Error rejecting challenge request", e)
                            }
                        )
                    }
                }

            }

            LazyColumn {
                items(players) { user ->
                    var requestId by remember { mutableStateOf<String?>(null) }

                    Card(
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .fillMaxWidth()
                            .clickable {
                                if (Firebase.auth.currentUser != null) {
                                    sendChallengeRequest(//meydan okuma isteği gönder
                                        fromUserId = Firebase.auth.currentUser!!.uid,
                                        toUserId = user.userId,
                                        roomType = selectedRoom,
                                        gameType = selectedGameType,
                                        onSuccess = { challengeRequestId ->
                                            // İstek başarıyla gönderildiğinde isteğe verilen yanıtı dinle
                                            requestId = "$challengeRequestId"
                                            Log.d(
                                                "ChallengeRequest",
                                                "Request sent successfully to ${user.username}"
                                            )
                                            showDialog = true
                                            // Geri sayımı başlat
                                            startCountdown(
                                                FirebaseFirestore.getInstance(),
                                                user.userId,
                                                "$challengeRequestId",
                                                onCountdownUpdate = { timeLeft ->
                                                    remainingTime = timeLeft
                                                    if (timeLeft <= 0L) {
                                                        // Geri sayım bittiğinde showDialog'u false yap
                                                        showDialog = false
                                                    }
                                                })
                                        },
                                        onFailure = { exception ->
                                            // İstek gönderilirken bir hata oluştuğunda
                                            Toast
                                                .makeText(
                                                    context,
                                                    exception.message,
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()

                                            Log.e(
                                                "ChallengeRequest",
                                                "Failed to send request",
                                                exception
                                            )
                                        }
                                    )
                                }

                                Log.d("PlayerClicked", "Clicked on player: ${user.username}")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = user.username,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = user.status,
                                fontWeight = FontWeight.Light,
                                fontSize = 8.sp
                            )
                        }

                    }

                    if (showDialog && requestId != null) {
                        Log.d(
                            "hataa",
                            "show diyolag truueeeeeee"
                        )
                        if (remainingTime != null && remainingTime!! > 0) {
                            Dialog(onDismissRequest = { /* Geri sayım sırasında dialog kapatılamaz */ }) {
                                // Dialog içeriğinde yalnızca geri sayımı göster
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.5f)) // Arka planı yarı saydam siyah yap
                                        .padding(32.dp) // İçerik ve kenarlar arasında boşluk bırak
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        CircularProgressIndicator(color = Color.White) // Yükleniyor simgesi
                                        Spacer(modifier = Modifier.height(16.dp)) // Simge ve metin arasında boşluk
                                        Text(
                                            "Cevap için kalan süre: $remainingTime saniye",
                                            color = Color.White // Metin rengi
                                        )
                                    }
                                }
                            }
                        }

                        requestId?.let { listenrequestId ->
                            LaunchedEffect(listenrequestId) {//istek yanıtını dinle
                                listenForChallengeRequestResponse(
                                    challengeRequestId = listenrequestId,
                                    userId = user.userId,
                                    db = FirebaseFirestore.getInstance(),
                                    onStatusChanged = { status ->
                                        when (status) {
                                            "accepted" -> {
                                                Toast.makeText(
                                                    context,
                                                    "${user.username} isteğinizi kabul etti.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                val (randomLetter, wordIndex) = getRandomTurkishLetterAndWordIndex(selectedRoom)
                                                updateUserStatus(
                                                    selectedGameType,
                                                    selectedRoom,
                                                    Firebase.auth.currentUser!!.uid,
                                                    "oyunda"
                                                )
                                                updateGamerInfoInRoom(selectedGameType, selectedRoom, Firebase.auth.currentUser!!.uid , false)
                                                val rivalId = user.userId
                                                println("rakip ıdddddddddddddddddd $rivalId")
                                                navController.navigate("game/$selectedGameType/$selectedRoom/$randomLetter/$wordIndex/$rivalId/false")
                                            }


                                            "rejected"
                                            -> {
                                                Toast.makeText(
                                                    context,
                                                    "${user.username} isteğinizi reddetti. ",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                        Log.d(
                                            "ChallengeStatus",
                                            "Challenge request $listenrequestId status is now $status"
                                        )

                                    }
                                )
                            }
                        }
                    }
                }
            }

        } else {
            Text(text = "Odadan ayrıldınız.")
        }
    }
}

fun sendChallengeRequest(
    fromUserId: String,
    toUserId: String,
    roomType: String,
    gameType: Boolean,
    onSuccess: (Any?) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val path = "game_rooms/$gameType/rooms/$roomType/players/$toUserId"
    db.document(path).get().addOnSuccessListener { document ->
        val status = document.getString("status")
        if (status == "aktif") {
            val challengeRequest = hashMapOf(
                "toUserId" to toUserId,
                "fromUserId" to fromUserId,
                "timestamp" to FieldValue.serverTimestamp(),
                "status" to "beklemede",// "accepted", "declined" veya "expired" da olabilir
            )
            db.collection("user_requests").document(toUserId).collection("requests")
                .add(challengeRequest)
                .addOnSuccessListener { documentReference ->
                    val challengeRequestId = documentReference.id // Oluşturulan belgenin ID'si
                    Log.d(
                        "sendChallengeRequest",
                        "Challenge request sent with ID: $challengeRequestId"
                    )

                    onSuccess(challengeRequestId)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("Kullanıcı şu anda oyunda ve meydan okuma isteği kabul edemiyor."))
        }

    }.addOnFailureListener { exception ->
        // Kullanıcının durumunu kontrol ederken hata oluşursa
        onFailure(Exception("Kullanıcı durumu kontrol edilirken bir hata oluştu: ${exception.message}"))
    }
}


fun listenForChallengeRequests(
    userId: String,
    db: FirebaseFirestore,
    onNewRequest: (ChallengeRequest) -> Unit
) {
    Log.d("fonksiyonnnn", "Listening for requests failed")
    db.collection("user_requests")
        .document(userId)
        .collection("requests")
        .whereEqualTo("status", "beklemede")
        .addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("ChallengeRequest", "Listening for requests failed", error)
                return@addSnapshotListener
            }

            snapshot?.documents?.forEach { document ->
                val id = document.id
                val fromUserId = document.getString("fromUserId") ?: ""
                val toUserId = document.getString("toUserId") ?: ""
                val timestamp = document.getTimestamp("timestamp")
                val status = document.getString("status") ?: ""
                // Alınan verilerle ChallengeRequest nesnesi oluştur
                val request = ChallengeRequest(
                    id = id,
                    fromUserId = fromUserId,
                    toUserId = toUserId,
                    timestamp = timestamp,
                    status = status
                )
                onNewRequest(request)
            }
        }
}

fun respondToChallengeRequest(
    request: ChallengeRequest,
    response: String,
    db: FirebaseFirestore,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val challengeRequestRef = db.collection("user_requests")
        .document(request.toUserId) // Meydan okuma isteğini alan kullanıcının ID'si
        .collection("requests")
        .document(request.id) // Meydan okuma isteğinin belge ID'si

    // İsteğin durumunu güncelle (kabul edildi olarak)
    challengeRequestRef
        .update("status", response) // response "accepted" ya da "rejected" olabilir
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun listenForChallengeRequestResponse(
    challengeRequestId: String,
    userId: String,
    db: FirebaseFirestore,
    onStatusChanged: (String) -> Unit
) {
    val challengeRequestRef = db.collection("user_requests")
        .document(userId)
        .collection("requests")
        .document(challengeRequestId)

    challengeRequestRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
            Log.e("listenForResponse", "Error listening for challenge response", error)
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()) {
            val status = snapshot.getString("status") ?: ""
            onStatusChanged(status)
        }
    }
}

fun startCountdown(
    db: FirebaseFirestore,
    userId: String,
    challengeRequestId: String,
    onCountdownUpdate: (Long) -> Unit
) {
    val countdownTime = 10L // 10 saniye
    var timeLeft = countdownTime

    // Meydan okuma isteğinin zamanlayıcısını başlat
    CoroutineScope(Dispatchers.Main).launch {
        while (timeLeft > 0) {
            delay(1000L) // 1 saniye beklet
            timeLeft-- // Her saniyede süreyi azalt
            onCountdownUpdate(timeLeft) // Güncellenen süreyi callback ile döndür

            // Meydan okuma isteğinin durumunu kontrol et
            val challengeRequestRef = db.collection("user_requests")
                .document(userId)
                .collection("requests")
                .document(challengeRequestId)

            challengeRequestRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val status = document.getString("status")
                    if (status == "accepted" || status == "rejected") {
                        // Meydan okuma kabul edildi veya reddedildi, geri sayımı durdur
                        this.cancel()
                        onCountdownUpdate(0L) // Süreyi sıfırla ve callback'i çağır
                    }
                }
            }
        }
    }
}


fun addUserToRoom(user: FirebaseUser, gameType: Boolean, roomType: String) {//kullanıcıyı odaya ekle
    val db = FirebaseFirestore.getInstance()

    db.collection("usernames").document(user.uid).get().addOnSuccessListener { document ->
        val username = document.getString("username") ?: "Bilinmeyen Kullanıcı"
        Log.d("Username", "Kullanıcı Adı: $username")

        // Oyuncunun bilgilerini içeren bir map oluşturun
        val userData = hashMapOf(
            "userId" to user.uid,
            "userName" to username,
            "joinedAt" to FieldValue.serverTimestamp(),
            "status" to "aktif"// Katılma zamanı
        )

        // Belirlenen yolda oyuncuyu 'players' koleksiyonuna ekleyin
        val path = "game_rooms/$gameType/rooms/$roomType/players/${user.uid}"
        val userRef = db.document(path)

        // Kullanıcıyı odanın 'players' koleksiyonuna belge olarak ekleyin
        userRef.set(userData)
            .addOnSuccessListener {
                Log.d(
                    "AddUserToRoom",
                    "User successfully added to the room's players collection with username: $username"
                )
            }
            .addOnFailureListener { e ->
                Log.e("AddUserToRoom", "Failed to add user to the room's players collection.", e)
            }
    }.addOnFailureListener { exception ->
        Log.d("Username", "get failed with ", exception)
    }
}

fun removeUserFromRoom(
    user: FirebaseUser,
    gameType: Boolean,
    roomType: String,
    onRemoved: () -> Unit
) {
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

fun listenToRoomPlayers(
    gameType: Boolean,
    roomType: String,
    onPlayersChanged: (List<User>) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val path = "game_rooms/$gameType/rooms/$roomType/players"
    val playersRef = db.collection(path)
    val players = mutableListOf<User>()


    playersRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
            Log.w("ListenToRoomPlayers", "Listen failed.", error)
            return@addSnapshotListener
        }

        snapshot?.documentChanges?.forEach { change ->
            // Firestore dokümanından User nesnesi oluştur
            val user = User(
                userId = change.document.id,
                username = change.document.getString("userName") ?: "Anonim",
                status = change.document.getString("status") ?: "aktif"
            )

            when (change.type) {
                DocumentChange.Type.ADDED -> {
                    players.add(user)
                }

                DocumentChange.Type.MODIFIED -> {
                    val index = players.indexOfFirst { it.userId == user.userId }
                    if (index != -1) {
                        // Eğer kullanıcı zaten listede varsa, bilgileri güncelle
                        players[index] = user
                    }
                }

                DocumentChange.Type.REMOVED -> {
                    // Kullanıcı listesinden kaldır
                    players.removeAll { it.userId == user.userId }
                }
            }
        }

        onPlayersChanged(players)
    }

}

fun updateUserStatus(
    gameType: Boolean,
    roomType: String,
    userId: String,
    status: String
) {
    val db = FirebaseFirestore.getInstance()
    val path = "game_rooms/$gameType/rooms/$roomType/players"
    val userRef = db.collection(path).document(userId)

    // Kullanıcının durumunu güncelle
    userRef.update("status", status)
        .addOnSuccessListener {
            Log.d("UpdateUserStatus", "User status updated to $status")
        }
        .addOnFailureListener { e ->
            Log.e("UpdateUserStatus", "Error updating user status", e)
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

fun findUsernameById(userId: String, db: FirebaseFirestore, onUsernameFound: (String?) -> Unit) {
    // `usernames` koleksiyonundan `userId`'ye göre sorgu yap
    val userRef = db.collection("usernames").document(userId)
    userRef.get().addOnSuccessListener { document ->
        if (document.exists()) {
            // Belge başarıyla bulunduysa, `username` alanını oku
            val username = document.getString("username")
            onUsernameFound(username)
        } else {
            // Kullanıcı bulunamadı
            Log.d("findUsernameById", "No user found with ID: $userId")
            onUsernameFound(null)
        }
    }.addOnFailureListener { exception ->
        // Sorgu sırasında bir hata oluştu
        Log.e("findUsernameById", "Error fetching user with ID: $userId", exception)
        onUsernameFound(null)
    }
}
fun getRandomTurkishLetterAndWordIndex(roomType: String): Pair<String, Int> {
    val turkishLetters = arrayOf("A", "B", "C", "Ç", "D", "E", "F", "G", "Ğ", "H", "I", "İ", "J", "K", "L", "M", "N", "O", "Ö", "P", "R", "S", "Ş", "T", "U", "Ü", "V", "Y", "Z")
    val randomNumber = Random.nextInt(turkishLetters.size)
    val selectedLetter = turkishLetters[randomNumber]
    val wordIndex = Random.nextInt(roomType.toInt() -1) +1 // Assuming the word index is a random number from 0 to 4
    return Pair(selectedLetter, wordIndex)

}
fun updateGamerInfoInRoom(gameType: Boolean, roomType: String, userId: String, isDuello: Boolean) {
    val db = FirebaseFirestore.getInstance()
    val gamerPath = "game_rooms/$gameType/rooms/$roomType/gamer$roomType/$userId"

    val updateMap = hashMapOf<String, Any>(
        "userId" to userId,
        "status" to "oyunda",
        "hasGuessingRight" to false,
        "saripuan" to 0,
        "kalansaniye" to 0,
        "yesilpuan" to 0,
        "word" to "",
        "word1" to "",
        "word2" to "",
        "word3" to "",
        "word4" to "",
        "word5" to "",
        "word6" to "",
        "word7" to ""

    )
    if (!isDuello) {
        updateMap["puan"] = 0
    }

    db.document(gamerPath).set(updateMap, SetOptions.merge())
        .addOnSuccessListener {
        }
        .addOnFailureListener { exception ->
        }
}



@Preview(showBackground = true)
@Composable
fun RoomScreenPreview() {
    // Mock NavController oluştur. Gerçek bir navigasyon işlemi yapmayacak.
    val navController = rememberNavController()

    // Önizleme için sahte gameType ve room seçimi sağla.
    val selectedGameType = true
    val selectedRoom = "4"

    RoomScreen(navController, selectedGameType, selectedRoom)
}