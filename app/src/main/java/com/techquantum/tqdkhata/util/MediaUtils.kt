package com.techquantum.tqdkhata.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object MediaUtils {

    private const val TAG = "MediaUtils"

    fun getResourceDirectory(context: Context): File {
        val extDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val dir = File(extDir ?: context.filesDir, "client_resources")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun createMediaFile(context: Context, clientId: Long, isVideo: Boolean = false): File {
        val dir = getResourceDirectory(context)
        val ext = if (isVideo) "mp4" else "jpg"
        val prefix = if (isVideo) "vid" else "img"
        val file = File(dir, "${prefix}_client_${clientId}_${System.currentTimeMillis()}.$ext")
        try {
            file.parentFile?.mkdirs()
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating media file: ${e.message}")
        }
        return file
    }

    fun getFileUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun setPendingPhotoPath(context: Context, clientId: Long, path: String) {
        try {
            context.getSharedPreferences("tqd_media_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("pending_photo_$clientId", path)
                .commit()
        } catch (_: Exception) {}
    }

    fun getPendingPhotoPath(context: Context, clientId: Long): String? {
        return try {
            context.getSharedPreferences("tqd_media_prefs", Context.MODE_PRIVATE)
                .getString("pending_photo_$clientId", null)
        } catch (_: Exception) {
            null
        }
    }

    fun clearPendingPhotoPath(context: Context, clientId: Long) {
        try {
            context.getSharedPreferences("tqd_media_prefs", Context.MODE_PRIVATE)
                .edit()
                .remove("pending_photo_$clientId")
                .commit()
        } catch (_: Exception) {}
    }

    fun copyUriToLocalResource(context: Context, sourceUri: Uri, clientId: Long, isVideo: Boolean = false): File? {
        return try {
            val destinationFile = createMediaFile(context, clientId, isVideo)
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            destinationFile
        } catch (e: Exception) {
            Log.e(TAG, "Failed to copy uri to resource: ${e.message}", e)
            null
        }
    }

    fun loadThumbnail(filePath: String, isVideo: Boolean = false): Bitmap? {
        return try {
            var file = File(filePath)
            if (!file.exists() || file.length() == 0L) {
                Log.w(TAG, "File does not exist or is empty: $filePath")
                return null
            }

            if (isVideo) {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(filePath)
                val frame = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                    ?: retriever.frameAtTime
                retriever.release()
                frame
            } else {
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(filePath, options)

                var sampleSize = 1
                val reqSize = 400
                if (options.outWidth > 0 && options.outHeight > 0) {
                    while (options.outWidth / sampleSize > reqSize || options.outHeight / sampleSize > reqSize) {
                        sampleSize *= 2
                    }
                }

                val decodeOptions = BitmapFactory.Options().apply {
                    inSampleSize = sampleSize
                    inPreferredConfig = Bitmap.Config.RGB_565
                }
                val bitmap = BitmapFactory.decodeFile(filePath, decodeOptions)
                    ?: BitmapFactory.decodeFile(filePath) // fallback raw decode if sample failed
                    ?: return null

                // Handle EXIF orientation safely without losing the decoded bitmap
                try {
                    val exif = ExifInterface(filePath)
                    val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    val rotationAngle = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                        else -> 0f
                    }

                    if (rotationAngle != 0f) {
                        val matrix = Matrix().apply { postRotate(rotationAngle) }
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    } else {
                        bitmap
                    }
                } catch (exifError: Throwable) {
                    Log.w(TAG, "EXIF rotation failed, using raw bitmap: ${exifError.message}")
                    bitmap
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading thumbnail for $filePath: ${e.message}", e)
            null
        }
    }

    fun deleteAllCapturedVideos(context: Context) {
        try {
            val dir = getResourceDirectory(context)
            dir.listFiles()?.filter { it.name.startsWith("vid_") || it.name.endsWith(".mp4") }?.forEach {
                it.delete()
            }
            val internalDir = File(context.filesDir, "client_resources")
            internalDir.listFiles()?.filter { it.name.startsWith("vid_") || it.name.endsWith(".mp4") }?.forEach {
                it.delete()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up captured videos: ${e.message}")
        }
    }

    fun viewMedia(context: Context, filePath: String, isVideo: Boolean = false) {
        val file = File(filePath)
        if (!file.exists()) {
            Toast.makeText(context, "Photo not found on device", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val uri = getFileUri(context, file)
            val mimeType = if (isVideo) "video/*" else "image/*"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No app available to open this file", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to open photo", Toast.LENGTH_SHORT).show()
        }
    }
}
