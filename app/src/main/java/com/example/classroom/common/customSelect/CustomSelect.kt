package com.example.classroom.common.customSelect

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient

@Composable
fun <T> CustomSelect(
    label: String,
    options: List<T>,
    selectedOption: List<T>,
    onOptionSelected: (List<T>) -> Unit,
    multiple: Boolean = false,
    optionDisplay: (T) -> String, // Lambda to define how options are displayed
    modifier: Modifier = Modifier,
    color1: Color = Azul,
    color2: Color = AzulGradient
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = if (multiple) {
        selectedOption.joinToString { optionDisplay(it) }
    } else {
        selectedOption.firstOrNull()?.let { optionDisplay(it) } ?: ""
    }

    Box(modifier = Modifier.padding(horizontal = 8.dp)){
        Column(modifier = modifier) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(color1, color2)), RoundedCornerShape(16.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(text = if (selectedText.isEmpty()) "Seleccione" else selectedText, color = Color.Black, fontSize = 14.sp)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                options.forEach { option ->
                    val isSelected = selectedOption.contains(option)
                    DropdownMenuItem(onClick = {
                        expanded = false
                        if (multiple) {
                            // Toggle selection in multiple mode
                            val newSelection = selectedOption.toMutableList()
                            if (isSelected) newSelection.remove(option) else newSelection.add(option)
                            onOptionSelected(newSelection)
                        } else {
                            // Set single selection
                            onOptionSelected(listOf(option))
                        }
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (multiple) {
                                Checkbox(checked = isSelected, onCheckedChange = null)
                            }
                            Text(text = optionDisplay(option), modifier = Modifier.padding(start = if (multiple) 8.dp else 0.dp))
                        }
                    }
                }
            }
        }

    }
}
