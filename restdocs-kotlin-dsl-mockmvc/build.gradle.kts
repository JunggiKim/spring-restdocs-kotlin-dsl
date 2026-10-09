plugins {
    kotlin("jvm")
    `java-library`
    id("com.vanniktech.maven.publish")
}

extra["POM_ARTIFACT_ID"] = "restdocs-kotlin-dsl-mockmvc"

dependencies {
    api(project(":restdocs-kotlin-dsl"))
    api("org.springframework.restdocs:spring-restdocs-mockmvc:3.0.1")
    compileOnly("org.springframework:spring-test:6.2.1")
    compileOnly("org.springframework:spring-webmvc:6.2.1")

    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}
