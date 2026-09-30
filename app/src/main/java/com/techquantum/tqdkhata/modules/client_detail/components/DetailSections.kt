package com.techquantum.tqdkhata.modules.client_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techquantum.tqdkhata.theme.BrandNavy
import com.techquantum.tqdkhata.theme.TextMuted

@Composable
fun DetailSectionHeader(
    title: String,
    count: Int? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = BrandNavy
        )
        if (count != null) {
            Text(
                text = "($count)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted
            )
        }
    }
}
