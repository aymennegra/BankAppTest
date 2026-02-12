import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
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
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Coroutines multiplatform
                implementation(libs.kotlinx.coroutines.core)

                // Ktor multiplatform
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)

                // Serialization multiplatform
                implementation(libs.kotlinx.serialization.json)

                // Koin multiplatform
                implementation(libs.koin.core)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.lifecycle.viewmodel.ktx)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.android)
                implementation(libs.koin.androidx.compose)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.mockk)
                implementation(libs.mockk.android)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
                implementation(libs.kotest.assertions)
                implementation(libs.koin.test)
                implementation(libs.koin.test.junit4)
            }
        }
    }
}

android {
    namespace = "com.example.bankapp"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}