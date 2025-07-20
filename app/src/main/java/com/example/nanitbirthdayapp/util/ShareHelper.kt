package com.example.nanitbirthdayapp.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object ShareHelper {

    suspend fun saveBitmapAndShare(context: Context, bitmap: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val shareUri = saveBitmapToCache(context, bitmap)
                if (shareUri != null) {
                    withContext(Dispatchers.Main) {
                        openShareDialog(context, shareUri)
                    }
                    true
                } else {
                    Log.e("ShareHelper", "Failed to save bitmap to cache")
                    false
                }
            } catch (e: Exception) {
                Log.e("ShareHelper", "Error in saveBitmapAndShare", e)
                false
            }
        }
    }

    private fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val cacheDir = File(context.cacheDir, "shared_images")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val imageFile = File(cacheDir, "birthday_${System.currentTimeMillis()}.jpg")
            Log.d("ShareHelper", "Saving bitmap to: ${imageFile.absolutePath}")

            FileOutputStream(imageFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            Log.d("ShareHelper", "Created FileProvider URI: $uri")
            uri
        } catch (e: Exception) {
            Log.e("ShareHelper", "Error saving bitmap to cache", e)
            null
        }
    }

    private fun openShareDialog(context: Context, imageUri: Uri) {
        try {
            Log.d("ShareHelper", "Opening share dialog with URI: $imageUri")
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                putExtra(Intent.EXTRA_TEXT, "Check out this birthday celebration! 🎉")
                putExtra(Intent.EXTRA_SUBJECT, "Happy Birthday!")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share Birthday")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            Log.e("ShareHelper", "Error opening share dialog", e)
        }
    }
}
