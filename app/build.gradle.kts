plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.ocr_ktp_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ocr_ktp_project"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    implementation("com.google.firebase:firebase-ml-vision:19.0.2")
    implementation("com.google.firebase:firebase-ml-vision:24.0.3")

    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")

    implementation("com.github.marchinram:RxGallery:0.6.6")
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("org.michaelbel:bottomsheet:1.2.3")
    implementation("io.reactivex.rxjava2:rxjava:2.2.6")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("org.jetbrains.anko:anko-commons:0.10.8")
}