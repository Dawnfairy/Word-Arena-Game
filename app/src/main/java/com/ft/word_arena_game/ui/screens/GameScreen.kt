package com.ft.word_arena_game.ui.screens


import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.ft.word_arena_game.ui.components.GameConnection
import com.ft.word_arena_game.ui.components.GameExit
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.Locale
import kotlin.random.Random

enum class PlayerStatus {
    UserWon,
    UserLost,
    Restart,
    Undefined
}

@Composable
fun GameScreen(
    navController: NavController,
    selectedGameType: Boolean,
    selectedRoom: String,
    randomLetter: String,
    wordIndex: Int,
    rivalId: String,
    isDuello: Boolean
) {

    val user = Firebase.auth.currentUser
    val context = LocalContext.current
    val connect = GameConnection(context = context)
//ss almaya bakkkk
    val dortHarfliKelimeler = listOf(
        "konu",
        "olay",
        "ülke",
        "adam",
        "para",
        "anne",
        "baba",
        "süre",
        "saat",
        "sıra",
        "kapı",
        "gece",
        "alan",
        "ürün",
        "aile",
        "halk",
        "akıl",
        "ayak",
        "araç",
        "hava",
        "sayı",
        "grup",
        "soru",
        "arka",
        "yazı",
        "okul",
        "renk",
        "yapı",
        "amaç",
        "film",
        "etki",
        "ışık",
        "oyun",
        "oran",
        "oğul",
        "lira",
        "ağız",
        "masa",
        "kafa",
        "spor",
        "ölüm",
        "isim",
        "kent",
        "önem",
        "koca",
        "ilgi",
        "koku",
        "ağaç",
        "bina",
        "kalp",
        "şart",
        "köşe",
        "bura",
        "ölçü",
        "dolu",
        "izin",
        "ateş",
        "fark",
        "tane",
        "ders",
        "defa",
        "batı",
        "dost",
        "plan",
        "karı",
        "cilt",
        "kaza",
        "adım",
        "ilaç",
        "yasa",
        "ceza",
        "ilke",
        "hoca",
        "adet",
        "faiz",
        "hata",
        "kere",
        "olma",
        "deri",
        "eşya",
        "aday",
        "öykü",
        "uçak",
        "doğa",
        "işçi",
        "kamu",
        "çaba",
        "risk",
        "cami",
        "sene",
        "otel",
        "zevk",
        "okur",
        "site",
        "abla",
        "kilo",
        "sırt",
        "paşa",
        "kriz"
    )
    val besHarfliKelimeler = listOf(
        "insan",
        "değil",
        "ZAMAN",
        "çocuk",
        "gider",
        "neden",
        "sebep",
        "kadın",
        "dünya",
        "durum",
        "şekil",
        "taraf",
        "hayat",
        "bilgi",
        "sonuç",
        "sorun",
        "sahip",
        "yüzde",
        "olays",
        "istem",
        "kitap",
        "bugün",
        "erkek",
        "çevre",
        "yaşam",
        "sokak",
        "tarih",
        "bölüm",
        "anlam",
        "banka",
        "madde",
        "karar",
        "biçim",
        "haber",
        "Allah",
        "parça",
        "değer",
        "gelir",
        "görev",
        "bölge",
        "deniz",
        "vücut",
        "temel",
        "resim",
        "hanım",
        "hatun",
        "nokta",
        "işlem",
        "orada",
        "araba",
        "duygu",
        "örnek",
        "duvar",
        "sanat",
        "takım",
        "müzik",
        "türlü",
        "kısım",
        "sabah",
        "ortam",
        "düzey",
        "aşağı",
        "cevap",
        "akşam",
        "hasta",
        "şehir",
        "hafta",
        "hesap",
        "fiyat",
        "satış",
        "içeri",
        "savaş",
        "kalan",
        "sayfa",
        "kurum",
        "kağıt",
        "cadde",
        "pazar",
        "sınıf",
        "güneş",
        "parti",
        "yatak",
        "yazar",
        "kulak",
        "sebep",
        "kural",
        "firma",
        "proje",
        "model",
        "balık",
        "görüş",
        "bahçe",
        "sevgi",
        "ekmek",
        "kurul",
        "köpek",
        "istek",
        "korku",
        "polis",
        "fikir",
        "koşul",
        "ortak",
        "hedef",
        "kenar",
        "beyin",
        "çizgi",
        "süreç",
        "bakış",
        "bilim",
        "ifade",
        "beden",
        "çözüm",
        "seçim",
        "zarar",
        "metre",
        "bitki",
        "kredi",
        "imkan",
        "kanal",
        "şarkı",
        "sahne",
        "aşama",
        "orman",
        "düzen",
        "hücre",
        "roman",
        "vergi",
        "basın",
        "sınır",
        "birey",
        "bebek",
        "bakan",
        "boyut",
        "dergi",
        "inanç",
        "üzeri",
        "giriş",
        "baskı",
        "tepki",
        "cümle",
        "Tanrı",
        "tavır",
        "yayın",
        "vakit",
        "daire",
        "katkı",
        "yanıt",
        "burun",
        "çıkar",
        "medya",
        "artış",
        "yürek",
        "belge",
        "etraf",
        "meyve",
        "bacak",
        "kanun",
        "müdür",
        "hukuk",
        "silah",
        "talep",
        "asker",
        "beyan",
        "besin",
        "çiçek",
        "saygı",
        "ücret",
        "gider",
        "örgüt",
        "boyun",
        "cihaz",
        "denge",
        "kahve",
        "öteki",
        "adres",
        "güven",
        "marka",
        "yarar",
        "gönül",
        "hayal",
        "şarap",
        "altın",
        "eylem",
        "kesim",
        "birim"
    )
    val altiHarfliKelimeler = listOf(
        "içinde",
        "devlet",
        "gerçek",
        "ilişki",
        "toplum",
        "şirket",
        "kaynak",
        "gazete",
        "doktor",
        "eğitim",
        "milyon",
        "kültür",
        "hizmet",
        "dikkat",
        "üretim",
        "dakika",
        "derece",
        "yöntem",
        "enerji",
        "sağlık",
        "teknik",
        "dışarı",
        "merkez",
        "toprak",
        "trafik",
        "mutfak",
        "varlık",
        "hayvan",
        "sektör",
        "yüzyıl",
        "sigara",
        "kelime",
        "başarı",
        "piyasa",
        "miktar",
        "meydan",
        "yardım",
        "kardeş",
        "meslek",
        "başkan",
        "numara",
        "sinema",
        "kavram",
        "mektup",
        "makine",
        "kalite",
        "eleman",
        "günlük",
        "koltuk",
        "hikaye",
        "ağabey",
        "destek",
        "otobüs",
        "sanayi",
        "millet",
        "reklam",
        "geçmiş",
        "toplam",
        "dükkan",
        "servis",
        "tedavi",
        "kimlik",
        "mesele",
        "sürücü",
        "milyar",
        "fırsat",
        "mağaza",
        "sözcük",
        "korgan",
        "rüzgar",
        "telefo",
        "yıldız",
        "bilinç",
        "mevcut",
        "meclis",
        "yaprak"
    )
    val yediHarfliKeliemeler = listOf(
        "arkadaş",
        "özellik",
        "program",
        "hareket",
        "çalışma",
        "müşteri",
        "telefon",
        "düşünce",
        "ihtiyaç",
        "gelecek",
        "öğrenci",
        "yönetim",
        "yabancı",
        "ekonomi",
        "hükümet",
        "malzeme",
        "pencere",
        "mahalle",
        "görüntü",
        "anlayış",
        "kontrol",
        "üstünde",
        "gelişme",
        "sanatçı",
        "içerisi",
        "nitelik",
        "problem",
        "ağırlık",
        "sıkıntı",
        "yatırım",
        "tehlike",
        "işletme",
        "sigorta",
        "değişim",
        "çerçeve",
        "yetenek",
        "ticaret",
        "vitamin",
        "yumurta"
    )
    val harfSayisi = selectedRoom.toInt()
    val harfSabitiVarMi = selectedGameType
    var kutuBoyutu = 70
    var kutuMesafesi = 16
    var yaziBoyutu = 32
    var showErrorDialog by remember { mutableStateOf(false) }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Kelime Bulunamadı") },
            text = { Text(text = "Girilen kelime sözlükte bulunamadı. Lütfen başka bir kelime deneyin.") },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("Tamam")
                }
            }
        )
    }


    val focusManager = LocalFocusManager.current
    var letters by remember { mutableStateOf(List(harfSayisi) { "" }) }
    val focusRequesters = List(harfSayisi) { FocusRequester() }
    var showGameScreen by remember { mutableStateOf(true) } // Oyun ekranını gösterme durumunu tutan değişken
    var showCircularProgress by remember { mutableStateOf(false) } // Oyun ekranını gösterme durumunu tutan değişken
    val textColorList = remember { mutableStateListOf<Color>() } // Harf renkleri için liste
    val coroutineScope = rememberCoroutineScope()
    var sureDurduMu by remember { mutableStateOf(false) }
    var playerStatus by remember { mutableStateOf("") }
    if (user != null) {

        LaunchedEffect(key1 = user.uid) {
            val userId = user.uid
            val db = FirebaseFirestore.getInstance()
            val playerPath =
                "game_rooms/$selectedGameType/rooms/$selectedRoom/gamer$selectedRoom/$userId"

            db.document(playerPath).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ListenForStatusChange", "Listening failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val status = snapshot.getString("status") ?: ""
                    coroutineScope.launch {
                        playerStatus = status
                    }
                }
            }
        }


        when (playerStatus) {
            "kazanan" -> {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text(text = "Tebrikler!") },
                    text = { Text(text = "Oyunu kazandınız.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                //deletePlayerFromGamerCollection(selectedGameType, selectedRoom, user.uid)
                                navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId/$isDuello")
                                //navController.popBackStack()
                            }
                        ) {
                            Text("Tamam")
                        }
                    }
                )
            }

            "kaybeden" -> {
                // Kaybeden için UI
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text(text = "Üzgünüz!") },
                    text = { Text(text = "Oyunu kaybettiniz.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                //deletePlayerFromGamerCollection(selectedGameType, selectedRoom, user.uid)
                                navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId/$isDuello")
                                //navController.popBackStack()
                            }
                        ) {
                            Text("Tamam")
                        }
                    }
                )
            }

            "berabere" -> {
                // Kaybeden için UI
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text(text = "Berabere!") },
                    text = { Text(text = "Oyun berabere bitti.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                //deletePlayerFromGamerCollection(selectedGameType, selectedRoom, user.uid)

                                navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId/$isDuello")
                                //navController.popBackStack()
                            }
                        ) {
                            Text("Tamam")
                        }
                    }
                )
            }
        }
    }
    if (textColorList.isEmpty()) {
        repeat(harfSayisi) {
            textColorList.add(Color.Black)
        }
    }

    if (harfSayisi == 5) {
        kutuBoyutu = 55
        kutuMesafesi = 12
        yaziBoyutu = 24

    } else if (harfSayisi == 6) {
        kutuBoyutu = 45
        kutuMesafesi = 8
        yaziBoyutu = 16
    } else if (harfSayisi == 7) {
        kutuBoyutu = 35
        kutuMesafesi = 4
        yaziBoyutu = 8
    }

    var timeLeft by remember { mutableStateOf(60) }
    if (connect) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
        ) {
            if (showGameScreen) {
                var isTimerRunning by remember { mutableStateOf(true) } // Timer'ın çalışıp çalışmadığını kontrol eden fla

                val progress = remember(timeLeft, 60) {
                    (timeLeft.toFloat() / 60.toFloat())
                }
                LaunchedEffect(key1 = timeLeft) {
                    if (timeLeft > 0) {
                        delay(1000) // 1 saniye bekleyin
                        if (!sureDurduMu) {
                            timeLeft--
                        }
                        // Zamanı azalt
                    } else {
                        isTimerRunning = false
                        println("buraya geldi 1")

                    }
                }



                if (isTimerRunning) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    )
                    {
                        Box(

                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)

                        ) {
                            CircularProgressIndicator(
                                progress = progress,
                                color = Color.Gray,
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(50.dp)
                            )
                            Text(
                                text = "$timeLeft",
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }


                } else {
                    val snackbarHostState = remember { SnackbarHostState() }
                    var gameStatus by remember { mutableStateOf(PlayerStatus.Undefined) }

                    listenForGameReadyStatus(selectedGameType, selectedRoom) { readyStatusList ->
                        val isMyUserReady =
                            readyStatusList.any { it.userId == user?.uid && it.isReady }
                        val isMyRivalReady =
                            readyStatusList.any { it.userId == rivalId && it.isReady }

                        gameStatus = when {
                            isMyUserReady && !isMyRivalReady -> PlayerStatus.UserWon
                            !isMyUserReady && isMyRivalReady -> PlayerStatus.UserLost
                            else -> PlayerStatus.Restart // Diğer durumlar için oyunun yeniden başlaması gerekebilir
                        }
                    }
                    LaunchedEffect(gameStatus) {
                        when (gameStatus) {
                            PlayerStatus.UserWon -> {
                                updateGamerWinnerInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    user?.uid ?: "",
                                    rivalId,
                                    "kazanan",
                                    "kaybeden"
                                ) {
                                    showCircularProgress = false
                                }
                                Toast.makeText(
                                    context,
                                    "Rakip oyuncu kelime girişi yapmadı. Oyunu kazandınız.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            PlayerStatus.UserLost -> {
                                updateGamerWinnerInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    user?.uid ?: "",
                                    rivalId,
                                    "kaybeden",
                                    "kazanan"
                                ) {
                                    showCircularProgress = false
                                }
                                Toast.makeText(
                                    context,
                                    "Kelime girişi yapmadınız. Oyunu kaybettiniz.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            PlayerStatus.Restart -> {
                                showCircularProgress = false
                                snackbarHostState.showSnackbar(
                                    "Kelime girişi yapılmadı oyun tekrar başlıyor.",
                                    duration = SnackbarDuration.Short
                                )
                                delay(2000) // 2 saniye bekleyin
                                navController.navigate("game/$selectedGameType/$selectedRoom/$randomLetter/$wordIndex/$rivalId")
                            }

                            else -> Unit
                        }
                    }
                    // Oyun yeniden başlatılırken Snackbar göster
                    if (gameStatus == PlayerStatus.Restart) {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                }

                Text(text = "Kullanıcı 1", modifier = Modifier.padding(bottom = 24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    letters.forEachIndexed { index, letter ->
                        val isCurrentIndexLocked =
                            harfSabitiVarMi && index == wordIndex // 3. indexi kilitlemek için koşul
                        LetterInput(
                            letter = letter,
                            onValueChange = { value ->
                                if (!isCurrentIndexLocked) { // Kilitli değilse güncelleme yap
                                    letters = letters.toMutableList().also {
                                        it[index] =
                                            value.uppercase(Locale.forLanguageTag("tr-TR"))
                                                .singleOrNull()?.toString() ?: ""
                                    }
                                }
                                // Focus değişikliklerini yönet
                                if (value.length == 1 && index == wordIndex - 1 && harfSabitiVarMi) { // Eğer kullanıcı 2. indexe (üçüncü kutuya) değer girerse
                                    if (wordIndex + 1 < harfSayisi) {
                                        focusRequesters[wordIndex + 1].requestFocus() // Doğrudan 4. indexe (beşinci kutuya) odaklan
                                    }
                                    letters = letters.toMutableList().also {
                                        it[index + 1] =
                                            randomLetter.singleOrNull()?.toString() ?: ""
                                    }
                                }
                                if (value.length == 1 && index < letters.lastIndex) {
                                    focusRequesters[index + 1].requestFocus()
                                } else if (value.isEmpty() && index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            },
                            focusRequester = focusRequesters[index],
                            kutuBoyutu = kutuBoyutu,
                            yaziBoyutu = yaziBoyutu,
                            isLocked = isCurrentIndexLocked,
                            lockedLetter = if (isCurrentIndexLocked) randomLetter else "",
                            textColorList = textColorList,
                            index = 0
                        )
                        if (index < letters.lastIndex) Spacer(Modifier.width(kutuMesafesi.dp))
                    }
                }
                if (showCircularProgress) {
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
                                    "Rakip oyuncu bekleniyor.",
                                    color = Color.White // Metin rengi
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        focusManager.clearFocus()

                        val enteredWord =
                            letters.joinToString(separator = "")  //  harf sabitde bulunan indexteki harfi buraya eklemen gerek enteredWord değişkenine
                        println(enteredWord)

                        if (harfSayisi == 4) {
                            if (!dortHarfliKelimeler.map { it.uppercase(Locale.forLanguageTag("tr-TR")) }
                                    .contains(enteredWord)) {
                                showErrorDialog = true

                            } else {
                                sureDurduMu = true
                                if (user != null) {
                                    updateGameInfoInRoom(
                                        selectedGameType,
                                        selectedRoom,
                                        user.uid,
                                        hashMapOf(
                                            "word" to enteredWord,
                                            "ready" to true,
                                            "hasGuessingRight" to true
                                        )
                                    )
                                    showCircularProgress = true
                                }
                                listenForGameStart(selectedGameType, selectedRoom, rivalId) {
                                    showGameScreen = false
                                    showCircularProgress = false

                                }
                            }

                        } else if (harfSayisi == 5) {
                            if (!besHarfliKelimeler.map { it.uppercase(Locale.forLanguageTag("tr-TR")) }
                                    .contains(enteredWord)) {
                                showErrorDialog = true

                            } else {
                                sureDurduMu = true
                                if (user != null) {
                                    updateGameInfoInRoom(
                                        selectedGameType,
                                        selectedRoom,
                                        user.uid,
                                        hashMapOf(
                                            "word" to enteredWord,
                                            "ready" to true,
                                            "hasGuessingRight" to true
                                        )
                                    )
                                    showCircularProgress = true
                                }
                                listenForGameStart(selectedGameType, selectedRoom, rivalId) {
                                    showGameScreen = false
                                    showCircularProgress = false

                                }
                            }

                        } else if (harfSayisi == 6) {
                            if (!altiHarfliKelimeler.map { it.uppercase(Locale.forLanguageTag("tr-TR")) }
                                    .contains(enteredWord)) {
                                showErrorDialog = true

                            } else {
                                sureDurduMu = true
                                if (user != null) {
                                    updateGameInfoInRoom(
                                        selectedGameType,
                                        selectedRoom,
                                        user.uid,
                                        hashMapOf(
                                            "word" to enteredWord,
                                            "ready" to true,
                                            "hasGuessingRight" to true
                                        )
                                    )
                                    showCircularProgress = true
                                }
                                listenForGameStart(selectedGameType, selectedRoom, rivalId) {
                                    showGameScreen = false
                                    showCircularProgress = false
                                }

                            }

                        } else if (harfSayisi == 7) {
                            if (!yediHarfliKeliemeler.map { it.uppercase(Locale.forLanguageTag("tr-TR")) }
                                    .contains(enteredWord)) {
                                showErrorDialog = true

                            } else {
                                sureDurduMu = true
                                if (user != null) {
                                    updateGameInfoInRoom(
                                        selectedGameType,
                                        selectedRoom,
                                        user.uid,
                                        hashMapOf(
                                            "word" to enteredWord,
                                            "ready" to true,
                                            "hasGuessingRight" to true
                                        )
                                    )
                                    showCircularProgress = true
                                }
                                listenForGameStart(selectedGameType, selectedRoom, rivalId) {
                                    showGameScreen = false
                                    showCircularProgress = false

                                }
                            }

                        }


                    }, enabled = isTimerRunning,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))

                ) {
                    Text(text = "ONAYLA", color = Color.White)
                }
            }

            if (!showGameScreen) {
                var bulunacakKelime by remember { mutableStateOf("") }
                if (user != null) {
                    listenForOtherGamerWord(
                        selectedGameType,
                        selectedRoom,
                        rivalId
                    ) { otherGamerWord ->
                        bulunacakKelime = otherGamerWord
                        Log.d("kelimeeeeee", bulunacakKelime)
                    }
                    LetterGrid(
                        harfSayisi,
                        kutuBoyutu,
                        kutuMesafesi,
                        yaziBoyutu,
                        dortHarfliKelimeler,
                        besHarfliKelimeler,
                        altiHarfliKelimeler,
                        yediHarfliKeliemeler,
                        bulunacakKelime,
                        selectedGameType,
                        selectedRoom,
                        user.uid,
                        rivalId,
                        navController,
                        timeLeft,
                        isDuello
                    )
                }
            }

        }

        if (user != null) {
            GameExit(selectedGameType, selectedRoom, user.uid, rivalId)
        }
    } else {
        var isTimerRunning by remember { mutableStateOf(true) } // Timer'ın çalışıp çalışmadığını kontrol eden fla

        var timeLeft by remember { mutableStateOf(10) }

        val progress = remember(timeLeft, 10) {
            (timeLeft.toFloat() / 10.toFloat())
        }

        LaunchedEffect(key1 = timeLeft) {
            if (timeLeft > 0) {
                delay(1000) // 1 saniye bekleyin
                timeLeft-- // Zamanı azalt
            } else {
                isTimerRunning = false

            }
        }



        if (isTimerRunning) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            )
            {
                Box(

                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)

                ) {
                    CircularProgressIndicator(
                        progress = progress,
                        color = Color.Gray,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "$timeLeft",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }


        } else {
            var showErrorDialog by remember { mutableStateOf(false) }
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text(text = "Bağlantı Hatası") },
                    text = { Text(text = "Oyunu Kaybettin") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showErrorDialog = false
                                if (user != null) {
                                    updateGamerWinnerInRoom(
                                        selectedGameType,
                                        selectedRoom,
                                        user.uid,
                                        rivalId,
                                        "kaybeden",
                                        "kazanan"
                                    ) {
                                        //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")
                                    }
                                }

                            }
                        ) {
                            Text("Tamam")
                        }
                    }
                )
            }
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun LetterGrid(
    harfSayisi: Int,
    kutuBoyutu: Int,
    kutuMesafesi: Int,
    YaziBoyutu: Int,
    dortHarfliKelimeler: List<String>,
    besHarfliKelimeler: List<String>,
    altiHarfliKelimeler: List<String>,
    yediHarfliKelimeler: List<String>,
    bulunacakKelime: String,
    selectedGameType: Boolean,
    selectedRoom: String,
    userId: String,
    rivalId: String,
    navController: NavController,
    timeLeftKopya: Int,
    isDuello: Boolean
) {


    var puan = 0
    var timeCount by remember { mutableStateOf(0) }
    var yesilPuan = 0
    var sariPuan = 0
    var rakipOyuncuKelimeyiBulamadiMi by remember { mutableStateOf(false) }//burayı true ya çek
    var zamanindaTahminYapilmadiMi by remember { mutableStateOf(false) }
    var rivalScore by remember { mutableStateOf(0) } //

    listenForOtherGamerGuessingRightLoss(
        selectedGameType,
        selectedRoom,
        userId
    ) {
        rakipOyuncuKelimeyiBulamadiMi = true
    }

    var showErrorDialog by remember { mutableStateOf(false) }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Kelime Bulunamadı") },
            text = { Text(text = "Girilen kelime sözlükte bulunamadı. Lütfen başka bir kelime deneyin.") },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("Tamam")
                }
            }
        )
    }

    var showErrorDialog2 by remember { mutableStateOf(false) }
    if (showErrorDialog2) {
        AlertDialog(
            onDismissRequest = { showErrorDialog2 = false },
            title = { Text(text = "Tebrikler!!!") },
            text = { Text(text = "Doğru kelime bulundu") },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorDialog2 = false
                        updateGameInfoInRoom(
                            selectedGameType,
                            selectedRoom,
                            userId,
                            hashMapOf(
                                "tahminsüresi" to timeCount
                            )
                        )
                        updateGamerWinnerInRoom(
                            selectedGameType,
                            selectedRoom,
                            userId,
                            rivalId,
                            "kazanan",
                            "kaybeden"
                        ) {
                            //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                        }

                    }
                ) {
                    Text("Tamam")
                }
            }
        )
    }


    var showErrorDialog3 by remember { mutableStateOf(false) }
    if (showErrorDialog3) {
        AlertDialog(
            onDismissRequest = { showErrorDialog3 = false },
            title = { Text(text = "Oyunu kaybettiniz !!!") },
            text = { Text(text = "Uzun süre tahmin yapmadığınız için oyunu kaybettiniz") },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorDialog3 = false
                        updateGamerWinnerInRoom(
                            selectedGameType,
                            selectedRoom,
                            userId,
                            rivalId,
                            "kaybeden",
                            "kazanan"
                        ) {
                            //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                        }

                    }
                ) {
                    Text("Tamam")
                }
            }
        )
    }

    var isTimerRunning by remember { mutableStateOf(true) } // Timer'ın çalışıp çalışmadığını kontrol eden fla

    var timeLeft by remember { mutableStateOf(60) }


    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000) // 1 saniye bekleyin
            timeLeft-- // Zamanı azalt
        } else {
            isTimerRunning = false

        }
    }

    if (rakipOyuncuKelimeyiBulamadiMi) {
        timeLeft = 0
        isTimerRunning = false
    }
    var isTimerRunning1 by remember { mutableStateOf(true) } // Timer'ın çalışıp çalışmadığını kontrol eden fla

    var timeLeft1 by remember { mutableStateOf(10) }
    val progress = remember(timeLeft1, 10) {
        (timeLeft1.toFloat() / 10.toFloat())
    }



    if (!isTimerRunning) {

        LaunchedEffect(key1 = timeLeft1) {
            if (timeLeft1 > 0) {
                delay(1000) // 1 saniye bekleyin
                timeLeft1-- // Zamanı azalt
            } else {
                isTimerRunning1 = false

            }
        }


        if (isTimerRunning1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            )
            {
                Box(

                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)

                ) {
                    CircularProgressIndicator(
                        progress = progress,
                        color = Color.Gray,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "$timeLeft1",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }


        } else {
            if (rakipOyuncuKelimeyiBulamadiMi) {
                isTimerRunning = false

                zamanindaTahminYapilmadiMi = true

            } else {
                showErrorDialog3 = true

            }

            isTimerRunning1 = true

        }


    }


    var enteredWord1: String
    var enteredWord2: String
    var enteredWord3: String
    var enteredWord4: String
    var enteredWord5: String
    var enteredWord6: String
    var enteredWord7: String
    val totalSize = harfSayisi * harfSayisi // Toplam hücre sayısı
    var currentRow by remember { mutableStateOf(0) } // Şu anki satır
    var letters by remember { mutableStateOf(List(totalSize) { "" }) } // Tüm hücreler için harfler
    val focusRequesters = remember { List(totalSize) { FocusRequester() } }
    val textColorList = remember { mutableStateListOf<Color>() } // Harf renkleri için liste

    if (textColorList.isEmpty()) {
        repeat(harfSayisi * harfSayisi) {
            textColorList.add(Color.Black)
        }
    }


    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.padding(16.dp)) {


        var showDialog by remember { mutableStateOf(false) }
        if (showDialog) {
            val wordList = mutableListOf<String>()
            val propertyNames =
                listOf("word1", "word2", "word3", "word4", "word5", "word6", "word7")
            fetchDocumentData(selectedGameType, selectedRoom, userId, propertyNames) { data ->
                for ((index, propertyName) in propertyNames.withIndex()) {
                    val word = data[propertyName] as String? // Özelliği al
                    if (!word.isNullOrEmpty()) { // Dize boş veya null değilse listeye ekle
                        wordList.add(word)
                    }
                }
                println(wordList) // Liste yazdır
            }
            /*ShowFloatingDialog(
                onDismiss = { showDialog = false },  // Dialog'u kapat
                wordList = wordList, // Örnek kelime listesi
                gridSize = 4,
                arananKelime = bulunacakKelime// İstenen ızgara boyutuba
            )

             */
        }

        Row(
            modifier = Modifier
                .fillMaxWidth() // Row'u genişlet
                .padding(8.dp), // Padding ekleyin
            horizontalArrangement = Arrangement.End, // Sağa hizalama
            verticalAlignment = Alignment.CenterVertically // Dikeyde ortala
        ) {
            IconButton(
                onClick = {
                    showDialog = true // Icon Button'a tıklanınca showDialog state'ini güncelle
                },
                modifier = Modifier.size(48.dp) // Icon Button boyutu
            ) {
                Icon(
                    imageVector = Icons.Filled.Face,
                    contentDescription = "Rakibi Gör" // Erişilebilirlik açıklaması
                )
            }
        }



        for (i in 0 until totalSize step harfSayisi) {
            Row(horizontalArrangement = Arrangement.spacedBy((kutuMesafesi - 2).dp)) {
                for (j in i until i + harfSayisi) {
                    if (zamanindaTahminYapilmadiMi) {


                        var rastgeleKelime = ""
                        if (harfSayisi == 4) {
                            val randomInRange = Random.nextInt(0, 20)
                            rastgeleKelime = dortHarfliKelimeler[randomInRange]

                        }
                        if (harfSayisi == 5) {
                            val randomInRange = Random.nextInt(0, 20)
                            rastgeleKelime = besHarfliKelimeler[randomInRange]

                        }
                        if (harfSayisi == 6) {
                            val randomInRange = Random.nextInt(0, 20)
                            rastgeleKelime = altiHarfliKelimeler[randomInRange]

                        }
                        if (harfSayisi == 7) {
                            val randomInRange = Random.nextInt(0, 20)
                            rastgeleKelime = yediHarfliKelimeler[randomInRange]

                        }

                        focusRequesters[harfSayisi * currentRow].requestFocus() // Doğrudan 4. indexe (beşinci kutuya) odaklan
                        letters = letters.toMutableList().also {
                            it[0 + harfSayisi * currentRow] = rastgeleKelime[0].toString()
                                .uppercase(Locale.forLanguageTag("tr-TR")).singleOrNull()
                                ?.toString() ?: ""
                            it[1 + harfSayisi * currentRow] = rastgeleKelime[1].toString()
                                .uppercase(Locale.forLanguageTag("tr-TR")).singleOrNull()
                                ?.toString() ?: ""
                            it[2 + harfSayisi * currentRow] = rastgeleKelime[2].toString()
                                .uppercase(Locale.forLanguageTag("tr-TR")).singleOrNull()
                                ?.toString() ?: ""
                            it[3 + harfSayisi * currentRow] = rastgeleKelime[3].toString()
                                .uppercase(Locale.forLanguageTag("tr-TR")).singleOrNull()
                                ?.toString() ?: ""
                            if (harfSayisi >= 5) {
                                it[4 + harfSayisi * currentRow] = rastgeleKelime[4].toString()
                                    .uppercase(Locale.forLanguageTag("tr-TR")).singleOrNull()
                                    ?.toString() ?: ""
                            }
                            if (harfSayisi >= 6) {
                                it[5 + harfSayisi * currentRow] = rastgeleKelime[5].toString()
                                    .uppercase(Locale.forLanguageTag("tr-TR")).singleOrNull()
                                    ?.toString() ?: ""
                            }
                            if (harfSayisi >= 7) {
                                it[6 + harfSayisi * currentRow] = rastgeleKelime[6].toString()
                                    .uppercase(Locale.forLanguageTag("tr-TR")).singleOrNull()
                                    ?.toString() ?: ""
                            }



                            puan = 0
                            yesilPuan = 0
                            sariPuan = 0
                            for (i in 0 until harfSayisi) {

                                println(rastgeleKelime[i] + " " + bulunacakKelime[i])
                                if (rastgeleKelime[i].uppercaseChar() == bulunacakKelime[i]) {
                                    textColorList[i + harfSayisi * currentRow] =
                                        Color(android.graphics.Color.parseColor("#0AA351"))
                                    puan += 10
                                    yesilPuan += 10
                                } else {
                                    for (j in 0 until harfSayisi) {
                                        println(rastgeleKelime[j] + " " + bulunacakKelime[j])

                                        if (rastgeleKelime[i].uppercaseChar() == bulunacakKelime[j] && i != j) {
                                            textColorList[i + harfSayisi * currentRow] =
                                                Color(android.graphics.Color.parseColor("#F2F90C"))
                                            puan += 5
                                            sariPuan += 5
                                        }

                                    }
                                }

                            }

                            if (bulunacakKelime.equals(rastgeleKelime)) {
                                showErrorDialog2 = true

                            } else if (currentRow + 1 == harfSayisi) {

                                puan += timeLeftKopya
                                println("puannnn : $puan ")
                                updateGameInfoInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    userId,
                                    hashMapOf(
                                        "hasGuessingRight" to false,
                                        if (isDuello) {
                                            "puanduello" to puan
                                        } else {
                                            "puan" to puan
                                        },
                                        "saripuan" to sariPuan,
                                        "yesilpuan" to yesilPuan,
                                        "kalansaniye" to timeLeftKopya,
                                        "tahminsüresi" to timeCount
                                    )
                                )
                            }
                            if (currentRow + 1 == harfSayisi && rakipOyuncuKelimeyiBulamadiMi) {

                                getRivalScoreOnce(
                                    selectedGameType,
                                    selectedRoom,
                                    rivalId
                                ) { score ->
                                    rivalScore = score
                                    println("rakip puannnn : $rivalScore ")

                                    when {
                                        puan > rivalScore -> updateGamerWinnerInRoom(
                                            selectedGameType,
                                            selectedRoom,
                                            userId,
                                            rivalId,
                                            "kazanan",
                                            "kaybeden"
                                        ) {
                                            //navController.popBackStack()
                                        }

                                        puan < rivalScore -> updateGamerWinnerInRoom(
                                            selectedGameType,
                                            selectedRoom,
                                            userId,
                                            rivalId,
                                            "kaybeden",
                                            "kazanan"
                                        ) {
                                            //navController.popBackStack()
                                        }

                                        else -> if (puan != 0) {
                                            updateGamerWinnerInRoom(
                                                selectedGameType,
                                                selectedRoom,
                                                userId,
                                                rivalId,
                                                "berabere",
                                                "berabere"
                                            ) {
                                                //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")
                                            }
                                        }
                                    }
                                }
                            }

                            zamanindaTahminYapilmadiMi = false
                            // zamanı tekrardan sıfırla 10 saniyer kelime boyutunaa göre indexleri ayarla rastgele kelime seç harfleri ona göre ayarla
                            timeLeft1 = 10
                            isTimerRunning1 = true
                            currentRow++

                        }


                    }

                    LetterInput(

                        letter = letters[j],


                        onValueChange = { value ->
                            if (value.length <= 1) {
                                letters = letters.toMutableList().also {
                                    it[j] = value.uppercase(Locale.forLanguageTag("tr-TR"))
                                        .singleOrNull()?.toString() ?: ""
                                }

                                if (value.length == 1 && j < (i + harfSayisi - 1)) {
                                    focusRequesters[j + 1].requestFocus()
                                }

                                if (value.isEmpty() && j > 0) {
                                    focusRequesters[j - 1].requestFocus()
                                }

                            }
                        },

                        focusRequester = focusRequesters[j],
                        kutuBoyutu,
                        kutuMesafesi,
                        textColorList = textColorList,
                        index = j
                    )


                }
            }

            Spacer(modifier = Modifier.padding(8.dp))
        }



        Button(
            onClick = {


                if (rakipOyuncuKelimeyiBulamadiMi) {

                    timeCount += (10 - timeLeft1)
                    timeLeft = 0
                    isTimerRunning = false
                } else {

                    timeCount += (70 - timeLeft - timeLeft1)
                    timeLeft = 60
                    isTimerRunning = true
                }
                println(timeCount)
                timeLeft1 = 10
                isTimerRunning1 = true
                if (letters.slice(currentRow * harfSayisi until (currentRow + 1) * harfSayisi)
                        .all { it.isNotEmpty() }
                ) {

                    if (currentRow <= harfSayisi - 1) {


                        if (currentRow == 0) {
                            enteredWord1 = letters.joinToString(separator = "")
                            enteredWord1 = enteredWord1.takeLast(harfSayisi)
                            println(enteredWord1)
                            var sonuc = KelimeVarmi(
                                kelime = enteredWord1,
                                dortHarfliKelimeler = dortHarfliKelimeler,
                                besHarfliKelimeler = besHarfliKelimeler,
                                altiHarfliKelimeler = altiHarfliKelimeler,
                                yediHarfliKelimeler = yediHarfliKelimeler,
                                harfSayisi = harfSayisi
                            )

                            if (sonuc) {
                                updateGameInfoInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    userId,
                                    hashMapOf(
                                        "word1" to enteredWord1
                                    )
                                )
                                for (i in 0 until harfSayisi) {

                                    if (enteredWord1[i] == bulunacakKelime[i]) {
                                        textColorList[i] =
                                            Color(android.graphics.Color.parseColor("#0AA351"))
                                    } else {
                                        for (j in 0 until harfSayisi) {
                                            if (enteredWord1[i] == bulunacakKelime[j] && i != j) {
                                                textColorList[i] =
                                                    Color(android.graphics.Color.parseColor("#F2F90C"))
                                            }

                                        }
                                    }

                                }

                                if (bulunacakKelime == enteredWord1) {
                                    showErrorDialog2 = true

                                }


                                currentRow++
                                focusRequesters[currentRow * harfSayisi].requestFocus()
                            } else {
                                showErrorDialog = true
                            }

                        } else if (currentRow == 1) {
                            enteredWord2 = letters.joinToString(separator = "")
                            enteredWord2 = enteredWord2.takeLast(harfSayisi)
                            println(enteredWord2)
                            var sonuc = KelimeVarmi(
                                kelime = enteredWord2,
                                dortHarfliKelimeler = dortHarfliKelimeler,
                                besHarfliKelimeler = besHarfliKelimeler,
                                altiHarfliKelimeler = altiHarfliKelimeler,
                                yediHarfliKelimeler = yediHarfliKelimeler,
                                harfSayisi = harfSayisi
                            )

                            if (sonuc) {
                                updateGameInfoInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    userId,
                                    hashMapOf(
                                        "word2" to enteredWord2
                                    )
                                )
                                for (i in 0 until harfSayisi) {

                                    if (enteredWord2[i] == bulunacakKelime[i]) {
                                        textColorList[i + harfSayisi * currentRow] =
                                            Color(android.graphics.Color.parseColor("#0AA351"))
                                    } else {
                                        for (j in 0 until harfSayisi) {
                                            if (enteredWord2[i] == bulunacakKelime[j] && i != j) {
                                                textColorList[i + harfSayisi * currentRow] =
                                                    Color(android.graphics.Color.parseColor("#F2F90C"))
                                            }

                                        }
                                    }


                                }

                                if (bulunacakKelime == enteredWord2) {
                                    showErrorDialog2 = true
                                }
                                currentRow++
                                focusRequesters[currentRow * harfSayisi].requestFocus()
                            } else {
                                showErrorDialog = true
                            }


                        } else if (currentRow == 2) {
                            enteredWord3 = letters.joinToString(separator = "")
                            enteredWord3 = enteredWord3.takeLast(harfSayisi)
                            println(enteredWord3)

                            var sonuc = KelimeVarmi(
                                kelime = enteredWord3,
                                dortHarfliKelimeler = dortHarfliKelimeler,
                                besHarfliKelimeler = besHarfliKelimeler,
                                altiHarfliKelimeler = altiHarfliKelimeler,
                                yediHarfliKelimeler = yediHarfliKelimeler,
                                harfSayisi = harfSayisi
                            )

                            if (sonuc) {
                                updateGameInfoInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    userId,
                                    hashMapOf(
                                        "word3" to enteredWord3
                                    )
                                )
                                for (i in 0 until harfSayisi) {

                                    if (enteredWord3[i] == bulunacakKelime[i]) {
                                        textColorList[i + harfSayisi * currentRow] =
                                            Color(android.graphics.Color.parseColor("#0AA351"))
                                    } else {
                                        for (j in 0 until harfSayisi) {
                                            if (enteredWord3[i] == bulunacakKelime[j] && i != j) {
                                                textColorList[i + harfSayisi * currentRow] =
                                                    Color(android.graphics.Color.parseColor("#F2F90C"))
                                            }

                                        }
                                    }


                                }



                                if (bulunacakKelime == enteredWord3) {
                                    showErrorDialog2 = true

                                }
                                currentRow++
                                focusRequesters[currentRow * harfSayisi].requestFocus()
                            } else {
                                showErrorDialog = true
                            }


                        } else if (currentRow == 3) {
                            puan = 0
                            sariPuan = 0
                            yesilPuan = 0
                            enteredWord4 = letters.joinToString(separator = "")
                            enteredWord4 = enteredWord4.takeLast(harfSayisi)
                            println(enteredWord4)

                            var sonuc = KelimeVarmi(
                                kelime = enteredWord4,
                                dortHarfliKelimeler = dortHarfliKelimeler,
                                besHarfliKelimeler = besHarfliKelimeler,
                                altiHarfliKelimeler = altiHarfliKelimeler,
                                yediHarfliKelimeler = yediHarfliKelimeler,
                                harfSayisi = harfSayisi
                            )

                            if (sonuc) {
                                updateGameInfoInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    userId,
                                    hashMapOf(
                                        "word4" to enteredWord4
                                    )
                                )
                                for (i in 0 until harfSayisi) {

                                    if (enteredWord4[i] == bulunacakKelime[i]) {
                                        textColorList[i + harfSayisi * currentRow] =
                                            Color(android.graphics.Color.parseColor("#0AA351"))
                                        puan += 10
                                        yesilPuan += 10
                                    } else {
                                        for (j in 0 until harfSayisi) {
                                            if (enteredWord4[i] == bulunacakKelime[j] && i != j) {
                                                puan += 5
                                                sariPuan += 5

                                                textColorList[i + harfSayisi * currentRow] =
                                                    Color(android.graphics.Color.parseColor("#F2F90C"))
                                            }

                                        }
                                    }


                                }


                                if (bulunacakKelime == enteredWord4) {
                                    showErrorDialog2 = true
                                    // bu kısımlarda kelimeyi bulan kullanıcı varsa veritabanından güncelle
                                } else if (currentRow + 1 == harfSayisi) {
//false tahmin hakkı bitti
                                    println("ssssaaaaa" + timeCount)
                                    puan += timeLeftKopya
                                    println("puannnn : $puan ")
                                    updateGameInfoInRoom(
                                        selectedGameType,
                                        selectedRoom,
                                        userId,
                                        hashMapOf(
                                            "hasGuessingRight" to false,
                                            if (isDuello) {
                                                "puanduello" to puan
                                            } else {
                                                "puan" to puan
                                            },
                                            "saripuan" to sariPuan,
                                            "yesilpuan" to yesilPuan,
                                            "kalansaniye" to timeLeftKopya,
                                            "tahminsüresi" to timeCount
                                        )
                                    )
                                }
                                if (currentRow + 1 == harfSayisi && rakipOyuncuKelimeyiBulamadiMi) {

                                    getRivalScoreOnce(
                                        selectedGameType,
                                        selectedRoom,
                                        rivalId
                                    ) { score ->
                                        rivalScore = score
                                        println("rakip puannnn : $rivalScore ")

                                        when {
                                            puan > rivalScore -> updateGamerWinnerInRoom(
                                                selectedGameType,
                                                selectedRoom,
                                                userId,
                                                rivalId,
                                                "kazanan",
                                                "kaybeden"
                                            ) {
                                                //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                                            }

                                            puan < rivalScore -> updateGamerWinnerInRoom(
                                                selectedGameType,
                                                selectedRoom,
                                                userId,
                                                rivalId,
                                                "kaybeden",
                                                "kazanan"
                                            ) {
                                                //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                                            }

                                            else -> if (puan != 0) {
                                                updateGamerWinnerInRoom(
                                                    selectedGameType,
                                                    selectedRoom,
                                                    userId,
                                                    rivalId,
                                                    "berabere",
                                                    "berabere"
                                                ) {
                                                    //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")
                                                }
                                            }
                                        }
                                    }
                                }

                                currentRow++
                                if (harfSayisi > currentRow)
                                    focusRequesters[currentRow * harfSayisi].requestFocus()
                            } else {
                                showErrorDialog = true
                            }


                        } else if (currentRow == 4 && harfSayisi >= 5) {

                            puan = 0
                            yesilPuan = 0
                            sariPuan = 0

                            enteredWord5 = letters.joinToString(separator = "")
                            enteredWord5 = enteredWord5.takeLast(harfSayisi)
                            println(enteredWord5 + "aaaaaa")


                            var sonuc = KelimeVarmi(
                                kelime = enteredWord5,
                                dortHarfliKelimeler = dortHarfliKelimeler,
                                besHarfliKelimeler = besHarfliKelimeler,
                                altiHarfliKelimeler = altiHarfliKelimeler,
                                yediHarfliKelimeler = yediHarfliKelimeler,
                                harfSayisi = harfSayisi
                            )

                            if (sonuc) {
                                updateGameInfoInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    userId,
                                    hashMapOf(
                                        "word5" to enteredWord5
                                    )
                                )
                                for (i in 0 until harfSayisi) {

                                    if (enteredWord5[i] == bulunacakKelime[i]) {
                                        textColorList[i + harfSayisi * currentRow] =
                                            Color(android.graphics.Color.parseColor("#0AA351"))
                                        puan += 10
                                        yesilPuan += 10
                                    } else {
                                        for (j in 0 until harfSayisi) {
                                            if (enteredWord5[i] == bulunacakKelime[j] && i != j) {
                                                textColorList[i + harfSayisi * currentRow] =
                                                    Color(android.graphics.Color.parseColor("#F2F90C"))
                                                puan += 5
                                                sariPuan += 5
                                            }

                                        }
                                    }


                                }



                                if (bulunacakKelime == enteredWord5) {
                                    showErrorDialog2 = true
                                    println(enteredWord5)
                                } else if (currentRow + 1 == harfSayisi) {
//false tahmin hakkı bitti
                                    puan += timeLeftKopya
                                    println("puannnn : $puan ")
                                    updateGameInfoInRoom(
                                        selectedGameType,
                                        selectedRoom,
                                        userId,
                                        hashMapOf(
                                            "hasGuessingRight" to false,
                                            if (isDuello) {
                                                "puanduello" to puan
                                            } else {
                                                "puan" to puan
                                            },
                                            "saripuan" to sariPuan,
                                            "yesilpuan" to yesilPuan,
                                            "kalansaniye" to timeLeftKopya,
                                            "tahminsüresi" to timeCount
                                        )
                                    )
                                }
                                if (currentRow + 1 == harfSayisi && rakipOyuncuKelimeyiBulamadiMi) {

                                    getRivalScoreOnce(
                                        selectedGameType,
                                        selectedRoom,
                                        rivalId
                                    ) { score ->
                                        rivalScore = score
                                        println("rakip puannnn : $rivalScore ")

                                        when {
                                            puan > rivalScore -> updateGamerWinnerInRoom(
                                                selectedGameType,
                                                selectedRoom,
                                                userId,
                                                rivalId,
                                                "kazanan",
                                                "kaybeden"
                                            ) {
                                                //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                                            }

                                            puan < rivalScore -> updateGamerWinnerInRoom(
                                                selectedGameType,
                                                selectedRoom,
                                                userId,
                                                rivalId,
                                                "kaybeden",
                                                "kazanan"
                                            ) {
                                                //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                                            }

                                            else -> if (puan != 0) {
                                                updateGamerWinnerInRoom(
                                                    selectedGameType,
                                                    selectedRoom,
                                                    userId,
                                                    rivalId,
                                                    "berabere",
                                                    "berabere"
                                                ) {
                                                    //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")
                                                }
                                            }
                                        }
                                    }
                                }

                                currentRow++
                                if (harfSayisi > currentRow)
                                    focusRequesters[currentRow * harfSayisi].requestFocus()
                            } else {
                                showErrorDialog = true
                            }


                        } else if (currentRow == 5 && harfSayisi >= 6) {
                            puan = 0
                            yesilPuan = 0
                            sariPuan = 0
                            enteredWord6 = letters.joinToString(separator = "")
                            enteredWord6 = enteredWord6.takeLast(harfSayisi)
                            println(enteredWord6)


                            var sonuc = KelimeVarmi(
                                kelime = enteredWord6,
                                dortHarfliKelimeler = dortHarfliKelimeler,
                                besHarfliKelimeler = besHarfliKelimeler,
                                altiHarfliKelimeler = altiHarfliKelimeler,
                                yediHarfliKelimeler = yediHarfliKelimeler,
                                harfSayisi = harfSayisi
                            )

                            if (sonuc) {

                                updateGameInfoInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    userId,
                                    hashMapOf(
                                        "word6" to enteredWord6
                                    )
                                )
                                for (i in 0 until harfSayisi) {

                                    if (enteredWord6[i] == bulunacakKelime[i]) {
                                        textColorList[i + harfSayisi * currentRow] =
                                            Color(android.graphics.Color.parseColor("#0AA351"))
                                        puan += 10
                                        yesilPuan += 10
                                    } else {
                                        for (j in 0 until harfSayisi) {
                                            if (enteredWord6[i] == bulunacakKelime[j] && i != j) {
                                                textColorList[i + harfSayisi * currentRow] =
                                                    Color(android.graphics.Color.parseColor("#F2F90C"))
                                                puan += 5
                                                sariPuan += 5
                                            }

                                        }
                                    }


                                }



                                if (bulunacakKelime == enteredWord6) {
                                    showErrorDialog2 = true
                                } else if (currentRow + 1 == harfSayisi) {
//false tahmin hakkı bitti
                                    puan += timeLeftKopya
                                    println("puannnn : $puan ")
                                    updateGameInfoInRoom(
                                        selectedGameType,
                                        selectedRoom,
                                        userId,
                                        hashMapOf(
                                            "hasGuessingRight" to false,
                                            if (isDuello) {
                                                "puanduello" to puan
                                            } else {
                                                "puan" to puan
                                            },
                                            "saripuan" to sariPuan,
                                            "yesilpuan" to yesilPuan,
                                            "kalansaniye" to timeLeftKopya,
                                            "tahminsüresi" to timeCount
                                        )
                                    )
                                }
                                if (currentRow + 1 == harfSayisi && rakipOyuncuKelimeyiBulamadiMi) {

                                    getRivalScoreOnce(
                                        selectedGameType,
                                        selectedRoom,
                                        rivalId
                                    ) { score ->
                                        rivalScore = score
                                        println("rakip puannnn : $rivalScore ")

                                        when {
                                            puan > rivalScore -> updateGamerWinnerInRoom(
                                                selectedGameType,
                                                selectedRoom,
                                                userId,
                                                rivalId,
                                                "kazanan",
                                                "kaybeden"
                                            ) {
                                                //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                                            }

                                            puan < rivalScore -> updateGamerWinnerInRoom(
                                                selectedGameType,
                                                selectedRoom,
                                                userId,
                                                rivalId,
                                                "kaybeden",
                                                "kazanan"
                                            ) {
                                                //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                                            }

                                            else -> if (puan != 0) {
                                                updateGamerWinnerInRoom(
                                                    selectedGameType,
                                                    selectedRoom,
                                                    userId,
                                                    rivalId,
                                                    "berabere",
                                                    "berabere"
                                                ) {
                                                    //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")
                                                }
                                            }
                                        }
                                    }
                                }

                                currentRow++
                                if (harfSayisi > currentRow)
                                    focusRequesters[currentRow * harfSayisi].requestFocus()
                            } else {
                                showErrorDialog = true
                            }


                        } else if (currentRow == 6 && harfSayisi >= 7) {
                            puan = 0
                            yesilPuan = 0
                            sariPuan = 0
                            enteredWord7 = letters.joinToString(separator = "")
                            enteredWord7 = enteredWord7.takeLast(harfSayisi)
                            println(enteredWord7)

                            var sonuc = KelimeVarmi(
                                kelime = enteredWord7,
                                dortHarfliKelimeler = dortHarfliKelimeler,
                                besHarfliKelimeler = besHarfliKelimeler,
                                altiHarfliKelimeler = altiHarfliKelimeler,
                                yediHarfliKelimeler = yediHarfliKelimeler,
                                harfSayisi = harfSayisi
                            )

                            if (sonuc) {
                                updateGameInfoInRoom(
                                    selectedGameType,
                                    selectedRoom,
                                    userId,
                                    hashMapOf(
                                        "word7" to enteredWord7
                                    )
                                )
                                for (i in 0 until harfSayisi) {

                                    if (enteredWord7[i] == bulunacakKelime[i]) {
                                        textColorList[i + harfSayisi * currentRow] =
                                            Color(android.graphics.Color.parseColor("#0AA351"))
                                        puan += 10
                                        yesilPuan += 10
                                    } else {
                                        for (j in 0 until harfSayisi) {
                                            if (enteredWord7[i] == bulunacakKelime[j] && i != j) {
                                                textColorList[i + harfSayisi * currentRow] =
                                                    Color(android.graphics.Color.parseColor("#F2F90C"))
                                                puan += 5
                                                sariPuan += 5
                                            }

                                        }
                                    }

                                }

                                if (bulunacakKelime == enteredWord7) {
                                    showErrorDialog2 = true
                                } else if (currentRow + 1 == harfSayisi) {
//false tahmin hakkı bitti
                                    puan += timeLeftKopya
                                    println("puannnn : $puan ")
                                    updateGameInfoInRoom(
                                        selectedGameType,
                                        selectedRoom,
                                        userId,
                                        hashMapOf(
                                            "hasGuessingRight" to false,
                                            if (isDuello) {
                                                "puanduello" to puan
                                            } else {
                                                "puan" to puan
                                            },
                                            "saripuan" to sariPuan,
                                            "yesilpuan" to yesilPuan,
                                            "kalansaniye" to timeLeftKopya,
                                            "tahminsüresi" to timeCount
                                        )
                                    )
                                }
                                if (currentRow + 1 == harfSayisi && rakipOyuncuKelimeyiBulamadiMi) {

                                    getRivalScoreOnce(
                                        selectedGameType,
                                        selectedRoom,
                                        rivalId
                                    ) { score ->
                                        rivalScore = score
                                        println("rakip puannnn : $rivalScore ")

                                        when {
                                            puan > rivalScore -> updateGamerWinnerInRoom(
                                                selectedGameType,
                                                selectedRoom,
                                                userId,
                                                rivalId,
                                                "kazanan",
                                                "kaybeden"
                                            ) {
                                                //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                                            }

                                            puan < rivalScore -> updateGamerWinnerInRoom(
                                                selectedGameType,
                                                selectedRoom,
                                                userId,
                                                rivalId,
                                                "kaybeden",
                                                "kazanan"
                                            ) {
                                                //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")

                                            }

                                            else -> if (puan != 0) {
                                                updateGamerWinnerInRoom(
                                                    selectedGameType,
                                                    selectedRoom,
                                                    userId,
                                                    rivalId,
                                                    "berabere",
                                                    "berabere"
                                                ) {
                                                    //navController.navigate("finish/$selectedGameType/$selectedRoom/$rivalId")
                                                }
                                            }
                                        }
                                    }
                                }

                                currentRow++
                                if (harfSayisi > currentRow)
                                    focusRequesters[currentRow * harfSayisi].requestFocus()
                            } else {
                                showErrorDialog = true
                            }


                        }


                    } else {
                        keyboardController?.hide() // Klavyeyi gizle
                    }
                }
                /*
                if (context is Activity) {
                    CoroutineScope(Dispatchers.Main).launch {
                        captureAndUploadScreenshot(context, userId)
                    }
                }*/
            },

            enabled = if (harfSayisi > currentRow) {
                letters.slice(currentRow * harfSayisi until (currentRow + 1) * harfSayisi)
                    .all { it.isNotEmpty() }
            } else {
                // Eğer harfSayisi, currentRow'dan küçük veya eşitse, enabled değerini false olarak ayarla
                false
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text("Next Row")
        }


    }

// İlk kutucuğa odaklan
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

fun KelimeVarmi(
    kelime: String,
    dortHarfliKelimeler: List<String>,
    besHarfliKelimeler: List<String>,
    altiHarfliKelimeler: List<String>,
    yediHarfliKelimeler: List<String>,
    harfSayisi: Int
): Boolean {
    var isFindWord = false


    if (harfSayisi == 4 && dortHarfliKelimeler.map { it.uppercase(Locale.forLanguageTag("tr-TR")) }
            .contains(kelime)) {
        isFindWord = true


    } else if (harfSayisi == 5 && besHarfliKelimeler.map { it.uppercase(Locale.forLanguageTag("tr-TR")) }
            .contains(kelime)) {
        isFindWord = true

    } else if (harfSayisi == 6 && altiHarfliKelimeler.map { it.uppercase(Locale.forLanguageTag("tr-TR")) }
            .contains(kelime)) {
        isFindWord = true

    } else if (harfSayisi == 7 && yediHarfliKelimeler.map { it.uppercase(Locale.forLanguageTag("tr-TR")) }
            .contains(kelime)) {

        isFindWord = true
    }



    return isFindWord

}

fun updateGameInfoInRoom(
    gameType: Boolean,
    roomType: String,
    userId: String,
    updateData: Map<String, Any>
) {
    val db = FirebaseFirestore.getInstance()
    val gameInfoPath = "game_rooms/$gameType/rooms/$roomType/gamer$roomType/$userId"

    db.document(gameInfoPath).set(updateData, SetOptions.merge())
        .addOnSuccessListener {
            Log.d("UpdateGameInfoInRoom", "Game info updated successfully for user $userId.")
        }
        .addOnFailureListener { exception ->
            Log.e("UpdateGameInfoInRoom", "Failed to update game info for user $userId", exception)
        }
}


fun updateGamerWinnerInRoom(
    gameType: Boolean,
    roomType: String,
    userId: String,
    rivalId: String,
    status: String,
    status1: String,
    onComplete: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val gamerPath = "game_rooms/$gameType/rooms/$roomType/gamer$roomType"

    db.collection(gamerPath).get().addOnSuccessListener { snapshot ->
        val loserGamerPath = "$gamerPath/$rivalId"
        db.document(loserGamerPath).set(hashMapOf("status" to status1), SetOptions.merge())
            .addOnSuccessListener {
                Log.d("updateGamerWinnerInRoom", "Winner updated successfully.")
                onComplete()
            }
            .addOnFailureListener { exception ->
                Log.e("updateGamerWinnerInRoom", "Failed to update winner", exception)
                onComplete()
            }

// Şu anki kullanıcının (kazananın) durumunu güncelle
        val winnerGamerPath = "$gamerPath/$userId"
        db.document(winnerGamerPath).set(hashMapOf("status" to status), SetOptions.merge())
            .addOnSuccessListener {
                Log.d("updateGamerWinnerInRoom", "Winner updated successfully.")
                onComplete()
            }
            .addOnFailureListener { exception ->
                Log.e("updateGamerWinnerInRoom", "Failed to update winner", exception)
                onComplete()
            }
    }.addOnFailureListener { exception ->
        Log.e("updateGamerWinnerInRoom", "Failed to retrieve gamers", exception)
        onComplete()
    }
}

fun listenForOtherGamerWord(
    gameType: Boolean,
    roomType: String,
    rivalId: String,
    onWordReceived: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val rivalRef = db.document("game_rooms/$gameType/rooms/$roomType/gamer$roomType/$rivalId")

    rivalRef.addSnapshotListener { documentSnapshot, e ->
        if (e != null) {
            Log.e("ListenForRivalWord", "Listening failed.", e)
            return@addSnapshotListener
        }

// Dokümandan kelimeyi alıp callback fonksiyonunu çağır
        val word = documentSnapshot?.getString("word") ?: ""
        if (word.isNotEmpty()) {
            onWordReceived(word)
        }
    }
}

fun listenForOtherGamerGuessingRightLoss(
    gameType: Boolean,
    roomType: String,
    myUserId: String,
    onGuessingRightLost: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val gamersRef = db.collection("game_rooms/$gameType/rooms/$roomType/gamer$roomType")

    gamersRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.e("ListenForOtherGamerGuessingRightLoss", "Listening failed.", e)
            return@addSnapshotListener
        }

        snapshot?.documents?.forEach { document ->
            val userId = document.id
// Kendi userId'imi hariç tut ve hasGuessingRight özelliğine bak
            if (userId != myUserId) {
                val hasGuessingRight = document.getBoolean("hasGuessingRight") ?: true
                if (!hasGuessingRight) { // Eğer hasGuessingRight false ise
                    onGuessingRightLost() // Karşı tarafın tahmin hakkını kaybettiği callback fonksiyonunu çağır
                }
            }
        }
    }
}

