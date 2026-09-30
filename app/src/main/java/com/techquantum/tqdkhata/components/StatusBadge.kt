package com.techquantum.tqdkhata.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techquantum.tqdkhata.model.enums.ProjectStatus
import com.techquantum.tqdkhata.theme.StatusCancelled
import com.techquantum.tqdkhata.theme.StatusCancelledBg
import com.techquantum.tqdkhata.theme.StatusDelivered
import com.techquantum.tqdkhata.theme.StatusDeliveredBg
import com.techquantum.tqdkhata.theme.StatusInProgress
import com.techquantum.tqdkhata.theme.StatusInProgressBg
import com.techquantum.tqdkhata.theme.StatusLead
import com.techquantum.tqdkhata.theme.StatusLeadBg
import com.techquantum.tqdkhata.theme.StatusMeeting
import com.techquantum.tqdkhata.theme.StatusMeetingBg
import com.techquantum.tqdkhata.theme.StatusOnHold
import com.techquantum.tqdkhata.theme.StatusOnHoldBg
import com.techquantum.tqdkhata.theme.StatusQuoted
import com.techquantum.tqdkhata.theme.StatusQuotedBg

@Composable
fun StatusBadge(
    status: ProjectStatus,
    modifier: Modifier = Modifier
) {
    val (textColor, bgColor) = when (status) {
        ProjectStatus.NEW_LEAD -> Pair(StatusLead, StatusLeadBg)
        ProjectStatus.IN_DISCUSSION -> Pair(StatusMeeting, StatusMeetingBg)
        ProjectStatus.QUOTED -> Pair(StatusQuoted, StatusQuotedBg)
        ProjectStatus.IN_PROGRESS -> Pair(StatusInProgress, StatusInProgressBg)
        ProjectStatus.DELIVERED -> Pair(StatusDelivered, StatusDeliveredBg)
        ProjectStatus.ON_HOLD -> Pair(StatusOnHold, StatusOnHoldBg)
        ProjectStatus.CANCELLED -> Pair(StatusCancelled, StatusCancelledBg)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status.displayName,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
