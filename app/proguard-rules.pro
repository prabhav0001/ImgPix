# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# --- Gson & Retrofit Rules ---
# Gson uses reflection to serialize/deserialize, so we need to keep the model classes
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.deecode.walls.data.model.** { *; }
-keep class com.deecode.walls.data.local.** { *; }

# Retrofit & Gson
# Note: Modern versions of Retrofit and Gson include their own consumer ProGuard rules.
# We do not need to explicitly keep their packages (which causes "overly broad keep rule" warnings).
# If you face issues, ensure you are using the latest versions or add specific rules.

# --- Coroutines ---
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.android.AndroidExceptionPreHandler {
    <init>();
}

# --- Room ---
-keep class androidx.room.RoomDatabase { *; }
-keep class * extends androidx.room.RoomDatabase

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile