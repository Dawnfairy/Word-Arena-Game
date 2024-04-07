package com.ft.word_arena_game

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ft.word_arena_game.ui.navigation.NavGraph
import com.ft.word_arena_game.ui.theme.WordArenaGameTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            WordArenaGameTheme {
                FirebaseFirestore.getInstance().clearPersistence()
                    .addOnSuccessListener {
                        // Önbellek başarıyla temizlendi
                        Log.d("FirestoreCache", "Firestore önbelleği başarıyla temizlendi.")
                    }
                    .addOnFailureListener { e ->
                        // Önbellek temizlenirken bir hata oluştu
                        Log.e("FirestoreCache", "Firestore önbelleğini temizleme başarısız.", e)
                    }
                NavGraph()


            }
        }
    }
}