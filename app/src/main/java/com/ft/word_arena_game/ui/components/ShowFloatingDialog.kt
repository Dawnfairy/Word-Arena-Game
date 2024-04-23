package com.ft.word_arena_game.ui.components
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ShowFloatingDialog(onDismiss: () -> Unit, wordList: List<String>, gridSize: Int = 4,arananKelime: String) {


    val completeWordList = wordList +
            List(maxOf(0, gridSize - wordList.size)) { "" }
    val paddedWordList = completeWordList.map { it.padEnd(gridSize, ' ') }.take(gridSize)

    Dialog(onDismissRequest = { onDismiss() }) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                paddedWordList.forEachIndexed { rowIndex, row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.chunked(1).forEachIndexed { columnIndex, char ->
                            // Kutucuğun rengini belirle
                            val color = when {
                                arananKelime.length > columnIndex &&
                                        char[0].uppercase() == arananKelime[columnIndex].uppercase() -> Color.Green // Doğru yerde doğru harf
                                arananKelime.contains(char, ignoreCase = true) -> Color.Yellow // Yanlış yerde doğru harf
                                else -> Color.LightGray // Harf yok
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(Color.White)
                                    .size(30.dp) // Kutucuk boyutu
                                    .border(width = 1.dp, color = Color.Black)
                            ) {
                                Text(text = char, fontSize = 20.sp, color = color)
                            }
                        }
                    }
                }
                // Dialogu kapatma butonu
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                ) {
                    Text("Kapat")
                }
            }
        }
    }


}
