package com.deecode.walls.util

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.core.net.toUri
import com.deecode.walls.R

class ImageDownloader(private val context: Context) {

    fun downloadImage(url: String, fileName: String? = null) {
        try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = url.toUri()

            val name = fileName ?: "IMG_${System.currentTimeMillis()}.jpg"

            val request = DownloadManager.Request(uri)
                .setTitle(name)
                .setDescription(context.getString(R.string.downloading_image))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "Walls/$name")
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            downloadManager.enqueue(request)
            Toast.makeText(context, context.getString(R.string.download_started), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, context.getString(R.string.download_failed, e.message), Toast.LENGTH_SHORT).show()
        }
    }
}
