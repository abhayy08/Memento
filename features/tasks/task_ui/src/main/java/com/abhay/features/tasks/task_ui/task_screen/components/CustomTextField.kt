package com.abhay.features.tasks.task_ui.task_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = Int.MAX_VALUE,
    placeholder: String = ""
) {

    val cursorColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val materialColor = MaterialTheme.colorScheme.primary
    var borderColor by remember {
        mutableStateOf(materialColor)
    }
    val backgroundColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray


    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                borderColor = if (it.isFocused) materialColor.copy(0.8f) else Color.Transparent
            }
            .border(1.5.dp, borderColor, RoundedCornerShape(8.dp)),
        textStyle = TextStyle(color = textColor),
        minLines = minLines,
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .background(backgroundColor.copy(0.6f), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Box {
                    if (value.isBlank()) {
                        Text(
                            text = placeholder,
                            fontSize = 14.sp,
                            color = textColor.copy(0.3f)
                        )
                    }
                    innerTextField()
                }
            }
        },
        cursorBrush = SolidColor(cursorColor)
    )
}

@Preview(showBackground = true)
@Composable
private fun previewOfTF() {
//    CustomTextField()
}