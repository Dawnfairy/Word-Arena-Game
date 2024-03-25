package com.ft.word_arena_game.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GameButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp), // Buton kenarlarına ovallik ekle
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(48.dp),
    ) {
        Text(text)
    }
}
@Preview(showBackground = true)
@Composable
fun GameButtonPreview() {
    GameButton(text = "Preview Button", onClick = {})
}
