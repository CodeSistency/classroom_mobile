package com.example.classroom.common.composables.customDialogs.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.classroom.R
import com.example.classroom.common.composables.customDialogs.CardDialogParent
import com.example.classroom.presentation.theme.Azul

@Composable
fun LoadingDialog(

) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        CardDialogParent(
            showButton = false,
            title = stringResource(id = R.string.loading),
            color = Azul,
            isLoading = true
        )
    }
}