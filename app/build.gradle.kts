plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    // 1. 应用插件
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.hjaquaculture"
    compileSdk {
        version = release(36)
    }
    buildFeatures {
        compose = true
    }
    defaultConfig {
        applicationId = "com.hjaquaculture"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

kotlin {
    // 设置 Kotlin 编译器使用的 JVM 版本
    jvmToolchain(11) // 17 表示 Java 17，您也可以使用 8 (即 1.8), 11 等。
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.material3.window.size.class1)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.material)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.androidx.compose.material3.icons.extended)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // 1. Room 基础配置
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // 编译时依赖，使用 KSP

    // 2. 单元测试配置 (JUnit 4)
    testImplementation(libs.kotlinx.coroutines.test)

    // 3. Android 设备测试配置 (Instrumented tests)
    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    //单独引用图标文件
    implementation(libs.material.icons.core)

    implementation(libs.androidx.navigation.compose)

    // Paging3 引用在 toml 文件中定义的依赖
    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.androidx.paging.common) // 用于测试
    // 按需引用其他可选模块
    // implementation(libs.androidx.paging.rxjava2)
    // implementation(libs.androidx.paging.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // 可选的依赖（如配合Compose使用）
    implementation(libs.hilt.navigation.compose)

    implementation(libs.kotlinx.serialization.json)

}