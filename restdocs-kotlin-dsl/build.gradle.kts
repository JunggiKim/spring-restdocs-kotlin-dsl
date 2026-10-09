plugins {
    kotlin("jvm")
    `java-library`
    id("com.vanniktech.maven.publish")
}

extra["POM_ARTIFACT_ID"] = "restdocs-kotlin-dsl"

dependencies {
    api("org.springframework.restdocs:spring-restdocs-core:3.0.1")

    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}
