package com.ft.word_arena_game.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay


data class PlayerResult(
    val username: String?,
    val status: String?, // Oyuncunun tahminleri
    val puan: Int, // Tahmin için harcanan süre
    val puanduello: Int,
    val saripuan: Int, // Puan detayları
    val yesilpuan: Int, // Puan detayları
    val kalansaniye: Int, // Puan detayları
    val tahminsüresi: Int
)

@Composable
fun FinishScreen(
    navController: NavController,
    selectedGameType: Boolean,
    selectedRoom: String,
    rivalId: String, // Düello isteği için callback
    isDuello: Boolean
    ) {
    val user = Firebase.auth.currentUser
    val userId = user!!.uid
    val context = LocalContext.current
    var playerOneResult by remember { mutableStateOf<PlayerResult?>(null) }
    var playerTwoResult by remember { mutableStateOf<PlayerResult?>(null) }
    var requestId by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showDialog1 by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf<Long?>(null) }
    var remainingTime1 by remember { mutableStateOf<Long?>(null) }
    var currentRequest by remember { mutableStateOf<ChallengeRequest?>(null) }
    var autoNavigate by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(30000) // 30 saniye bekleyin
        if (autoNavigate) {
            // Oyuncu herhangi bir işlem yapmadı, anasayfaya veya kanala yönlendir
            navController.navigate("room/$selectedGameType/$selectedRoom")
        }
    }

    updateGameInfoInRoom(
        selectedGameType,
        selectedRoom,
        userId,
        hashMapOf(
            "ready" to false
        )
    )

    LaunchedEffect(userId) {
        findUsernameById(userId, FirebaseFirestore.getInstance()) { username ->            // Kullanıcı adı bulunduğunda, diğer verileri çek
            val propertyNames = listOf("status", "puan", "saripuan","puanduello", "yesilpuan", "kalansaniye", "tahminsüresi")
            fetchDocumentData(selectedGameType, selectedRoom, userId, propertyNames) { playerData ->
                playerOneResult = PlayerResult(                // Tüm veriler toplandığında, `PlayerResult` nesnesini oluştur
                    username = username ?: "Bilinmiyor",
                    status = playerData["status"] as? String ?: "",
                    puan = (playerData["puan"] as? Number)?.toInt() ?: 0,
                    puanduello = (playerData["puanduello"] as? Number)?.toInt() ?: 0,
                    saripuan = (playerData["saripuan"] as? Number)?.toInt() ?: 0,
                    yesilpuan = (playerData["yesilpuan"] as? Number)?.toInt() ?: 0,
                    kalansaniye = (playerData["kalansaniye"] as? Number)?.toInt() ?: 0,
                    tahminsüresi = (playerData["tahminsüresi"] as? Number)?.toInt() ?: 0
                )
                println("puan düello " + playerOneResult!!.puanduello)
            }
        }
    }

    LaunchedEffect(rivalId) {
        findUsernameById(rivalId, FirebaseFirestore.getInstance()) { username ->
            // Kullanıcı adı bulunduğunda, diğer verileri çek
            val propertyNames = listOf("status", "puan", "puanduello", "saripuan", "yesilpuan",  "kalansaniye", "tahminsüresi")
            fetchDocumentData(selectedGameType, selectedRoom, rivalId, propertyNames) { playerData ->
                // Tüm veriler toplandığında, `PlayerResult` nesnesini oluştur
                playerTwoResult = PlayerResult(
                    username = username ?: "Bilinmiyor",
                    status = playerData["status"] as? String ?: "",
                    puan = (playerData["puan"] as? Number)?.toInt() ?: 0,
                    puanduello = (playerData["puanduello"] as? Number)?.toInt() ?: 0,
                    saripuan = (playerData["saripuan"] as? Number)?.toInt() ?: 0,
                    yesilpuan = (playerData["yesilpuan"] as? Number)?.toInt() ?: 0,
                    kalansaniye = (playerData["kalansaniye"] as? Number)?.toInt() ?: 0,
                    tahminsüresi = (playerData["tahminsüresi"] as? Number)?.toInt() ?: 0
                )
            }
        }
    }

    val isLoading = remember {
        derivedStateOf { playerOneResult == null || playerTwoResult == null }
    }

    //gelen istek
    LaunchedEffect(Firebase.auth.currentUser?.uid) { //istekleri dinliyor
        Firebase.auth.currentUser?.uid?.let { userId ->
            listenForChallengeRequests(userId, FirebaseFirestore.getInstance()) { request ->
                currentRequest = request
                showDialog1 = true
                remainingTime1 = null
                autoNavigate = false
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
    //isteği dinliyor
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
                        autoNavigate = false
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
                        navController.navigate("room/$selectedGameType/$selectedRoom")
                    },
                    title = { Text("Düello İsteği") },
                    text = {
                        Text("$currentRequestUsername tarafından düello isteği geldi. \nCevap için kalan süre: $remainingTime1 saniye")
                    },
                    confirmButton = {
                        Button(onClick = {
                            // İsteği kabul et
                            showDialog1 = false
                            autoNavigate = false
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
                            user.let { it1 ->
                                val (randomLetter, wordIndex) = getRandomTurkishLetterAndWordIndex(selectedRoom)
                                updateGamerInfoInRoom(selectedGameType, selectedRoom, it1.uid, true)
                                navController.navigate("game/$selectedGameType/$selectedRoom/$randomLetter/$wordIndex/$rivalId/true")
                            }


                        }) {
                            Text("Kabul Et")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            // İsteği reddet
                            showDialog1 = false
                            autoNavigate = false
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
                            navController.navigate("room/$selectedGameType/$selectedRoom")
                        }) {
                            Text("Reddet")
                        }
                    }
                )
            } else if (remainingTime1!! <= 0L && showDialog1) {

                showDialog1 = false
                autoNavigate = false
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
                navController.navigate("room/$selectedGameType/$selectedRoom")
            }
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Oyun Sonuçları") },
                actions = {
                    // Düello butonu sağ üst köşede
                    Button(onClick = {
                        autoNavigate = false
                        if (Firebase.auth.currentUser != null) {
                            sendChallengeRequest1(//meydan okuma isteği gönder
                                fromUserId = Firebase.auth.currentUser!!.uid,
                                toUserId = rivalId,
                                roomType = selectedRoom,
                                gameType = selectedGameType,
                                onSuccess = {
                                        challengeRequestId ->
                                    // İstek başarıyla gönderildiğinde isteğe verilen yanıtı dinle
                                    requestId = "$challengeRequestId"
                                    Log.d(
                                        "ChallengeRequest",
                                        "Request sent successfully to ${playerTwoResult?.username}"
                                    )
                                    showDialog = true
                                    // Geri sayımı başlat
                                    startCountdown(
                                        FirebaseFirestore.getInstance(),
                                        rivalId,
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

                    }
                    ) {
                        Text("Düello")
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
                                    userId = rivalId,
                                    db = FirebaseFirestore.getInstance(),
                                    onStatusChanged = { status ->
                                        when (status) {
                                            "accepted" -> {
                                                Toast.makeText(
                                                    context,
                                                    "${playerTwoResult?.username} isteğinizi kabul etti.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                val (randomLetter, wordIndex) = getRandomTurkishLetterAndWordIndex(selectedRoom)
                                                updateGamerInfoInRoom(selectedGameType, selectedRoom, Firebase.auth.currentUser!!.uid, true )
                                                navController.navigate("game/$selectedGameType/$selectedRoom/$randomLetter/$wordIndex/$rivalId/true")
                                            }
                                            "rejected"
                                            -> {
                                                Toast.makeText(
                                                    context,
                                                    "${playerTwoResult?.username} isteğinizi reddetti. ",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                navController.navigate("room/$selectedGameType/$selectedRoom")
                                            }
                                        }
                                        autoNavigate = false
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
            )
        },
        floatingActionButton = {
            // Oyundan Çık FAB
            FloatingActionButton(onClick = {
                autoNavigate = false
                navController.navigate("room/$selectedGameType/$selectedRoom")
            }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Oyundan Çık")
            }
        }
    ){
            paddingValues ->
        Column(
            modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){


            var showDialog by remember { mutableStateOf(false) }
            if (showDialog) {
                val wordList = mutableListOf<String>()
                val propertyNames = listOf("word1", "word2", "word3","word4", "word5", "word6", "word7")
                fetchDocumentData(selectedGameType, selectedRoom, userId, propertyNames) { data ->
                    for ((index, propertyName) in propertyNames.withIndex()) {
                        val word = data[propertyName] as String? // Özelliği al
                        if (!word.isNullOrEmpty()) { // Dize boş veya null değilse listeye ekle
                            wordList.add(word)
                        }
                    }
                    println(wordList) // Liste yazdır
                }
               /* ShowFloatingDialog(
                    onDismiss = { showDialog = false },  // Dialog'u kapat
                    wordList = listOf("elma", "adam"), // Örnek kelime listesi
                    gridSize = 4,
                    arananKelime = "adam"// İstenen ızgara boyutuba
                )

                */
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth() // Row'u genişlet
                    .padding(8.dp), // Padding ekleyin
                horizontalArrangement = Arrangement.Start, // Sağa hizalama
                verticalAlignment = Alignment.CenterVertically // Dikeyde ortala
            ) {
                IconButton(
                    onClick = {
                        showDialog = true // Icon Button'a tıklanınca showDialog state'ini güncelle
                    },
                    modifier = Modifier.size(48.dp) // Icon Button boyutu
                ) {
                    androidx.compose.material.Icon(
                        imageVector = Icons.Filled.Face,
                        contentDescription = "Rakibi Gör" // Erişilebilirlik açıklaması
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            // Eğer oyuncu sonuçları yüklenmediyse yükleme işareti göster
            if (isLoading.value) {
                    CircularProgressIndicator(color = Color.Gray) // Yükleme işareti
            } else {
                // Oyuncu sonuçlarını göster
                playerOneResult?.let {
                    PlayerResultSection(it, isDuello)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                playerTwoResult?.let {
                    PlayerResultSection(it, isDuello)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }




}

@Composable
fun PlayerResultSection(playerResult: PlayerResult, isDuello: Boolean) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "${playerResult.status} oyuncu ''${playerResult.username}''", style = MaterialTheme.typography.titleLarge)
        Text(text = "Sonuçlar", style = MaterialTheme.typography.titleMedium)
        if(isDuello){
            if( playerResult.puanduello != 0){
                Text(text = "Sarı alan: ${playerResult.saripuan} puan", style = MaterialTheme.typography.bodySmall)
                Text(text = "Yeşil alan: ${playerResult.yesilpuan} puan", style = MaterialTheme.typography.bodySmall)
                Text(text = "Kalan süre: ${playerResult.kalansaniye} saniye", style = MaterialTheme.typography.bodySmall)
                Text(text = "Skor: ${playerResult.puanduello} puan", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Geçmiş Skor: ${playerResult.puan} puan", style = MaterialTheme.typography.bodyMedium)
            }else if(playerResult.puan != 0){
                Text(text = "Geçmiş Skor: ${playerResult.puan} puan", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Tahmin süresi: ${playerResult.tahminsüresi} saniye", style = MaterialTheme.typography.bodySmall)
            }else{
                Text(text = "Tahmin süresi: ${playerResult.tahminsüresi} saniye", style = MaterialTheme.typography.bodySmall)
            }

        }else{
            if(playerResult.puan != 0){
                Text(text = "Sarı alan: ${playerResult.saripuan} puan", style = MaterialTheme.typography.bodySmall)
                Text(text = "Yeşil alan: ${playerResult.yesilpuan} puan", style = MaterialTheme.typography.bodySmall)
                Text(text = "Kalan süre: ${playerResult.kalansaniye} saniye", style = MaterialTheme.typography.bodySmall)
                Text(text = "Skor: ${playerResult.puan} puan", style = MaterialTheme.typography.bodyMedium)
            }else {
                Text(text = "Tahmin süresi: ${playerResult.tahminsüresi} saniye", style = MaterialTheme.typography.bodySmall)
            }
        }
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
fun sendChallengeRequest1(
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


    }.addOnFailureListener { exception ->
        // Kullanıcının durumunu kontrol ederken hata oluşursa
        onFailure(Exception("Kullanıcı durumu kontrol edilirken bir hata oluştu: ${exception.message}"))
    }
}