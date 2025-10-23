# Quick Setup Guide

## Step-by-Step Instructions

### 1. Get Your Pexels API Key

1. Go to https://www.pexels.com/api/
2. Click "Get Started" or "Sign Up"
3. Create a free account
4. Once logged in, you'll see your API key on the dashboard
5. Copy your API key

### 2. Add API Key to the Project

Open the file: `app/build.gradle.kts`

Find this line (around line 19):
```kotlin
buildConfigField("String", "PEXELS_API_KEY", "\"YOUR_PEXELS_API_KEY\"")
```

Replace `YOUR_PEXELS_API_KEY` with your actual API key:
```kotlin
buildConfigField("String", "PEXELS_API_KEY", "\"abc123xyz456\"")
```

**Important:** Keep the quotes and backslashes exactly as shown!

### 3. Open Project in Android Studio

1. Launch Android Studio
2. Click "Open" or "Open an existing project"
3. Navigate to the "Wall" folder
4. Click "OK"
5. Wait for Gradle to sync (this may take a few minutes)

### 4. Run the App

**Option A: Using an Emulator**
1. In Android Studio, click "Device Manager" (phone icon)
2. Create a new virtual device if you don't have one
3. Click the green "Run" button (or press Shift+F10)
4. Select your emulator
5. Wait for the app to install and launch

**Option B: Using a Real Device**
1. Enable "Developer Options" on your Android phone:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
2. Enable "USB Debugging" in Developer Options
3. Connect your phone via USB
4. Click the green "Run" button in Android Studio
5. Select your device
6. Click "OK"

### 5. Common Issues and Solutions

**Issue: "Gradle sync failed"**
- Solution: Make sure you have a stable internet connection
- Try: File → Invalidate Caches → Invalidate and Restart

**Issue: "SDK not found"**
- Solution: File → Project Structure → SDK Location
- Set Android SDK location (usually ~/Android/Sdk on Linux)

**Issue: "API key error"**
- Solution: Double-check your API key in app/build.gradle.kts
- Make sure the quotes and backslashes are correct

**Issue: "No wallpapers loading"**
- Solution: Check your internet connection
- Verify your API key is active on Pexels.com
- Check Android Logcat for error messages

### 6. Testing the App

1. **Browse wallpapers**: Scroll through the main screen
2. **Search**: Tap the search icon and try "nature" or "ocean"
3. **View details**: Tap any wallpaper
4. **Set wallpaper**: Click "Set Wallpaper" button
5. **Share**: Click "Share" button to share with friends

### Need Help?

- Check the main README.md for detailed documentation
- Open an issue on GitHub
- Check Logcat in Android Studio for error messages

---

Happy wallpaper browsing! 🎨
