# API Configuration Guide

## Getting Your Pexels API Key

### Step 1: Create Account
1. Visit: https://www.pexels.com/
2. Click "Join" or "Sign Up" (top right corner)
3. Fill in your details or use Google/Facebook login
4. Verify your email address

### Step 2: Access API Section
1. Once logged in, go to: https://www.pexels.com/api/
2. You'll see the API documentation page
3. Your API key should be visible on this page

### Step 3: Get API Key
1. Look for "Your API Key" section
2. Click to reveal your API key
3. Copy the entire key (it will be a long string of letters and numbers)

Example format: `YOUR_KEY_HERE_123456789abcdefghijklmnop`

### Step 4: Add to Project

Open: `app/build.gradle.kts`

Find this line (around line 19):
```kotlin
buildConfigField("String", "PEXELS_API_KEY", "\"YOUR_PEXELS_API_KEY\"")
```

Replace with:
```kotlin
buildConfigField("String", "PEXELS_API_KEY", "\"abc123xyz456yourkey\"")
```

**IMPORTANT NOTES:**
- Keep the escaped quotes `\"` 
- Don't share your API key publicly
- Don't commit the API key to version control
- The app won't work without a valid API key

### Step 5: Sync Gradle
1. After adding your API key, click "Sync Now" in Android Studio
2. Wait for Gradle sync to complete
3. Build and run your app

## API Usage Limits (Free Tier)

- **Per Hour**: 200 requests
- **Per Month**: 20,000 requests

The app is designed to work within these limits through:
- Pagination (30 photos per request)
- Local caching
- Efficient loading

## Troubleshooting

### Error: "401 Unauthorized"
- Your API key is invalid or missing
- Check if you copied the entire key
- Verify on Pexels website that your key is active

### Error: "429 Too Many Requests"
- You've exceeded the rate limit
- Wait for the hourly reset
- Consider implementing more aggressive caching

### App shows no wallpapers
- Check internet connection
- Verify API key is correct
- Check Android Logcat for error messages
- Try: `adb logcat | grep Pexels`

## Security Best Practices

1. **Don't hardcode in source files**: Use BuildConfig (as we do)
2. **Don't commit to Git**: Add to .gitignore
3. **Use environment variables** for production
4. **Regenerate if exposed**: If you accidentally share your key

## Alternative API Key Storage (Advanced)

For production apps, consider:

### Option 1: local.properties
```properties
# local.properties (not tracked by Git)
PEXELS_API_KEY=your_key_here
```

Then in build.gradle.kts:
```kotlin
val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

buildConfigField("String", "PEXELS_API_KEY", 
    "\"${localProperties.getProperty("PEXELS_API_KEY")}\"")
```

### Option 2: Environment Variables
```bash
export PEXELS_API_KEY="your_key_here"
```

Then in build.gradle.kts:
```kotlin
buildConfigField("String", "PEXELS_API_KEY", 
    "\"${System.getenv("PEXELS_API_KEY")}\"")
```

## Need Help?

- Pexels API Documentation: https://www.pexels.com/api/documentation/
- Pexels Support: help@pexels.com
- Check the main README.md for more information

---

Keep your API key secure! 🔐
