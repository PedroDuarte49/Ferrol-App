plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ferrol_app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ferrol_app"
        minSdk = 25
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Material Design (Snackbar)
    implementation("com.google.android.material:material:1.11.0")
    // Conexión a internet (Retrofit)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
// Convertidor de JSON a objetos Kotlin
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
// Para mostrar la lista (RecyclerView)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}