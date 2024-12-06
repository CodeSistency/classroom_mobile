package com.example.classroom.common.composables.CustomInput

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment

enum class ValidationRegex(val pattern: Regex) {
    Email(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")),
    Phone(Regex("^\\+?[0-9]{10,13}\$")),
    Alphanumeric(Regex("^[A-Za-z0-9]+$"))
}

enum class TextFieldState {
    Default, Success, Error
}
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Done,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(20.dp),
    borderColor: Color = Color(0xFFB0BEC5),
    successColor: Color = Color(0xFF4CAF50),
    errorColor: Color = Color(0xFFF44336),
    validationRegex: ValidationRegex? = null,
    password: Boolean = false,
    errorMessage: String = "Invalid input",
    onNextClick: () -> Unit,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var textFieldState by remember { mutableStateOf(TextFieldState.Default) }
    var displayErrorMessage by remember { mutableStateOf(false) }

    fun validateInput(text: String): Boolean {
        return validationRegex?.pattern?.matches(text) ?: true
    }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                onValueChange(input)
                textFieldState = if (validateInput(input)) {
                    displayErrorMessage = false
                    if (input.isNotEmpty()) TextFieldState.Success else TextFieldState.Default
                } else {
                    displayErrorMessage = true
                    TextFieldState.Error
                }
            },
            leadingIcon = {
                if (icon != null){
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            },
            trailingIcon = {
                if (password) {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = Color.Gray
                        )
                    }
                }
            },
            label = { Text(text = label, fontSize = 12.sp, color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp)
                .background(Color.Transparent, shape),
            shape = shape,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = when (textFieldState) {
                    TextFieldState.Success -> successColor
                    TextFieldState.Error -> errorColor
                    else -> borderColor
                },
                unfocusedBorderColor = borderColor,
                cursorColor = MaterialTheme.colors.primary,
                textColor = Color.Black
            ),
            keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNextClick()
                }
            ),
            enabled = enabled
        )

        if (displayErrorMessage) {
            Text(
                text = errorMessage,
                color = errorColor,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

//
//enum class ValidationRegex(val pattern: Regex) {
//    Email(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")),
//    Phone(Regex("^\\+?[0-9]{10,13}\$")),
//    Alphanumeric(Regex("^[A-Za-z0-9]+$"))
//}
//
//enum class TextFieldState {
//    Default, Success, Error
//}
//
//@Composable
//fun CustomTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    modifier: Modifier = Modifier,
//    icon: ImageVector = Icons.Default.Email,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    imeAction: ImeAction = ImeAction.Done,
//    enabled: Boolean = true,
//    shape: Shape = RoundedCornerShape(16.dp),
//    borderColor: Color = Color(0xFFB0BEC5),
//    successColor: Color = Color(0xFF4CAF50),
//    errorColor: Color = Color(0xFFF44336),
//    validationRegex: ValidationRegex? = null,
//    password: Boolean = false,
//    errorMessage: String = "Invalid input",
//    onNextClick: () -> Unit,
//) {
//    var isPasswordVisible by remember { mutableStateOf(false) }
//    var textFieldState by remember { mutableStateOf(TextFieldState.Default) }
//    var displayErrorMessage by remember { mutableStateOf(false) }
//
//    // Private function to validate input based on regex
//    fun validateInput(text: String): Boolean {
//        return validationRegex?.pattern?.matches(text) ?: true
//    }
//
//    Column(modifier = modifier.fillMaxWidth()) {
//        TextField(
//            value = value,
//            onValueChange = { input ->
//                onValueChange(input)
//                textFieldState = if (validateInput(input)) {
//                    displayErrorMessage = false
//                    if (input.isNotEmpty()) TextFieldState.Success else TextFieldState.Default
//                } else {
//                    displayErrorMessage = true
//                    TextFieldState.Error
//                }
//            },
//            leadingIcon = {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = null,
//                    tint = Color.Black
//                )
//            },
//            trailingIcon = {
//                if (password) {
//                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
//                        Icon(
//                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
//                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
//                            tint = Color.Gray
//                        )
//                    }
//                }
//            },
//            label = { Text(text = label, fontSize = 12.sp) },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp)
//                .background(Color.Transparent, shape)
//                .padding(horizontal = 8.dp),
//            shape = shape,
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = Color.Transparent,
//                focusedIndicatorColor = when (textFieldState) {
//                    TextFieldState.Success -> successColor
//                    TextFieldState.Error -> errorColor
//                    else -> borderColor
//                },
//                unfocusedIndicatorColor = when (textFieldState) {
//                    TextFieldState.Success -> successColor
//                    TextFieldState.Error -> errorColor
//                    else -> borderColor
//                },
//                cursorColor = MaterialTheme.colors.primary,
//                textColor = Color.Black
//            ),
//            keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
//            keyboardActions = KeyboardActions(
//                onNext = {
//                    onNextClick()
//                }
//            ),
//            enabled = enabled
//        )
//
//        if (displayErrorMessage) {
//            Text(
//                text = errorMessage,
//                color = errorColor,
//                fontSize = 12.sp,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 16.dp, top = 4.dp)
//            )
//        }
//    }
//}