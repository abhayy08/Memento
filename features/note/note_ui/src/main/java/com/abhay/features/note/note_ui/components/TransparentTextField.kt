package com.abhay.features.note.note_ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    isHintVisible: Boolean,
    singleLine: Boolean,
    maxLines:Int = Int.MAX_VALUE,
    fontSize: TextUnit
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = TextStyle(
                color = if(isSystemInDarkTheme()) Color.White else Color.Black,
                fontSize = fontSize
            ),
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                },
            maxLines = maxLines,
            cursorBrush = if(isSystemInDarkTheme()) SolidColor(Color.White) else SolidColor(Color.Black)
        )
        if(isHintVisible) {
            Text(text = hint , style = TextStyle(
                color = if(isSystemInDarkTheme()) Color.White.copy(alpha = 0.4f) else Color.Gray,
                fontSize = fontSize
            ))
        }
    }
}