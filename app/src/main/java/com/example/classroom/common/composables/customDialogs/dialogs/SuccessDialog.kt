package com.example.classroom.common.composables.customDialogs.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.classroom.R
import com.example.classroom.common.composables.customDialogs.CardDialogParent
import com.example.classroom.presentation.theme.Azul

@Composable
fun SuccessDialog(
    message: String,
    onDismissRequest: () -> Unit = {},
    onClick: ()-> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        CardDialogParent(
            showButton = true,
            title = stringResource(id = R.string.successTitle),
            color = Azul,
            message = message,
            onClick = {
                onClick()
            }
        )
    }
}