package com.appcake.modsforvalheim.utlis

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import checkPermissionsDexterSe
import com.appcake.modsforvalheim.R
import com.google.android.material.snackbar.Snackbar
import showToastSkins
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object FileUtilsSkins {
    private const val saveImageDirName = "mods_for_valheim"

    fun getScreenShotFromView(viewSkins: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            screenshot = Bitmap.createBitmap(
                viewSkins.measuredWidth, viewSkins.measuredHeight, Bitmap.Config.ARGB_8888
            )
            val canvasSkins = Canvas(screenshot)
            viewSkins.draw(canvasSkins)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }

    fun saveMediaToStorageSkins(contextSkins: Context, fileNameSkins: String, bitmapSkins: Bitmap): String {
        val filenameSkins = "${fileNameSkins + "_" + System.currentTimeMillis()}.png"
        var fosSkins: OutputStream? = null
        val filePathSkins = File(
            Environment.DIRECTORY_PICTURES, saveImageDirName + File.separator + filenameSkins
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contextSkins.contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileNameSkins)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + File.separator + saveImageDirName
                    )
                }

                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fosSkins = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDirSkins = File(
                Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_PICTURES,
                saveImageDirName
            )
            if (!imagesDirSkins.exists()) {
                imagesDirSkins.mkdir()
            }
            val imageSkins = File(imagesDirSkins, filenameSkins)
            fosSkins = FileOutputStream(imageSkins)
        }

        fosSkins.use {
            bitmapSkins.compress(Bitmap.CompressFormat.PNG, 100, it)
            contextSkins.showToastSkins("Image Downloaded")
        }

        return filePathSkins.absolutePath
    }

    fun askForPermission(
        contextSkins: Context, rootViewSkins: View, actionSkins: (isGrantedSkins: Boolean) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            contextSkins.checkPermissionsDexterSe(
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) {
                val permissionsSnackBarSkins = Snackbar.make(
                    rootViewSkins,
                    contextSkins.resources.getString(R.string.permission_storage_needed_skins),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    contextSkins.resources.getString(R.string.open_settings_skins)
                ) {
                    val intentSkins = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uriSkins = Uri.fromParts("package", contextSkins.packageName, null)
                    intentSkins.data = uriSkins
                    contextSkins.startActivity(intentSkins)
                }

                if (it) {
                    if (permissionsSnackBarSkins.isShown) {
                        permissionsSnackBarSkins.dismiss()
                    }
                    actionSkins(true)
                } else {
                    actionSkins(false)
                    permissionsSnackBarSkins.show()
                }
            }
        } else {
            contextSkins.checkPermissionsDexterSe(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                val permissionsSnackBarSkins = Snackbar.make(
                    rootViewSkins,
                    contextSkins.resources.getString(R.string.permission_storage_needed_skins),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    contextSkins.resources.getString(R.string.open_settings_skins)
                ) {
                    val intentSkins = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uriSkins = Uri.fromParts("package", contextSkins.packageName, null)
                    intentSkins.data = uriSkins
                    contextSkins.startActivity(intentSkins)
                }

                if (it) {
                    if (permissionsSnackBarSkins.isShown) {
                        permissionsSnackBarSkins.dismiss()
                    }
                    actionSkins(true)
                } else {
                    actionSkins(false)
                    permissionsSnackBarSkins.show()
                }
            }
        }
    }
}
