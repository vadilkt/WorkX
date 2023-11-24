buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:4.0.0")
        classpath("com.google.gms:google-services:4.4.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}
val buildToolsVersion by extra("33.0.1")
repositories {
}

