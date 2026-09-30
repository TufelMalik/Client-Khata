package com.techquantum.tqdkhata.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.techquantum.tqdkhata.app.MainActivity
import com.techquantum.tqdkhata.model.data.local.AppDatabase
import com.techquantum.tqdkhata.utils.helpers.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TQDKhataWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val (totalClients, pendingReminders, todaysFollowUps) = withContext(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(context)
                val clientsCount = db.clientDao().getAllClientsList().size
                val reminders = db.reminderDao().getAllRemindersList()
                val pending = reminders.count { !it.isCompleted }
                val endOfDay = DateUtils.getEndOfTodayMillis()
                val today = reminders.count {
                    !it.isCompleted && it.reminderTimestamp != null && it.reminderTimestamp <= endOfDay
                }
                Triple(clientsCount, pending, today)
            } catch (e: Exception) {
                Triple(0, 0, 0)
            }
        }

        provideContent {
            GlanceTheme {
                WidgetContent(
                    totalClients = totalClients,
                    pendingReminders = pendingReminders,
                    todaysFollowUps = todaysFollowUps
                )
            }
        }
    }

    @Composable
    private fun WidgetContent(
        totalClients: Int,
        pendingReminders: Int,
        todaysFollowUps: Int
    ) {
        val navy = Color(0xFF0F243E)
        val cream = Color(0xFFF7F2EB)
        val bronze = Color(0xFFC07A3E)
        val red = Color(0xFFC62828)
        val sage = Color(0xFFC5D3C1)
        val white = Color(0xFFFFFFFF)
        val bg = Color(0xFFFCFAF7)

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(bg)
                .cornerRadius(16.dp)
                .padding(12.dp)
                .clickable(actionStartActivity<MainActivity>())
        ) {
            // Header Row
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TQD Khata",
                    style = TextStyle(
                        color = ColorProvider(navy),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.defaultWeight())
                Text(
                    text = "Tap to open",
                    style = TextStyle(
                        color = ColorProvider(bronze),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            // Stat Cards Row
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Total Clients
                StatBox(
                    label = "Clients",
                    count = totalClients.toString(),
                    badgeColor = navy,
                    textColor = white,
                    modifier = GlanceModifier.defaultWeight()
                )

                Spacer(modifier = GlanceModifier.width(8.dp))

                // Pending Reminders
                StatBox(
                    label = "Pending",
                    count = pendingReminders.toString(),
                    badgeColor = if (pendingReminders > 0) bronze else cream,
                    textColor = if (pendingReminders > 0) white else navy,
                    modifier = GlanceModifier.defaultWeight()
                )

                Spacer(modifier = GlanceModifier.width(8.dp))

                // Today's Follow-ups
                StatBox(
                    label = "Today",
                    count = todaysFollowUps.toString(),
                    badgeColor = if (todaysFollowUps > 0) red else cream,
                    textColor = if (todaysFollowUps > 0) white else navy,
                    modifier = GlanceModifier.defaultWeight()
                )
            }
        }
    }

    @Composable
    private fun StatBox(
        label: String,
        count: String,
        badgeColor: Color,
        textColor: Color,
        modifier: GlanceModifier
    ) {
        Column(
            modifier = modifier
                .background(badgeColor)
                .cornerRadius(12.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = count,
                style = TextStyle(
                    color = ColorProvider(textColor),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            Text(
                text = label,
                style = TextStyle(
                    color = ColorProvider(textColor),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
