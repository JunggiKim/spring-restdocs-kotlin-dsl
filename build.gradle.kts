plugins {
    kotlin("jvm") version "2.1.21" apply false
    id("com.vanniktech.maven.publish") version "0.34.0" apply false
}

allprojects {
    group = providers.gradleProperty("GROUP").orElse("io.github.junggikim").get()
    version = providers.gradleProperty("VERSION_NAME").orElse("0.1.2-SNAPSHOT").get()

    repositories {
        mavenCentral()
    }
}
