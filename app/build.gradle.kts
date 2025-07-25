import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)
}

android {
    namespace = "ru.topbun.minecraft_mods_pe"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.youlovehamit.survival"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val appKeyMetric = property("metric_key")?.toString() ?: error("Not found appKeyMetric in properties")
        buildConfigField("String", "METRIC_KEY", "\"$appKeyMetric\"")

        val applovinSdkKey = property("applovin_sdk_key")?.toString() ?: error("Not found applovin_sdk_key in properties")
        buildConfigField("String", "APPLOVIN_SDK_KEY", "\"$applovinSdkKey\"")

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

dependencies {

    implementation(libs.voyager.navigator)

    implementation(libs.app.update.ktx)
    implementation(libs.review)
    implementation(libs.play.services.ads)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.kotlinx.serialization.json)

    // Ads
    implementation (libs.mobileads.yandex)
    implementation(libs.applovin.sdk)
    implementation(libs.analytics)
    implementation(libs.google.adapter)
    implementation(libs.facebook.adapter)
    implementation(libs.mintegral.adapter)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":features:main"))
    implementation(project(":features:splash"))
    implementation(project(":features:detail_mod"))
    implementation(project(":features:favorite"))
    implementation(project(":features:feedback"))
    implementation(project(":features:instruction"))
    implementation(project(":features:tabs"))

    implementation(project(":navigation"))
    implementation(project(":domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:android"))
    implementation(project(":data"))

}
