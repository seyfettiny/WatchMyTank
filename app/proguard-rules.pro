 -keep class dagger.hilt.** { *; }
 -keep class dagger.hilt.android.internal.managers.** { *; }
 -dontwarn dagger.hilt.**

 -keep class androidx.room.** { *; }
 -keepclassmembers class * { @androidx.room.* <methods>; }
 -dontwarn androidx.room.**
 -keepattributes *Annotation*

 -keep class androidx.work.** { *; }
 -dontwarn androidx.work.**

 -keep class com.google.firebase.** { *; }
 -dontwarn com.google.firebase.**

 -dontwarn kotlinx.coroutines.**

 -keep class androidx.compose.** { *; }
 -dontwarn androidx.compose.**