package com.example.classroom.common.composables.CustomDatePicker

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient
import java.util.*

@Composable
fun CustomDatePicker(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    color1: Color = Azul,
    color2: Color = AzulGradient
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // State to open and close the DatePickerDialog
    var isDialogOpen by remember { mutableStateOf(false) }

    // Handle DatePickerDialog
    if (isDialogOpen) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val pickedDate = "${dayOfMonth.toString().padStart(2, '0')}/${(month + 1).toString().padStart(2, '0')}/$year"
                onDateSelected(pickedDate)
                isDialogOpen = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Display selected date and open DatePickerDialog on click
    Box(modifier = Modifier){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(color1, color2)),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { isDialogOpen = true }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = if (selectedDate.isEmpty()) label else selectedDate,
                color = if (selectedDate.isEmpty()) Color.Black else Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

    }
}
