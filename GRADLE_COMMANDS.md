# Useful Gradle Commands

This file contains helpful Gradle commands for building and managing your Android app.

## Build Commands

### Clean Build
```bash
./gradlew clean
```
Removes all build artifacts and temporary files.

### Build Debug APK
```bash
./gradlew assembleDebug
```
Builds a debug APK located at: `app/build/outputs/apk/debug/app-debug.apk`

### Build Release APK
```bash
./gradlew assembleRelease
```
Builds a release APK (requires signing configuration).

### Install Debug APK
```bash
./gradlew installDebug
```
Builds and installs the debug APK on a connected device/emulator.

### Build and Run
```bash
./gradlew installDebug && adb shell am start -n com.wallpaper.app/.ui.MainActivity
```
Builds, installs, and launches the app.

## Testing Commands

### Run Unit Tests
```bash
./gradlew test
```

### Run Tests with Coverage
```bash
./gradlew testDebugUnitTest
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## Code Quality Commands

### Lint Check
```bash
./gradlew lint
```
Runs Android Lint to find potential bugs and optimization improvements.

### Lint Report
```bash
./gradlew lintDebug
```
Generates a detailed lint report at: `app/build/reports/lint-results-debug.html`

## Dependency Commands

### View Dependencies
```bash
./gradlew app:dependencies
```
Shows the entire dependency tree.

### Check for Updates
```bash
./gradlew dependencyUpdates
```
(Requires gradle-versions-plugin)

## Cleaning Commands

### Clean and Build
```bash
./gradlew clean build
```

### Clean Build Cache
```bash
./gradlew cleanBuildCache
```

## Debug Commands

### View Build Configuration
```bash
./gradlew app:properties
```

### Show Tasks
```bash
./gradlew tasks
```
Lists all available Gradle tasks.

### Debug Build Info
```bash
./gradlew app:signingReport
```
Shows signing configuration info.

## Useful ADB Commands

### Install APK Manually
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Uninstall App
```bash
adb uninstall com.wallpaper.app
```

### View Logs
```bash
adb logcat
```

### Filter Logs for App
```bash
adb logcat | grep "WallpaperApp"
```

### View Connected Devices
```bash
adb devices
```

### Clear App Data
```bash
adb shell pm clear com.wallpaper.app
```

### Take Screenshot
```bash
adb shell screencap /sdcard/screenshot.png
adb pull /sdcard/screenshot.png
```

## Performance Commands

### Profile App
```bash
./gradlew --profile assembleDebug
```
Generates a build performance report.

### Build with Info
```bash
./gradlew assembleDebug --info
```
Shows detailed build information.

### Build with Debug Output
```bash
./gradlew assembleDebug --debug
```
Shows very detailed debug information.

## Troubleshooting Commands

### Refresh Dependencies
```bash
./gradlew --refresh-dependencies
```

### Stop Gradle Daemon
```bash
./gradlew --stop
```

### Build with Stacktrace
```bash
./gradlew assembleDebug --stacktrace
```

### Full Rebuild
```bash
./gradlew clean && ./gradlew build --refresh-dependencies
```

## Tips

1. **Faster Builds**: Add to `gradle.properties`:
   ```properties
   org.gradle.parallel=true
   org.gradle.caching=true
   org.gradle.configureondemand=true
   ```

2. **Offline Mode**: Use `--offline` flag when no internet:
   ```bash
   ./gradlew assembleDebug --offline
   ```

3. **Daemon Info**: Check daemon status:
   ```bash
   ./gradlew --status
   ```

4. **Memory Issues**: Increase heap size in `gradle.properties`:
   ```properties
   org.gradle.jvmargs=-Xmx4096m
   ```

## Common Issues

### "Permission denied" on gradlew
```bash
chmod +x gradlew
```

### "SDK location not found"
Create `local.properties`:
```properties
sdk.dir=/home/yourusername/Android/Sdk
```

### Gradle sync fails
```bash
./gradlew --stop
rm -rf ~/.gradle/caches/
./gradlew clean build
```

---

For more information, visit: https://docs.gradle.org/current/userguide/command_line_interface.html
