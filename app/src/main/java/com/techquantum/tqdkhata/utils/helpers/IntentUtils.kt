package com.techquantum.tqdkhata.utils.helpers

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.techquantum.tqdkhata.model.data.local.ClientEntity
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object IntentUtils {

    fun openDialer(context: Context, phone: String) {
        try {
            val cleanPhone = phone.replace(Regex("[^0-9+]"), "")
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:$cleanPhone".toUri()
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open phone dialer", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(context: Context, phone: String, message: String? = null) {
        try {
            var cleanPhone = phone.replace(Regex("[^0-9]"), "")
            if (cleanPhone.length == 10) {
                cleanPhone = "91$cleanPhone"
            }

            val encodedMsg = if (!message.isNullOrBlank()) {
                "?text=" + URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
            } else ""

            val uri = "https://wa.me/$cleanPhone$encodedMsg".toUri()
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.whatsapp")
            }
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                val fallbackIntent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(fallbackIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun openEmail(context: Context, email: String, subject: String? = null) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:$email".toUri()
                if (!subject.isNullOrBlank()) {
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                }
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareClientSummary(context: Context, client: ClientEntity) {
        try {
            val shareText = buildString {
                appendLine("📋 Client Details - TQDKhata")
                appendLine("━━━━━━━━━━━━━━━━━━━")
                appendLine("👤 Name: ${client.name}")
                if (!client.businessName.isNullOrBlank()) {
                    appendLine("🏢 Company: ${client.businessName}")
                }
                appendLine("📞 Phone: ${client.phone}")
                if (!client.altPhone.isNullOrBlank()) {
                    appendLine("📱 Alt Phone: ${client.altPhone}")
                }
                if (!client.city.isNullOrBlank()) {
                    appendLine("📍 City: ${client.city}")
                }
                if (!client.address.isNullOrBlank()) {
                    appendLine("🏠 Address: ${client.address}")
                }
                appendLine("🏷 Status: ${client.status.displayName}")
                appendLine("⚡ Priority: ${client.priority.label}")
                if (!client.budget.isNullOrBlank()) {
                    appendLine("💰 Budget: ${client.budget}")
                }
                appendLine()
                appendLine("📝 Requirement:")
                appendLine(client.requirements)
            }

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Client Info: ${client.name}")
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(intent, "Share Client via"))
        } catch (e: Exception) {
            Toast.makeText(context, "Share failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareJsonBackup(context: Context, jsonContent: String) {
        try {
            val cacheDir = File(context.cacheDir, "backups").apply { mkdirs() }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "TQD_Khata_Backup_$timeStamp.json"
            val file = File(cacheDir, fileName)
            file.writeText(jsonContent, StandardCharsets.UTF_8)

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "TQD Khata JSON Backup - $fileName")
                putExtra(Intent.EXTRA_TEXT, "Here is the TQD Khata backup file in JSON format.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share JSON Backup via"))
        } catch (e: Exception) {
            Toast.makeText(context, "Share failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun openAppSettings(context: Context) {
        try {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:${context.packageName}".toUri()
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open settings", Toast.LENGTH_SHORT).show()
        }
    }
}
