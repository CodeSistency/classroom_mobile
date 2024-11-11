package com.example.classroom.presentation.screens.submission.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.common.CustomInput.CustomTextField
import com.example.classroom.common.previewDocument.DocumentPreviewComponent
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.AzulGradient

@Composable
fun ReviewSubmissionScreen(
    submission: LocalActivitySubmission,
    onDownloadFile: (String) -> Unit,
    onGradeChange: (Float) -> Unit,
    onSubmitGrade: () -> Unit
) {
    var grade by remember { mutableStateOf(submission.grade) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Document preview and download
        if (submission.documentUrl != null) {
            DocumentPreviewComponent(
                documentUrl = submission.documentUrl,
                onDownloadFile = onDownloadFile
            )
        } else {
            Text(text = "Ningun documento.", style = MaterialTheme.typography.body2)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display student's comment
        Text(text = "Comentario del estudiante:", style = MaterialTheme.typography.subtitle1)
        submission.comment?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grade input
//        Text(text = "Calificación (0-100):", style = MaterialTheme.typography.subtitle1)
//        OutlinedTextField(
//            value = grade.toString(),
//            onValueChange = { value ->
//                val newGrade = value.toFloatOrNull()
//                if (newGrade != null && newGrade in 0f..100f) {
//                    grade = newGrade
//                    onGradeChange(newGrade)
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )


        CustomTextField(value = grade.toString(),
            onValueChange = { value ->
                val newGrade = value.toFloatOrNull()
                if (newGrade != null && newGrade in 0f..100f) {
                    grade = newGrade
                    onGradeChange(newGrade)
                }
            }, label = "Calificación (0-100)") {
            
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Submit grade button
        CustomButton(
            onClick = onSubmitGrade,
            modifier = Modifier.fillMaxWidth(),
            text = "Guardar calificacion",
            style = NavigationButtonStyle.SolidGradient,
            color2 = Azul,
            color1 = AzulGradient

        )
    }
}