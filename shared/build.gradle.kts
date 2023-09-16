plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("app.cash.sqldelight") version "2.0.0-alpha05"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:android-driver:2.0.0-alpha05")
            }
        }

        val iosMain by creating {
            dependencies {
                implementation("app.cash.sqldelight:native-driver:2.0.0-alpha05")
            }

            dependsOn(commonMain)
        }

        val iosTest by creating {
            dependsOn(commonTest)
        }
    }
}


sqldelight {
    databases {
        create("ParkDatabase") {
            packageName.set("com.example.segnaposto.database")
            sourceFolders.set(listOf("sqldelight"))
        }
    }
}

android {
    namespace = "com.example.segnaposto"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}