# Walls - Actress Gallery App - AI Coding Agent Instructions

## Project Overview
Android wallpaper gallery app built with **100% Jetpack Compose**, Material Design 3, and MVVM architecture. Displays 8,000+ actress profiles from external API with favorites, search, and browsing functionality.

**Package:** `com.deecode.walls`
**Min SDK:** 24 | **Target SDK:** 36 | **Language:** Kotlin 2.0.21

## Architecture Pattern: MVVM + Repository

### Layer Structure
```
data/
  ├── local/          # Room: FavoriteDao, FavoriteActress, FavoriteImage entities, WallsDatabase
  ├── model/          # API response models (Actress, ActressDetail)
  ├── preferences/    # DataStore: PreferencesManager for theme
  ├── remote/         # Retrofit: ActressApiService, RetrofitInstance (singleton)
  └── repository/     # WallsRepository (bridges API + local DB)

ui/
  ├── screens/        # Top-level composables (HomeScreen, BrowseScreen, etc.)
  ├── viewmodel/      # AndroidViewModel instances (HomeViewModel, etc.)
  ├── components/     # Reusable composables (Cards, GridLayouts, StateViews)
  ├── common/         # UiState sealed class (Idle, Loading, Success, Error)
  └── theme/          # Material3 theme configuration

navigation/
  ├── Screen.kt       # Sealed class with route constants + createRoute() helpers
  └── WallsNavGraph.kt # NavHost with animated transitions
```

### Critical Conventions

**1. ViewModel Initialization Pattern**
- All ViewModels extend `AndroidViewModel(application)`
- Initialize repository in `init {}` block using singleton pattern:
```kotlin
init {
    val database = WallsDatabase.getDatabase(application)
    repository = WallsRepository(RetrofitInstance.api, database.favoriteDao())
}
```

**2. State Management with UiState**
- Use `UiState<T>` sealed class for all async operations (Idle, Loading, Success, Error)
- Expose state via `StateFlow`: `private val _state = MutableStateFlow<UiState<T>>(UiState.Idle)`
- Always use `.asStateFlow()` for public exposure

**3. Navigation with URL Encoding**
- Album/Image URLs must be URL-encoded in `createRoute()`: `java.net.URLEncoder.encode(url, "UTF-8")`
- Decode in navigation composable: `URLDecoder.decode(it, "UTF-8")`
- Example in `Screen.AlbumDetail.createRoute()`

**4. Animation Standards**
- Main screens (Home/Browse/Search/Favorites): `fadeIn/fadeOut` with 300ms tween
- Detail screens: `slideInHorizontally/slideOutHorizontally` + `fadeIn/fadeOut`
- Bottom bar: `slideInVertically/slideOutVertically` with `AnimatedVisibility`

## API Integration

**Base URL:** `https://actress-gallery-api.onrender.com/`

**Key Endpoints:**
- `GET /api/ragalahari/latest` → List<Actress>
- `GET /api/ragalahari/letter/{letter}` → List<Actress> (A-Z, no Q)
- `GET /api/actress/{actress_id}` → ActressDetail
- `GET /api/album/photos?album_url={url}` → List<String>
- `GET /api/search?query={text}&limit={number}` → List<Actress>

**Timeout Configuration:** 30s connect/read/write (see `RetrofitInstance`)
**Logging:** OkHttp interceptor with `Level.BODY` (only in debug)

## Database & Persistence

**Room Database:** `WallsDatabase` (version 1)
- **Entities:** `FavoriteActress`, `FavoriteImage`
- **DAO:** `FavoriteDao` with Flow-based queries (`getAllFavoriteActresses()`, `isFavoriteActress()`)
- **Singleton:** Use `WallsDatabase.getDatabase(context)` - thread-safe with `@Volatile` + `synchronized`

**DataStore Preferences:**
- Theme stored as `booleanPreferencesKey("dark_theme")`
- Access via `PreferencesManager(context).isDarkTheme` Flow
- Toggle with `toggleTheme()` suspend function

## Networking & Connectivity

**Network Monitoring:** `NetworkConnectivityObserver` (util/)
- Uses `ConnectivityManager.NetworkCallback` with `callbackFlow`
- Check both `NET_CAPABILITY_INTERNET` + `NET_CAPABILITY_VALIDATED`
- Observe in `WallsApp` composable to show `NoInternetView` when offline

**Permissions Required:**
- `android.permission.INTERNET`
- `android.permission.ACCESS_NETWORK_STATE`

## Build & Development Workflow

**Build Commands:**
```bash
./gradlew assembleDebug      # Build debug APK
./gradlew assembleRelease    # Build release APK (ProGuard disabled)
./gradlew clean              # Clean build artifacts
```

**Run on Device/Emulator:**
- Use Android Studio's Run button (Shift+F10)
- Or: `./gradlew installDebug` + manual launch

**Dependency Management:**
- All versions centralized in `gradle/libs.versions.toml`
- Use version catalog references: `implementation(libs.retrofit)`
- Room requires `id("kotlin-kapt")` plugin + `kapt(libs.room.compiler)`

## UI/Compose Patterns

**Composable Organization:**
- **Screens:** Top-level UI with ViewModel, handle navigation callbacks
- **Components:** Reusable elements (Cards, Grids, TopAppBar)
- **StateViews:** Loading, Error, Empty states (in components/)

**Common Patterns:**
```kotlin
// Screen structure
@Composable
fun HomeScreen(
    onActressClick: (String) -> Unit,
    onThemeToggle: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.latestGalleries.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLatestGalleries()
    }

    when (val state = uiState) {
        UiState.Loading -> LoadingView()
        is UiState.Success -> { /* render data */ }
        is UiState.Error -> ErrorView(state.message) { viewModel.loadLatestGalleries() }
    }
}
```

**Image Loading (Coil):**
- Use `AsyncImage` with crossfade, placeholder, error handling
- Lazy grids: `LazyVerticalGrid(columns = GridCells.Fixed(2))`

## Testing Structure
- Unit tests: `app/src/test/java/` (minimal boilerplate)
- Instrumented tests: `app/src/androidTest/java/` (minimal boilerplate)
- **Note:** No comprehensive test suite exists - focus on manual testing

## Known Constraints
- API hosted on free tier (Render) → expect cold start delays (10-30s)
- No offline caching for API responses (only favorites stored locally)
- ProGuard disabled in release builds (`isMinifyEnabled = false`)
- Single Activity architecture with Compose navigation

## When Adding Features
1. **New Screen:** Create sealed object in `Screen.kt`, add route to `WallsNavGraph.kt`
2. **API Endpoint:** Add to `ActressApiService`, wrap in repository, handle in ViewModel
3. **Database Entity:** Add to `@Database` entities, increment version, write migration
4. **Theme Changes:** Modify `ui/theme/` (Color.kt, Theme.kt, Type.kt)
5. **Network Call:** Always wrap in `viewModelScope.launch` + `UiState` pattern

## Code Style Notes
- Use trailing lambdas for Compose functions
- Prefer `StateFlow` over `LiveData`
- Repository returns `Result<T>` for API calls (not throwing exceptions)
- All string literals should be in `res/values/strings.xml` (though README shows hardcoded strings exist)
