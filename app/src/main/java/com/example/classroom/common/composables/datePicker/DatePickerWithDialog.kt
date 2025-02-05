package com.example.classroom.common.composables.datePicker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.classroom.DateUtils

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWithDialog(
    modifier: Modifier = Modifier,
    dateState: DatePickerState,
    action: () -> Unit,
    dismissDialog: () -> Unit, // Lambda to dismiss the dialog
) {
    // Convert selected milliseconds to a human-readable date
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    val dateToString = millisToLocalDate?.let {
        DateUtils().dateToString(millisToLocalDate)
    } ?: "Select a date"

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display selected date or default prompt
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = dateToString,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )

        // DatePicker dialog
        DatePickerDialog(
            onDismissRequest = { dismissDialog() },
            confirmButton = {
                Button(
                    onClick = {
                        dismissDialog()
                        action() // Trigger the action on confirmation
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp)), // Rounded buttons for modern look
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "OK", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { dismissDialog() },
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(text = "Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        ) {
            DatePicker(
                state = dateState,
                showModeToggle = false // Disable mode toggle for a more modern feel
            )
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerWithDialog(
//    modifier: Modifier = Modifier,
//    dateState: DatePickerState,
//    action: () -> Unit,
//    dismissDialog: () -> Unit, // Lambda to dismiss the dialog
//) {
////    val dateState = rememberDatePickerState()
//    val millisToLocalDate = dateState.selectedDateMillis?.let {
//        DateUtils().convertMillisToLocalDate(it)
//    }
//    val dateToString = millisToLocalDate?.let {
//        DateUtils().dateToString(millisToLocalDate)
//    } ?: "Selecciona una fecha"
//    Column(
//        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
////        Text(
////            modifier = Modifier
////                .fillMaxWidth()
////                .clickable(onClick = {
//////                    showDialog = true
////                }),
////            text = dateToString,
////            textAlign = TextAlign.Center,
////            style = MaterialTheme.typography.headlineMedium
////        )
//            DatePickerDialog(
//                onDismissRequest = { dismissDialog() },
//                confirmButton = {
//                    Button(
//                        onClick = { dismissDialog() }
//                    ) {
//                        Text(text = "OK")
//                    }
//                },
//                dismissButton = {
//                    Button(
//                        onClick = { dismissDialog() }
//                    ) {
//                        Text(text = "Cancel")
//                    }
//                }
//            ) {
//                DatePicker(
//                    state = dateState,
//                    showModeToggle = true
//                )
//            }
//    }
//}