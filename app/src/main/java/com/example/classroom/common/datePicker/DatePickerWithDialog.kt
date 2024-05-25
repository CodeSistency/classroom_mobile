package com.example.classroom.common.datePicker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
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
//    val dateState = rememberDatePickerState()
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    val dateToString = millisToLocalDate?.let {
        DateUtils().dateToString(millisToLocalDate)
    } ?: "Selecciona una fecha"
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable(onClick = {
////                    showDialog = true
//                }),
//            text = dateToString,
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.headlineMedium
//        )
            DatePickerDialog(
                onDismissRequest = { dismissDialog() },
                confirmButton = {
                    Button(
                        onClick = { dismissDialog() }
                    ) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { dismissDialog() }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = dateState,
                    showModeToggle = true
                )
            }
    }
}