fun listenForGameStart(
    gameType: Boolean,
    roomType: String,
    rivalId: String,
    onGameStart: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val rivalRef = db.document("game_rooms/$gameType/rooms/$roomType/gamer$roomType/$rivalId")

    rivalRef.addSnapshotListener { documentSnapshot, e ->
        if (e != null) {
            Log.e("ListenForGameStart", "Listening failed.", e)
            return@addSnapshotListener
        }

// Doküman üzerinden direkt olarak 'ready' değerini kontrol edin
        val rivalReady = documentSnapshot?.getBoolean("ready") ?: false
        if (rivalReady) {
            Log.d("GameStart", "Rival is ready. Checking if the game can start...")
            onGameStart()
        }
    }
}

data class PlayerReadyStatus(val userId: String, val isReady: Boolean)

fun listenForGameReadyStatus(
    gameType: Boolean,
    roomType: String,
    onGameReadyStatusChanged: (List<PlayerReadyStatus>) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val gamersRef = db.collection("game_rooms/$gameType/rooms/$roomType/gamer$roomType")

    gamersRef.addSnapshotListener { snapshots, e ->
        if (e != null) {
            Log.e("ListenForGameReadyStatus", "Listening failed.", e)
            return@addSnapshotListener
        }

        val readyStatusList = mutableListOf<PlayerReadyStatus>()
        snapshots?.documents?.forEach { document ->
            val userId = document.id // Örneğin, belgenin ID'si kullanıcı ID'si olabilir
            val isReady = document.getBoolean("ready") ?: false
            readyStatusList.add(PlayerReadyStatus(userId, isReady))
        }

// Hazır durumdaki kullanıcıları ve durumlarını döndür
        onGameReadyStatusChanged(readyStatusList)
    }
}

