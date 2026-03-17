# Keep Retrofit interfaces
-keep interface retrofit2.** { *; }

# Gson / serialization DTO safety
-keep class com.stockapp.data.remote.dto.** { *; }

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Room
-keep class androidx.room.** { *; }

# Kotlin coroutines metadata
-keep class kotlinx.coroutines.** { *; }

# Keep model names if needed for JSON reflection
-keepnames class com.stockapp.domain.model.** { *; }
