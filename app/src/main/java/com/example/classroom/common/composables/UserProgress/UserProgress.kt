package com.example.classroom.common.composables.UserProgress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.Poppins

@Composable
fun StudentProgressGauge(weightedGrade: Double, totalPonderation: Int) {
    val animatedGrade = animateFloatAsState(targetValue = (weightedGrade / 100f).coerceIn(0.0, 1.0).toFloat())
    val animatedPonderation = animateFloatAsState(targetValue = (totalPonderation / 100f).coerceIn(0f, 1f))

    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.Gray.copy(alpha = 0.2f))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val stroke = Stroke(10.dp.toPx(), cap = StrokeCap.Round)
            val stroke2 = Stroke(5.dp.toPx(), cap = StrokeCap.Round)


            drawArc(
                color = Color.Green,
                startAngle = -90f,
                sweepAngle = animatedGrade.value * 360,
                useCenter = false,
                style = stroke
            )
            drawArc(
                color = Color.Blue,
                startAngle = -90f,
                sweepAngle = animatedPonderation.value * 360,
                useCenter = false,
                style = stroke2
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${String.format("%.1f", weightedGrade)}%",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Completed: ${totalPonderation}%",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun StudentPerformanceHeatmap(weightedGrade: Double, totalPonderation: Int) {
    val colors = listOf(Color.LightGray, Color.Blue, Color.Green)
    val gradeColor = colors[(weightedGrade / 50).coerceIn(0.0, 2.0).toInt()]
    val ponderationColor = colors[(totalPonderation / 50).coerceIn(0, 2).toInt()]

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Performance Overview", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(gradeColor),
                contentAlignment = Alignment.Center
            ) {
                Text("${String.format("%.1f", weightedGrade)}%", color = Color.White)
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ponderationColor),
                contentAlignment = Alignment.Center
            ) {
                Text("${totalPonderation}%", color = Color.White)
            }
        }
    }
}

@Composable
fun StudentPerformanceEmoji(weightedGrade: Double, totalPonderation: Int) {
    val gradeEmoji = when {
        weightedGrade >= 90 -> "🚀"
        weightedGrade >= 75 -> "🔥"
        weightedGrade >= 50 -> "😊"
        else -> "😞"
    }

    val ponderationEmoji = when {
        totalPonderation >= 90 -> "✅"
        totalPonderation >= 75 -> "📚"
        totalPonderation >= 50 -> "📈"
        else -> "🔄"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = gradeEmoji, fontSize = 40.sp)
            Text(text = "Grade: ${String.format("%.1f", weightedGrade)}%", fontSize = 14.sp)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = ponderationEmoji, fontSize = 40.sp)
            Text(text = "Completed: ${totalPonderation}%", fontSize = 14.sp)
        }
    }
}

@Composable
fun StudentStackedProgress(weightedGrade: Double, totalPonderation: Int) {
    val animatedGrade = animateFloatAsState(targetValue = (weightedGrade / 100f).coerceIn(0.0, 1.0).toFloat())
    val animatedPonderation = animateFloatAsState(targetValue = (totalPonderation / 100f).coerceIn(0f, 1f))

    Column {
        Text("Progreso de la materia", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.2f)) // Background bar
        ) {
            // Blue (Total Ponderation)
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedPonderation.value)
                    .height(18.dp)
                    .background(Color.Gray.copy(alpha = 0.6f), RoundedCornerShape(topEnd = 50f, bottomEnd = 50f))
            )

            // Green (Weighted Grade) - This is drawn **on top** of blue
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedGrade.value)
                    .height(18.dp)
                    .background(Azul, RoundedCornerShape(topEnd = 50f, bottomEnd = 50f))
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Inline Legend
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Promedio ${String.format("%.1f", weightedGrade)}%", fontSize = 12.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.width(4.dp))
            Box(modifier = Modifier.size(12.dp).background(Azul, shape = CircleShape))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Ponderación evaluada: ${totalPonderation}%", fontSize = 12.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.width(4.dp))
            Box(modifier = Modifier.size(12.dp).background(Color.Gray.copy(alpha = 0.6f), shape = CircleShape))
        }

//        Text(
//            text = "Nota: ${String.format("%.1f", weightedGrade)}%, Ponderación evaluada: ${totalPonderation}%",
//            fontSize = 12.sp,
//            color = Color.Gray
//        )
    }
}


//@Composable
//fun StudentStackedProgress(weightedGrade: Double, totalPonderation: Int) {
//    val animatedGrade = animateFloatAsState(targetValue = (weightedGrade / 100f).coerceIn(0.0, 1.0).toFloat())
//    val animatedPonderation = animateFloatAsState(targetValue = (totalPonderation / 100f).coerceIn(0f, 1f))
//
//    Column() {
//        Text("Progreso de la materia", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//
//        Spacer(modifier = Modifier.height(6.dp))
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(18.dp)
//                .clip(RoundedCornerShape(8.dp))
//                .background(Color.Gray.copy(alpha = 0.2f))
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(animatedPonderation.value)
//                    .height(18.dp)
//                    .background(Color.Blue.copy(alpha = 0.7f))
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth(animatedGrade.value)
//                        .background(Color.Green)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(6.dp))
//
//        Text(
//            text = "Nota: ${String.format("%.1f", weightedGrade)}%, Ponderación evaluada: ${totalPonderation}%",
//            fontSize = 12.sp,
//            color = Color.Gray
//        )
//    }
//}
