package com.techquantum.tqdkhata.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.techquantum.tqdkhata.data.model.ClientEntity
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.core.net.toUri

object IntentUtils {

    fun openDialer(context: Context, phone: String) {
        try {
            val cleanPhone = phone.replace(Regex("[^0-9+]"), "")
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:$cleanPhone".toUri()
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open dialer: ${e.message}", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, "WhatsApp not installed or could not open", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, "Share failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
