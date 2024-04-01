package com.ft.word_arena_game.ui.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun GameExit()
{
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog = true
            }
        }
    }

    DisposableEffect(context) {
        val dispatcher = (context as OnBackPressedDispatcherOwner).onBackPressedDispatcher
        dispatcher.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }

    if (showDialog) {
        ExitConfirmationDialog(
            onConfirm = {
                backCallback.isEnabled = false
                backCallback.remove()
                (context as OnBackPressedDispatcherOwner).onBackPressedDispatcher.onBackPressed()
            },
            onDismiss = {
                showDialog = false
            }
        )
    }


    Icon(
        imageVector = Icons.Default.ExitToApp,
        contentDescription = "Close",
        modifier = Modifier

            .size(48.dp)
            .padding(8.dp)
            .clickable { showDialog = true }, // Tıklanabilirlik özelliği eklendi
        tint = Color.Black // İkon rengi siyah olarak ayarlandı
    )
    

    
}

@Composable
fun ExitConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Çıkış Onayı") },
        text = {
            Text(
                text = "Oyundan çıkmak istediğinize emin misiniz?\nOyunu kaybedeceksiniz."
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text("Onayla")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Reddet")
            }
        }
    )
}