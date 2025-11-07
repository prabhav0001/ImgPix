# 🚀 Play Store Release Guide - Walls App

## ✅ Pre-Release Checklist Completed

- [x] Production keystore created
- [x] Signing configuration updated
- [x] Code minification enabled
- [x] Resource shrinking enabled
- [x] ProGuard rules configured
- [x] usesCleartextTraffic removed
- [x] Keystore files added to .gitignore

---

## 📋 **STEP-BY-STEP PLAY CONSOLE SETUP**

### **1. App Details (Create App Page)**

After clicking "Create app", fill in:

**App name:** `Walls - Actress Gallery` (or your preferred name)

**Default language:** Select your language (e.g., English (United States))

**App or game:** Select "App"

**Free or paid:** Select "Free"

**Declarations:**
- ✅ Check "I declare that this app complies with Google Play's Developer Program Policies"
- ✅ Check "I declare that this app complies with US export laws"

Click **Create app**

---

### **2. Set Up Your App**

You'll see a dashboard with tasks to complete:

#### **A. Privacy Policy** 🔒
1. Click "Privacy policy"
2. You MUST provide a privacy policy URL
3. Create one using a generator like:
   - https://app-privacy-policy-generator.nisrulz.com/
   - https://www.freeprivacypolicy.com/

**What to include:**
- Data collected: None (if you don't collect personal data)
- Third-party services: Mention the API you use
- Internet permission usage

**Example Privacy Policy Template:**
```
Privacy Policy for Walls App

This app does not collect, store, or share any personal user data.

Internet Permission:
- Used to fetch actress gallery images from our API
- No user data is transmitted

Network State Permission:
- Used to check internet connectivity for better user experience

Third-party Services:
- Actress Gallery API (actress-gallery-api-production.up.railway.app)
- Image hosting services

Contact: [Your Email]
Last Updated: November 7, 2025
```

4. Host this on GitHub Pages, your website, or use a privacy policy hosting service
5. Enter the URL in Play Console

#### **B. App Access** 🔓
1. Click "App access"
2. Select "All or some functionality is restricted"
3. Add instructions: "No login required. All features are freely accessible."
4. Click **Save**

#### **C. Ads** 📢
1. Click "Ads"
2. Select:
   - **"No, my app does not contain ads"** (if you don't have ads)
   - OR **"Yes, my app contains ads"** (if you plan to add AdMob later)
3. Click **Save**

#### **D. Content Rating** 🔞
1. Click "Content rating"
2. Fill out questionnaire:
   - Email: Your contact email
   - Category: "Photo & video"
3. Answer questions honestly:
   - Violence: NO
   - Sexual content: **YES** (you have actress photos - mark as mild)
   - Profanity: NO
   - Controlled substances: NO
   - Hate speech: NO
   - Gambling: NO
   - User interaction: NO (if no chat/forums)
4. Click **Save** → **Submit**
5. You'll get ratings (likely PEGI 3 / ESRB Everyone)

#### **E. Target Audience** 👥
1. Click "Target audience and content"
2. Select "Target age" → **13+** or **18+** (for actress content)
3. "Store listing appeal" → Select **Older users** (not targeting children)
4. Click **Save**

#### **F. News App** 📰
1. Click "News app"
2. Select **"No, my app is not a news app"**
3. Click **Save**

#### **G. COVID-19 Contact Tracing** 🦠
1. Click "COVID-19 contact tracing"
2. Select **"This is not a contact tracing app"**
3. Click **Save**

#### **H. Data Safety** 🛡️
1. Click "Data safety"
2. **Does your app collect or share user data?** → Select **"No"**
3. **Data security:**
   - Data is encrypted in transit: **YES** (HTTPS API)
   - Users can request data deletion: **NO** (no data collected)
4. Click **Save** → **Submit**

---

### **3. Build Your Release APK/AAB** 📦

**IMPORTANT:** First, update your `keystore.properties` file with the passwords you created in Step 1.

#### **Option A: Build AAB (Recommended for Play Store)**

```powershell
# Navigate to project
cd C:\Users\Dev\Documents\ImgPix

# Clean build
.\gradlew clean

# Build release AAB
.\gradlew bundleRelease
```

**Output location:** `app\build\outputs\bundle\release\app-release.aab`

#### **Option B: Build APK (Alternative)**

```powershell
# Build release APK
.\gradlew assembleRelease
```

**Output location:** `app\build\outputs\apk\release\app-release.apk`

⚠️ **If build fails with signing errors:**
- Double-check `keystore.properties` passwords
- Ensure keystore file is at `keystore/walls-release.jks`

---

### **4. Store Listing** 🎨

Click **Main store listing** in the left menu:

#### **App Details:**
- **App name:** Walls - Actress Gallery
- **Short description (80 chars max):**
  ```
  Browse 8,000+ actress galleries with beautiful Material You design
  ```

- **Full description (4000 chars max):**
  ```
  Discover stunning actress galleries with Walls, a modern Android app featuring:

  ✨ Features:
  • 8,000+ high-quality actress photo galleries
  • Browse latest galleries
  • Search and filter A-Z
  • Save favorite profiles & images
  • Beautiful Material You design
  • Dark/Light theme support
  • Fast image loading
  • Smooth animations

  📱 Modern Design:
  Built with 100% Jetpack Compose and Material Design 3, Walls provides a seamless, beautiful experience with dynamic color theming that adapts to your device.

  🔍 Easy Discovery:
  • Browse latest galleries on home screen
  • Alphabetical browsing A-Z
  • Real-time search functionality
  • Detailed actress profiles

  ❤️ Favorites:
  Save your favorite actress profiles and individual images for quick access.

  🎨 Themes:
  Toggle between beautiful light and dark themes, with Material You dynamic colors on Android 12+.

  Perfect for wallpaper enthusiasts and photography lovers!
  ```

#### **App Icon:**
- Upload your app icon (512x512 PNG)
- Location: `app\src\main\res\mipmap-xxxhdpi\ic_launcher.png` (you'll need to resize to 512x512)

#### **Feature Graphic:**
- Create a 1024x500 banner image
- Use Canva or Photoshop
- Include app name and key features
- Required for featured placement

#### **Screenshots (REQUIRED):**
You need **at least 2 screenshots** for phone:

**How to capture:**
1. Run app on emulator or device
2. Take screenshots of:
   - Home screen with galleries
   - Browse A-Z screen
   - Actress detail screen
   - Album view
   - Search screen
   - Favorites screen
   - Dark theme example

3. Use Android Studio's screenshot tool or:
   ```powershell
   adb shell screencap -p /sdcard/screenshot.png
   adb pull /sdcard/screenshot.png
   ```

**Screenshot requirements:**
- Format: PNG or JPEG
- Dimensions: 16:9 or 9:16 aspect ratio
- Min: 320px
- Max: 3840px
- Upload at least 2, up to 8

#### **Optional Assets:**
- **Phone screenshots:** Upload 2-8 phone screenshots
- **7-inch tablet screenshots:** Optional (if supporting tablets)
- **10-inch tablet screenshots:** Optional
- **Wear OS screenshots:** Not applicable

#### **Categorization:**
- **App category:** Photography
- **Tags:** wallpapers, actress, gallery, photos, images

#### **Contact Details:**
- **Email:** Your support email
- **Website:** Optional (your website or GitHub repo)
- **Phone:** Optional

#### **External Marketing (Optional):**
- Promo video: Optional YouTube link

Click **Save**

---

### **5. Production Release** 🎯

#### **Countries & Regions:**
1. Click **Countries/regions**
2. Select **Available in all countries** or choose specific countries
3. Click **Save**

#### **Create Release:**
1. Go to **Production** (left menu)
2. Click **Create new release**
3. Upload your AAB/APK:
   - Drag and drop `app-release.aab` into the upload area
4. **Release name:** `1.0` (auto-filled from versionName)
5. **Release notes (What's new):**
   ```
   🎉 Initial Release

   • Browse 8,000+ actress galleries
   • Search and filter functionality
   • Favorites system
   • Beautiful Material You design
   • Dark/Light theme support
   • Smooth performance
   ```

6. Click **Save**
7. Click **Review release**

---

### **6. Review & Publish** ✅

1. Review all sections - ensure all have green checkmarks
2. Fix any warnings (yellow) or errors (red)
3. Click **Start rollout to Production**
4. Confirm the rollout

---

## ⏰ **What Happens Next?**

1. **Review process:** Google will review your app (usually 1-3 days, can take up to 7 days)
2. **Possible outcomes:**
   - ✅ **Approved:** App goes live on Play Store
   - ❌ **Rejected:** You'll receive reasons and can fix and resubmit

3. **Common rejection reasons:**
   - Privacy policy issues
   - Content rating mismatch
   - Missing screenshots
   - Functionality issues
   - Policy violations

4. **Once approved:**
   - App appears on Play Store within hours
   - Users can search and download
   - You'll receive analytics in Play Console

---

## 🔄 **Future Updates**

When you need to release an update:

1. Update version in `build.gradle.kts`:
   ```kotlin
   versionCode = 2  // Increment by 1
   versionName = "1.1"  // Update version name
   ```

2. Build new AAB:
   ```powershell
   .\gradlew bundleRelease
   ```

3. Go to **Production** → **Create new release**
4. Upload new AAB
5. Add release notes describing changes
6. Review and publish

---

## 🐛 **Testing Before Release**

**Test the release build locally:**

```powershell
# Install release APK on device
.\gradlew installRelease

# Or manually install AAB using bundletool:
# Download bundletool from: https://github.com/google/bundletool/releases

# Generate APKs from AAB
java -jar bundletool.jar build-apks --bundle=app-release.aab --output=app.apks --mode=universal

# Extract and install
java -jar bundletool.jar install-apks --apks=app.apks
```

**Test checklist:**
- ✅ App launches successfully
- ✅ API calls work (internet required)
- ✅ Images load properly
- ✅ Navigation works smoothly
- ✅ Search functionality
- ✅ Favorites save/load
- ✅ Theme toggle works
- ✅ No crashes or errors
- ✅ ProGuard hasn't broken anything

---

## 📞 **Support & Resources**

- **Play Console:** https://play.google.com/console
- **Policy Center:** https://play.google.com/about/developer-content-policy/
- **App Signing Help:** https://support.google.com/googleplay/android-developer/answer/9842756
- **Bundletool:** https://developer.android.com/tools/bundletool

---

## 🔐 **IMPORTANT: Backup Your Keystore!**

⚠️ **CRITICAL:** If you lose your keystore, you can NEVER update your app!

**Backup locations:**
1. Copy `keystore/walls-release.jks` to a secure cloud storage
2. Save `keystore.properties` passwords in a password manager
3. Store in multiple secure locations

**What to backup:**
- ✅ `keystore/walls-release.jks`
- ✅ Keystore password
- ✅ Key alias (walls-key)
- ✅ Key password

---

## 📊 **Optional: Enable App Signing by Google**

Google can manage your app signing key for added security:

1. In Play Console → **Setup** → **App signing**
2. Follow instructions to upload or generate signing key
3. Google will handle signing, you upload unsigned AABs

Benefits:
- Recovery if you lose upload key
- Better security
- Automatic optimization

---

Good luck with your release! 🎉
