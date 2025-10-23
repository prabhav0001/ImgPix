# Walls - Actress Gallery Wallpaper App

A beautiful Material Design 3 Android wallpaper app built with Jetpack Compose, featuring 8,000+ high-quality actress gallery profiles.

## 🌟 Features

### Core Features
- **Latest Galleries**: Browse the latest actress photo galleries
- **Browse A-Z**: Navigate through 8,000+ profiles alphabetically (A-Z, excluding Q)
- **Smart Search**: Real-time search with debouncing for quick actress discovery
- **Favorites System**: Save favorite profiles and individual images
- **Album Viewing**: Explore detailed photo albums for each actress
- **Profile Details**: View comprehensive information including bio, nationality, profession, and more

### Design & UX
- **Material Design 3**: Modern, beautiful UI with Material You theming
- **Dark/Light Theme**: Seamless theme switching with DataStore preferences
- **Smooth Animations**:
  - Fade transitions between main screens
  - Slide animations for detail screens
  - Animated bottom navigation bar
- **Responsive Layout**: Optimized grid layouts for different screen sizes
- **Edge-to-Edge UI**: Immersive full-screen experience

### Technical Highlights
- **MVVM Architecture**: Clean separation of concerns
- **Jetpack Compose**: 100% Compose UI
- **Kotlin Coroutines & Flow**: Asynchronous programming
- **Retrofit & OkHttp**: Network communication
- **Room Database**: Local data persistence
- **Coil**: Efficient image loading
- **Navigation Component**: Type-safe navigation
- **DataStore**: Modern preferences storage

## 🏗️ Architecture

```
app/
├── data/
│   ├── local/              # Room database entities and DAOs
│   ├── model/              # Data models
│   ├── preferences/        # DataStore preferences
│   ├── remote/             # Retrofit API service
│   └── repository/         # Repository pattern
├── navigation/             # Navigation graph and routes
├── ui/
│   ├── common/             # Common UI utilities
│   ├── components/         # Reusable composables
│   ├── screens/            # Screen composables
│   ├── theme/              # Material Theme
│   └── viewmodel/          # ViewModels
└── MainActivity.kt
```

## 📱 Screens

### 1. Home Screen
- Displays latest galleries in a 2-column grid
- Pull-to-refresh functionality
- Theme toggle in app bar
- Click to view actress details

### 2. Browse A-Z
- Horizontal scrollable letter chips (A-Z, no Q)
- Filter actresses by first letter
- Grid layout with actress cards
- Visual feedback for selected letter

### 3. Search
- Real-time search with debouncing (500ms)
- Minimum 2 characters required
- Clear button to reset search
- Grid results layout

### 4. Favorites
- Two tabs: Profiles & Images
- Save favorite actress profiles
- Save individual favorite images
- Count badges on tabs
- Remove favorites with swipe or button

### 5. Actress Detail
- Header with name, profession, nationality
- Bio information
- Gallery of images
- Album cards with thumbnails
- Favorite toggle button
- Navigate to albums

### 6. Album Detail
- Display all photos from album
- Grid layout
- Back navigation
- Click to view full image

## 🔌 API Integration

The app uses the **Actress Gallery API** hosted at:
```
https://actress-gallery-api.onrender.com/
```

### API Endpoints Used:
- `GET /api/ragalahari/latest` - Latest galleries
- `GET /api/ragalahari/letter/{letter}` - Browse by letter
- `GET /api/actress/{actress_id}` - Actress details
- `GET /api/album/photos?album_url={url}` - Album photos
- `GET /api/search?query={text}&limit={number}` - Search

## 🎨 Theme & Styling

### Color Scheme
- **Primary**: Deep Purple (#673AB7)
- **Secondary**: Teal (#009688)
- **Dynamic Colors**: Supports Android 12+ Material You

### Typography
- Material Design 3 typography scale
- Clear hierarchy for readability

### Spacing & Layout
- Consistent 16dp padding
- 12dp grid spacing
- Rounded corners (12dp radius)
- Elevation for depth

## 🛠️ Dependencies

### Core
- Kotlin 2.0.21
- Android Gradle Plugin 8.13.0
- Compose BOM 2024.09.00

### Networking
- Retrofit 2.9.0
- OkHttp 4.12.0
- Gson 2.10.1

### UI & Image Loading
- Coil Compose 2.5.0
- Material Icons Extended

### Database
- Room 2.6.1

### Navigation
- Navigation Compose 2.7.7

### Preferences
- DataStore Preferences 1.0.0

### Lifecycle & Coroutines
- Lifecycle ViewModel Compose 2.7.0
- Kotlin Coroutines 1.7.3

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 36

### Build & Run
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run on emulator or device (min SDK 24)

### Permissions Required
- `INTERNET` - Network access for API calls
- `ACCESS_NETWORK_STATE` - Check network connectivity

## 📦 Build Variants
- **Debug**: Development build with logging
- **Release**: Optimized production build

## 🎯 Future Enhancements
- [ ] Image viewer with pinch-to-zoom
- [ ] Download wallpapers
- [ ] Set as wallpaper feature
- [ ] Share images
- [ ] Offline mode
- [ ] Image filters
- [ ] Collections/Categories
- [ ] Dark/Light/Auto theme options
- [ ] Grid size preferences (2/3 columns)
- [ ] Image quality settings

## 📄 License
This project is for educational purposes.

## 🙏 Credits
- API: [Actress Gallery API](https://actress-gallery-api.onrender.com/)
- Data Source: Ragalahari.com
- Material Design: Google

## 🐛 Known Issues
- API may have cold start delays (hosted on free tier)
- Some images may load slowly depending on network

## 📧 Contact
For questions or feedback, please open an issue.

---

**Note**: This app is designed for learning purposes and demonstrates modern Android development practices with Jetpack Compose, MVVM architecture, and Material Design 3.
