package com.techquantum.tqdkhata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techquantum.tqdkhata.data.model.Priority
import com.techquantum.tqdkhata.ui.theme.PriorityHigh
import com.techquantum.tqdkhata.ui.theme.PriorityHighBg
import com.techquantum.tqdkhata.ui.theme.PriorityLow
import com.techquantum.tqdkhata.ui.theme.PriorityLowBg
import com.techquantum.tqdkhata.ui.theme.PriorityMedium
import com.techquantum.tqdkhata.ui.theme.PriorityMediumBg

@Composable
fun PriorityBadge(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    val (textColor, bgColor) = when (priority) {
        Priority.HIGH -> Pair(PriorityHigh, PriorityHighBg)
        Priority.MEDIUM -> Pair(PriorityMedium, PriorityMediumBg)
        Priority.LOW -> Pair(PriorityLow, PriorityLowBg)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 7.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${priority.label} Priority",
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
