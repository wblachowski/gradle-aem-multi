import com.cognifide.gradle.aem.pkg.tasks.Compose

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.cognifide.aem.bundle")
}

description = "Example - AEM Application Core"

dependencies {
    implementation(project(":aem:app.common"))
}

aem {
    tasks {
        bundle("author")
        bundle("publish")
    }
}
