import org.gradle.configurationcache.extensions.capitalized
import java.util.Locale

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.rustAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.test.withrust"
    compileSdk = 35

    ndkVersion = "25.2.9519653"

    defaultConfig {
        applicationId = "com.test.withrust"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

cargo {
    module  = ".."
    libname = "rustnat"
    profile = if (gradle.startParameter.taskNames.any{ it.lowercase(Locale.getDefault()).contains("debug")}) "debug" else "release"
    targets = listOf("arm64", "x86_64", "x86")
}

tasks.whenTaskAdded {
    if (this.name == "mergeDebugJniLibFolders" || this.name == "mergeReleaseJniLibFolders") {
        this.dependsOn("cargoBuild")
        this.inputs.dir(buildDir.resolve("rustJniLibs/android"))
    }
}
tasks.whenTaskAdded {
    if (this.name == "javaPreCompileDebug" || this.name == "javaPreCompileRelease") {
        this.dependsOn("cargoBuild")
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}