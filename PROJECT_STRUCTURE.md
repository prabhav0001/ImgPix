# Complete Project Structure

```
Wall/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/wallpaper/app/
│   │       │   ├── data/
│   │       │   │   ├── api/
│   │       │   │   │   ├── PexelsApiService.kt      # Retrofit API interface
│   │       │   │   │   └── RetrofitClient.kt        # Retrofit singleton instance
│   │       │   │   ├── model/
│   │       │   │   │   └── PexelsModels.kt          # Data models (Photo, PhotoSource, PexelsResponse)
│   │       │   │   └── repository/
│   │       │   │       └── WallpaperRepository.kt   # Repository for data operations
│   │       │   └── ui/
│   │       │       ├── adapter/
│   │       │       │   └── WallpaperAdapter.kt      # RecyclerView adapter
│   │       │       ├── viewmodel/
│   │       │       │   ├── WallpaperViewModel.kt    # ViewModel for business logic
│   │       │       │   └── WallpaperViewModelFactory.kt
│   │       │       ├── MainActivity.kt              # Main screen with wallpaper grid
│   │       │       └── WallpaperDetailActivity.kt   # Detail screen with set wallpaper
│   │       ├── res/
│   │       │   ├── drawable/
│   │       │   │   └── gradient_overlay.xml         # Gradient for image overlays
│   │       │   ├── layout/
│   │       │   │   ├── activity_main.xml            # Main activity layout
│   │       │   │   ├── activity_wallpaper_detail.xml # Detail activity layout
│   │       │   │   └── item_wallpaper.xml           # RecyclerView item layout
│   │       │   ├── menu/
│   │       │   │   ├── menu_main.xml                # Main screen menu with search
│   │       │   │   └── menu_detail.xml              # Detail screen menu
│   │       │   ├── values/
│   │       │   │   ├── colors.xml                   # Color definitions
│   │       │   │   ├── strings.xml                  # String resources
│   │       │   │   └── themes.xml                   # App themes
│   │       │   └── xml/
│   │       │       ├── backup_rules.xml             # Backup configuration
│   │       │       └── data_extraction_rules.xml    # Data extraction config
│   │       └── AndroidManifest.xml                  # App manifest with permissions
│   ├── build.gradle.kts                             # App-level Gradle config
│   └── proguard-rules.pro                           # ProGuard rules
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties                # Gradle wrapper config
├── build.gradle.kts                                 # Project-level Gradle config
├── settings.gradle.kts                              # Gradle settings
├── gradle.properties                                # Gradle properties
├── .gitignore                                       # Git ignore file
├── README.md                                        # Main documentation
└── SETUP_GUIDE.md                                   # Quick setup guide
```

## File Descriptions

### Kotlin Source Files

#### Data Layer
- **PexelsModels.kt**: Defines data classes for API responses (Photo, PhotoSource, PexelsResponse)
- **PexelsApiService.kt**: Retrofit interface defining API endpoints
- **RetrofitClient.kt**: Singleton object providing Retrofit instance
- **WallpaperRepository.kt**: Repository pattern implementation for data operations

#### UI Layer
- **MainActivity.kt**: Main screen showing wallpaper grid with search functionality
- **WallpaperDetailActivity.kt**: Detail screen for viewing and setting wallpapers
- **WallpaperAdapter.kt**: RecyclerView adapter for displaying wallpaper grid
- **WallpaperViewModel.kt**: ViewModel managing UI state and business logic
- **WallpaperViewModelFactory.kt**: Factory for creating ViewModel instances

### Layout Files

- **activity_main.xml**: Main screen layout with toolbar, RecyclerView, and SwipeRefreshLayout
- **activity_wallpaper_detail.xml**: Detail screen layout with large image view and action buttons
- **item_wallpaper.xml**: Grid item layout for individual wallpaper thumbnails

### Resource Files

- **strings.xml**: All app strings for easy localization
- **colors.xml**: Color palette definitions
- **themes.xml**: Material Design theme configuration
- **menu_main.xml**: Search action in main screen
- **menu_detail.xml**: Download action in detail screen
- **gradient_overlay.xml**: Gradient drawable for image overlays

### Configuration Files

- **AndroidManifest.xml**: App configuration, activities, and permissions
- **build.gradle.kts**: Dependencies and build configuration
- **gradle.properties**: Gradle build properties
- **proguard-rules.pro**: Code obfuscation rules for release builds

## Key Features Implementation

### 1. Network Layer
- Uses Retrofit for REST API calls
- OkHttp for network operations
- Gson for JSON parsing
- API key authentication via headers

### 2. Image Loading
- Glide library for efficient image loading
- Placeholder and error handling
- Smooth transitions
- Memory and disk caching

### 3. Architecture
- MVVM pattern for clean separation of concerns
- Repository pattern for data access
- LiveData for reactive UI updates
- Kotlin Coroutines for async operations

### 4. UI Components
- Material Design Components
- RecyclerView with GridLayoutManager
- SwipeRefreshLayout for pull-to-refresh
- SearchView for wallpaper search
- Infinite scroll with pagination

### 5. Permissions
- Internet access for API calls
- Network state checking
- Set wallpaper permission

## Dependencies

- AndroidX Core & AppCompat
- Material Design Components
- Lifecycle & ViewModel
- Kotlin Coroutines
- Retrofit & Gson
- OkHttp Logging Interceptor
- Glide Image Loader
- RecyclerView
- SwipeRefreshLayout

## Build Variants

- **Debug**: Development build with logging enabled
- **Release**: Production build with ProGuard enabled

## Minimum Requirements

- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Compile SDK: 34

---

This structure follows Android best practices and modern architecture patterns.
