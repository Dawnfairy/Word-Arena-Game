package com.ft.word_arena_game.ui.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun GameConnection(context: Context): Boolean {
    var isNetworkConnected by remember { mutableStateOf(true) } // Bağlantı durumunu izleyen değişken

    // Bağlantı durumunu izleyen bir Broadcast Receiver oluşturun
    val networkCallback = remember {
        object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                // Bağlantı kaybedildiğinde değişkeni güncelle
                isNetworkConnected = false
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Bağlantı yeniden sağlandığında değişkeni güncelle
                isNetworkConnected = true
            }
        }
    }

    DisposableEffect(Unit) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Bağlantı durumunu izleme işlemi başlatılıyor
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        onDispose {
            // Komponent kaldırıldığında izleme işlemi durduruluyor
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    // Bağlantı durumuna göre ekranda kullanıcıyı bilgilendirin
    if (!isNetworkConnected) {
        Text("Bağlantı kesildi!")
    }

    return isNetworkConnected

}