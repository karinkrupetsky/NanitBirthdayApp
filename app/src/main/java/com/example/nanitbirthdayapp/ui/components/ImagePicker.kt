package com.example.nanitbirthdayapp.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.nanitbirthdayapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * A private helper class to store logic for image picker.
 */
private class ImagePickerHandler(
    private val context: Context,
    private val scope: CoroutineScope,
    private val onPhotoSelected: (Uri?) -> Unit,
    private val onError: (String) -> Unit
) {
    private fun saveBitmapAndGetUri(bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "camera_photo_${UUID.randomUUID()}.jpg")
        return try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            }
            Log.d("ImagePicker", "Bitmap saved successfully to: ${file.absolutePath}")
            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e("ImagePicker", "Failed to save camera bitmap", e)
            onError(context.getString(R.string.failed_to_save_image))
            null
        }
    }

    fun handleCameraResult(bitmap: Bitmap?) {
        if (bitmap != null) {
            scope.launch(Dispatchers.IO) {
                val uri = saveBitmapAndGetUri(bitmap)
                withContext(Dispatchers.Main) {
                    onPhotoSelected(uri)
                }
            }
        } else {
            Log.e("ImagePicker", "Photo capture failed, bitmap is null")
            onError(context.getString(R.string.failed_to_capture_photo))
            onPhotoSelected(null)
        }
    }

    fun handleGalleryResult(uri: Uri?) {
        if (uri == null) {
            Log.d("ImagePicker", "No image selected from gallery.")
        }
        onPhotoSelected(uri)
    }
}

/**
 * A  function that remembers the necessary launchers and returns a pair of functions
 * to launch the camera and gallery
 */
@Composable
fun rememberImagePicker(
    onPhotoSelected: (Uri?) -> Unit,
    onError: (String) -> Unit
): Pair<() -> Unit, () -> Unit> {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imagePickerHandler = remember { ImagePickerHandler(context, scope, onPhotoSelected, onError) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { imagePickerHandler.handleCameraResult(it) }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { imagePickerHandler.handleGalleryResult(it) }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("ImagePicker", "Permission was granted by user.")
            cameraLauncher.launch(null)
        } else {
            Log.e("ImagePicker", "Permission was denied by user.")
            onError(context.getString(R.string.failed_to_capture_photo))
            onPhotoSelected(null)
        }
    }

    val launchCamera: () -> Unit = {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.d("ImagePicker", "Permission already granted. Launching camera directly.")
            cameraLauncher.launch(null)
        } else {
            Log.d("ImagePicker", "Permission not granted. Requesting permission now.")
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val launchGallery: () -> Unit = {
        galleryLauncher.launch("image/*")
    }

    return remember(onPhotoSelected, onError) { Pair(launchCamera, launchGallery) }
}
