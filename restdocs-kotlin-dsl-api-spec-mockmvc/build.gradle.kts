plugins {
    kotlin("jvm")
    `java-library`
    id("com.vanniktech.maven.publish")
}

extra["POM_ARTIFACT_ID"] = "restdocs-kotlin-dsl-api-spec-mockmvc"

dependencies {
    api(project(":restdocs-kotlin-dsl-mockmvc"))
    api("com.epages:restdocs-api-spec-mockmvc:0.19.4")
}

kotlin {
    jvmToolchain(17)
}