// Oyuncuyu gamer koleksiyonundan silen fonksiyon
fun deletePlayerFromGamerCollection(gameType: Boolean, roomType: String, userId: String) {
    val db = FirebaseFirestore.getInstance()
    val gamerPath = "game_rooms/$gameType/rooms/$roomType/gamer$roomType/$userId"

    db.document(gamerPath).delete()
        .addOnSuccessListener {
            Log.d("deletePlayer", "Player successfully deleted from gamer collection.")
        }
        .addOnFailureListener { exception ->
            Log.e("deletePlayer", "Error deleting player from gamer collection", exception)
        }
}

fun getRivalScoreOnce(
    gameType: Boolean,
    roomType: String,
    rivalId: String,
    onScoreReceived: (Int) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
// Belirtilen rakibin puanının saklandığı dokümanın yolu
    val rivalRef = db.document("game_rooms/$gameType/rooms/$roomType/gamer$roomType/$rivalId")

    rivalRef.get().addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot.exists()) {
// Dokümandan puanı al ve callback fonksiyonunu çağır
            val score = documentSnapshot.getLong("puan")?.toInt() ?: 0
            onScoreReceived(score)
        } else {
            Log.d("GetRivalScoreOnce", "No such document")
        }
    }.addOnFailureListener { e ->
        Log.e("GetRivalScoreOnce", "Error getting document: ", e)
    }
}

@Composable
fun LetterInput(
    letter: String,
    onValueChange: (String) -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
    kutuBoyutu: Int = 48, // Varsayılan kutu boyutu
    yaziBoyutu: Int,
    isLocked: Boolean = false, // Kilit durumunu kontrol eden yeni parametre
    lockedLetter: String = "", // Kilitliyken gösterilecek harf
    textColorList: List<Color>, // Harf renklerini tutmak için liste
    index: Int // Harf indexi
) {
    val displayLetter =
        if (isLocked) lockedLetter else letter // Kilitliyse, belirlenen harfi kullan


    BasicTextField(
        value = displayLetter,
        onValueChange = { value ->
            if (!isLocked) { // Eğer kilitli değilse, değer güncellemesine izin ver
                onValueChange(value)
            }
        },
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = yaziBoyutu.sp,
            color = textColorList[index],
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier
            .focusRequester(focusRequester)
            .size(kutuBoyutu.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(1.dp),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.Center) { innerTextField() }
        },
        enabled = !isLocked, // Kilit durumuna göre etkinliği ayarla
        cursorBrush = SolidColor(Color.Black)
    )
}


