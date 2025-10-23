package com.wallpaper.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wallpaper.app.R
import com.wallpaper.app.data.api.RetrofitClient
import com.wallpaper.app.data.model.Photo
import com.wallpaper.app.data.repository.WallpaperRepository
import com.wallpaper.app.databinding.ActivityMainBinding
import com.wallpaper.app.ui.adapter.WallpaperAdapter
import com.wallpaper.app.ui.viewmodel.WallpaperViewModel
import com.wallpaper.app.ui.viewmodel.WallpaperViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WallpaperViewModel
    private lateinit var adapter: WallpaperAdapter
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupViewModel()
        setupObservers()
        setupSwipeRefresh()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun setupRecyclerView() {
        adapter = WallpaperAdapter { photo ->
            openWallpaperDetail(photo)
        }

        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        // Pagination
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                if (dy > 0) { // Scrolling down
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 4)) {
                        viewModel.loadMoreWallpapers()
                    }
                }
            }
        })
    }

    private fun setupViewModel() {
        val repository = WallpaperRepository(RetrofitClient.apiService)
        val factory = WallpaperViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WallpaperViewModel::class.java]
    }

    private fun setupObservers() {
        viewModel.wallpapers.observe(this) { wallpapers ->
            adapter.submitList(wallpapers)
            binding.emptyView.visibility = if (wallpapers.isEmpty() && !isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModel.isLoading.observe(this) { loading ->
            isLoading = loading
            binding.swipeRefreshLayout.isRefreshing = loading && adapter.itemCount == 0
            binding.progressBar.visibility = if (loading && adapter.itemCount > 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadCuratedWallpapers()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView
        
        searchView?.apply {
            queryHint = getString(R.string.search_wallpapers)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.searchWallpapers(it)
                        clearFocus()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Optional: Implement real-time search
                    return false
                }
            })
        }
        
        return true
    }

    private fun openWallpaperDetail(photo: Photo) {
        val intent = Intent(this, WallpaperDetailActivity::class.java).apply {
            putExtra(WallpaperDetailActivity.EXTRA_PHOTO_URL, photo.src.large2x)
            putExtra(WallpaperDetailActivity.EXTRA_PHOTOGRAPHER, photo.photographer)
            putExtra(WallpaperDetailActivity.EXTRA_PHOTO_ID, photo.id)
        }
        startActivity(intent)
    }
}
