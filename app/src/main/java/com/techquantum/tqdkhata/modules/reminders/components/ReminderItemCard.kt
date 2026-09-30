package com.techquantum.tqdkhata.modules.reminders.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techquantum.tqdkhata.model.data.local.ReminderWithClient
import com.techquantum.tqdkhata.theme.BrandNavy
import com.techquantum.tqdkhata.theme.BrandSage
import com.techquantum.tqdkhata.theme.TextMuted
import com.techquantum.tqdkhata.theme.TextPrimary
import com.techquantum.tqdkhata.theme.TextSecondary
import com.techquantum.tqdkhata.theme.WhatsAppGreen
import com.techquantum.tqdkhata.utils.helpers.DateUtils

@Composable
fun ReminderItemCard(
    reminder: ReminderWithClient,
    onToggle: (Boolean) -> Unit,
    onClientClick: () -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOverdue = !reminder.isCompleted && reminder.reminderTimestamp != null && reminder.reminderTimestamp < System.currentTimeMillis()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isCompleted) Color(0xFFF7F7F5) else Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BrandSage.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Checkbox icon
                IconButton(
                    onClick = { onToggle(!reminder.isCompleted) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (reminder.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Toggle Complete",
                        tint = if (reminder.isCompleted) Color(0xFF15803D) else TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reminder.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (reminder.isCompleted) TextMuted else TextPrimary,
                        textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )

                    if (!reminder.notes.isNullOrBlank()) {
                        Text(
                            text = reminder.notes,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    if (reminder.reminderTimestamp != null && reminder.reminderTimestamp > 0L) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = if (isOverdue) Color(0xFFDC2626) else BrandNavy,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = DateUtils.formatDateTime(reminder.reminderTimestamp),
                                fontSize = 11.sp,
                                fontWeight = if (isOverdue) FontWeight.Bold else FontWeight.Medium,
                                color = if (isOverdue) Color(0xFFDC2626) else BrandNavy
                            )
                            if (isOverdue) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• OVERDUE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer: Client name pill + Call / WhatsApp buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Client Name clickable pill
                Row(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onClientClick)
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = BrandNavy,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = reminder.clientName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandNavy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onCallClick,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = BrandNavy,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onWhatsAppClick,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = WhatsAppGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "WhatsApp",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
