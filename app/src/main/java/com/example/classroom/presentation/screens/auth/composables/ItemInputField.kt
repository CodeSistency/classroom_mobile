package com.example.classroom.presentation.screens.auth.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom.presentation.theme.Gris
import com.example.classroom.presentation.theme.PaddingCustom

@Composable
fun ItemInputField(
    titulo: String,
    darkTheme: Boolean,
    valueField: String,
    fieldRestriction: (String) -> String? = { it },
    valueOnChange: (String) -> Unit,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    changePasswordValue: ()-> Unit = {},
    onNextClick: () -> Unit
) {
    Column(Modifier.padding(horizontal = PaddingCustom.HORIZONTAL_STANDARD.size, vertical = 2.dp)) {
        Text(
            text = titulo,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
//                fontFamily = InterTight
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )

        if (!isPassword){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = valueField,
                onValueChange = {
                    fieldRestriction(it)?.let { inner ->
                        valueOnChange(inner)
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    backgroundColor = MaterialTheme.colors.surface,
//                    focusedBorderColor = MaterialTheme.colors.surface,
//                    disabledBorderColor = MaterialTheme.colors.surface,
//                    unfocusedBorderColor = MaterialTheme.colors.surface,
                    backgroundColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    disabledBorderColor = Gris,
                    unfocusedBorderColor = Gris,
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onNextClick()
                    }
                ),
                maxLines = 1
            )
        } else {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = valueField,
                onValueChange = {
                    fieldRestriction(it)?.let { inner ->
                        valueOnChange(inner)
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    focusedBorderColor = Color.Gray,
                    disabledBorderColor = Gris,
                    unfocusedBorderColor = Gris,
                ),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility

                    androidx.compose.material.IconButton(onClick = { changePasswordValue() }) {
                        Icon(imageVector = icon, contentDescription = "")
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onNextClick()
                    }
                ),
                maxLines = 1
            )
        }

    }
}
