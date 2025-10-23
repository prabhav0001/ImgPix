# Android Wallpaper App

A beautiful Android wallpaper application built with Kotlin that fetches high-quality wallpapers from Pexels.com (free/open source).

## Features

- 📱 Browse curated high-quality wallpapers from Pexels
- 🔍 Search wallpapers by keywords
- 🖼️ Set any wallpaper as your device wallpaper
- 📤 Share wallpapers with friends
- ♾️ Infinite scroll with pagination
- 🔄 Pull-to-refresh functionality
- 📲 Material Design UI
- 🎨 Grid layout for beautiful presentation

## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Network**: Retrofit + OkHttp
- **Image Loading**: Glide
- **Async Operations**: Kotlin Coroutines
- **UI**: Material Design Components
- **API**: Pexels API

## Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24+
- Pexels API Key (free)

## Setup Instructions

### 1. Get Pexels API Key

1. Visit [Pexels API](https://www.pexels.com/api/)
2. Sign up for a free account
3. Generate your API key

### 2. Configure the Project

1. Clone this repository:
   ```bash
   git clone <your-repo-url>
   cd Wall
   ```

2. Open the project in Android Studio

3. Add your Pexels API key in `app/build.gradle.kts`:
   ```kotlin
   buildConfigField("String", "PEXELS_API_KEY", "\"YOUR_PEXELS_API_KEY_HERE\"")
   ```
   Replace `YOUR_PEXELS_API_KEY_HERE` with your actual API key.

### 3. Build and Run

1. Sync the project with Gradle files
2. Connect your Android device or start an emulator
3. Click Run or press Shift+F10

## Project Structure

```
app/
├── src/main/
│   ├── java/com/wallpaper/app/
│   │   ├── data/
│   │   │   ├── api/              # Retrofit API service
│   │   │   ├── model/            # Data models
│   │   │   └── repository/       # Repository layer
│   │   └── ui/
│   │       ├── adapter/          # RecyclerView adapters
│   │       ├── viewmodel/        # ViewModels
│   │       ├── MainActivity.kt   # Main screen
│   │       └── WallpaperDetailActivity.kt  # Detail screen
│   ├── res/
│   │   ├── layout/               # XML layouts
│   │   ├── menu/                 # Menu resources
│   │   ├── values/               # Strings, colors, themes
│   │   └── drawable/             # Drawable resources
│   └── AndroidManifest.xml
└── build.gradle.kts
```

## Usage

### Browse Wallpapers
- Launch the app to see curated wallpapers
- Scroll down to load more wallpapers automatically
- Pull down to refresh the wallpaper list

### Search Wallpapers
- Tap the search icon in the toolbar
- Enter keywords (e.g., "nature", "abstract", "city")
- Press enter to search

### Set Wallpaper
- Tap any wallpaper to view details
- Click "Set Wallpaper" button
- Wait for confirmation

### Share Wallpaper
- Open wallpaper details
- Click "Share" button
- Choose your preferred sharing method

## Permissions

The app requires the following permissions:
- `INTERNET` - To fetch wallpapers from Pexels API
- `ACCESS_NETWORK_STATE` - To check network connectivity
- `SET_WALLPAPER` - To set wallpapers on the device

## API Rate Limits

Pexels API free tier includes:
- 200 requests per hour
- 20,000 requests per month

The app is optimized to minimize API calls through pagination and caching.

## Future Enhancements

- [ ] Download wallpapers to device
- [ ] Favorite wallpapers
- [ ] Local database for offline access
- [ ] Different wallpaper categories
- [ ] Set wallpaper for lock screen and home screen separately
- [ ] Dark mode support
- [ ] Wallpaper collections

## Screenshots

(Add screenshots of your app here)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- [Pexels](https://www.pexels.com/) for providing free high-quality photos
- All the amazing photographers on Pexels

## Contact

For questions or suggestions, please open an issue on GitHub.

---

Made with ❤️ using Kotlin
