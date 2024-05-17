package com.example.classroom.common.composables.customDialogs.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.classroom.R
import com.example.classroom.common.composables.customDialogs.CardDialogParent
import com.example.classroom.presentation.theme.Rojo

@Composable
fun WarningDialog(
    message: String,
    onDismissRequest: () -> Unit = {},
    secondaryClick: ()-> Unit = {},
    onClick: ()-> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        CardDialogParent(
            showButton = true,
            title = stringResource(id = R.string.warningTitle),
            color = Rojo,
            message = message,
            isForTwoButtons = true,
            titleButtonSecondary = stringResource(id = R.string.cancel),
            onClick = {
                onClick()
            },
            onClickCancel = {
                secondaryClick()
            }
        )
    }
}