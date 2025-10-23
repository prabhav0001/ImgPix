package com.wallpaper.app.ui

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wallpaper.app.R
import com.wallpaper.app.databinding.ActivityWallpaperDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WallpaperDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWallpaperDetailBinding
    private var photoUrl: String? = null
    private var photographer: String? = null
    private var isSettingWallpaper = false

    companion object {
        const val EXTRA_PHOTO_URL = "extra_photo_url"
        const val EXTRA_PHOTOGRAPHER = "extra_photographer"
        const val EXTRA_PHOTO_ID = "extra_photo_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadWallpaperDetails()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.wallpaper_detail)
        }
    }

    private fun loadWallpaperDetails() {
        photoUrl = intent.getStringExtra(EXTRA_PHOTO_URL)
        photographer = intent.getStringExtra(EXTRA_PHOTOGRAPHER)

        photographer?.let {
            binding.photographerTextView.text = getString(R.string.photo_by, it)
        }

        photoUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .placeholder(R.color.placeholder_color)
                .error(R.color.error_color)
                .into(binding.wallpaperImageView)
        }
    }

    private fun setupClickListeners() {
        binding.setWallpaperButton.setOnClickListener {
            setAsWallpaper()
        }

        binding.shareButton.setOnClickListener {
            shareWallpaper()
        }
    }

    private fun setAsWallpaper() {
        if (isSettingWallpaper) return

        photoUrl?.let { url ->
            isSettingWallpaper = true
            binding.progressBar.visibility = View.VISIBLE
            binding.setWallpaperButton.isEnabled = false

            lifecycleScope.launch {
                try {
                    val bitmap = loadBitmapFromUrl(url)
                    if (bitmap != null) {
                        setWallpaperBitmap(bitmap)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@WallpaperDetailActivity,
                                getString(R.string.wallpaper_set_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@WallpaperDetailActivity,
                                getString(R.string.wallpaper_set_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@WallpaperDetailActivity,
                            getString(R.string.wallpaper_set_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } finally {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE
                        binding.setWallpaperButton.isEnabled = true
                        isSettingWallpaper = false
                    }
                }
            }
        }
    }

    private suspend fun loadBitmapFromUrl(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                Glide.with(this@WallpaperDetailActivity)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun setWallpaperBitmap(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            try {
                val wallpaperManager = WallpaperManager.getInstance(this@WallpaperDetailActivity)
                wallpaperManager.setBitmap(bitmap)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun shareWallpaper() {
        photoUrl?.let { url ->
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_wallpaper))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.check_out_wallpaper, url))
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_download -> {
                Toast.makeText(this, getString(R.string.download_feature_coming_soon), Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
