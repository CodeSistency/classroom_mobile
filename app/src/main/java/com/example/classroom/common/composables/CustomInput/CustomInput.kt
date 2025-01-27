package com.example.classroom.common.composables.CustomInput

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

enum class ValidationRegex(val pattern: Regex) {
    Email(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")),
    Phone(Regex("^\\+[0-9]{2,3} [0-9]{6,10}\$")),
    Alphanumeric(Regex("^[A-Za-z0-9]+$")),
    AlphanumericWithSpaces(Regex("^[A-Za-z0-9 ]+$")),
    AllCharacters(Regex("^.*\$"))

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
    validationRegex: ValidationRegex = ValidationRegex.AllCharacters,
    password: Boolean = false,
    errorMessage: String = "Invalid input",
    onNextClick: () -> Unit,
    countryCodes: List<String> = listOf("+58", "+1", "+34", "+44", "+52", "+91"),
    showCountryCode: Boolean = false,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var textFieldState by remember { mutableStateOf(TextFieldState.Default) }
    var displayErrorMessage by remember { mutableStateOf(false) }
    var selectedCountryCode by remember { mutableStateOf("+58") } // Default is +58
    var expanded by remember { mutableStateOf(false) }

    fun validateInput(fullText: String): Boolean {
        return validationRegex.pattern.matches(fullText)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Show dropdown only if showCountryCode is true
            if (showCountryCode) {
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier.padding(end = 8.dp).height(48.dp)
                    ) {
                        Text(text = selectedCountryCode, fontSize = 14.sp)
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        countryCodes.forEach { code ->
                            DropdownMenuItem(onClick = {
                                selectedCountryCode = code
                                expanded = false
                            }) {
                                Text(text = code, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = value.removePrefix("$selectedCountryCode ").trimStart(), // Only display the phone number part
                onValueChange = { input ->
                    // Clean the input to ensure the country code isn't added multiple times
                    val cleanedInput = input.trim() // Remove any extra spaces

                    // If the country code exists in the input, remove it
                    val phoneNumberPart = if (cleanedInput.startsWith(selectedCountryCode)) {
                        cleanedInput.removePrefix("$selectedCountryCode ").trimStart()
                    } else {
                        cleanedInput
                    }

                    // Reconstruct the full value with the country code
                    val formattedValue = if (showCountryCode) {
                        "$selectedCountryCode $phoneNumberPart".trim()
                    } else {
                        phoneNumberPart
                    }

                    // Update the full value including the country code
                    onValueChange(if (showCountryCode) formattedValue else input)

                    // Validation logic
                    textFieldState = if (validateInput(if (showCountryCode) formattedValue else input)) {
                        displayErrorMessage = false
                        if (showCountryCode){
                            if (phoneNumberPart.isNotEmpty()) TextFieldState.Success else TextFieldState.Default
                        }else{
                            TextFieldState.Success
                        }

                    } else {
                        displayErrorMessage = true
                        TextFieldState.Error
                    }
                },
                label = { Text(text = label, fontSize = 12.sp, color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
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
                visualTransformation = if (password){
                    if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                                                    }else{
                    VisualTransformation.None
                                                         },

                enabled = enabled,
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
                }
            )
        }

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

//@Composable
//fun CustomTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    modifier: Modifier = Modifier,
//    icon: ImageVector? = null,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    imeAction: ImeAction = ImeAction.Done,
//    enabled: Boolean = true,
//    shape: Shape = RoundedCornerShape(20.dp),
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
//    fun validateInput(text: String): Boolean {
//        return validationRegex?.pattern?.matches(text) ?: true
//    }
//
//    Column(modifier = modifier.fillMaxWidth()) {
//        OutlinedTextField(
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
//                if (icon != null){
//                    Icon(
//                        imageVector = icon,
//                        contentDescription = null,
//                        tint = Color.Black
//                    )
//                }
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
//            label = { Text(text = label, fontSize = 12.sp, color = Color.Gray) },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp)
//                .padding(horizontal = 8.dp)
//                .background(Color.Transparent, shape),
//            shape = shape,
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                backgroundColor = Color.White,
//                focusedBorderColor = when (textFieldState) {
//                    TextFieldState.Success -> successColor
//                    TextFieldState.Error -> errorColor
//                    else -> borderColor
//                },
//                unfocusedBorderColor = borderColor,
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