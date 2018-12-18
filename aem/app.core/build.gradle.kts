import com.cognifide.gradle.aem.pkg.tasks.Compose

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.cognifide.aem.bundle")
}

description = "Example - AEM Application Core"

dependencies {
    implementation(project(":aem:app.common"))
}


configure<JavaPluginConvention> {
    sourceSets {
        create("author") {
            compileClasspath += this@sourceSets["main"].compileClasspath
        }
    }
}

tasks {
    register("jarAuthor", Jar::class.java) {
        val convention = (project.convention.getPluginByName("java") as JavaPluginConvention)

        from(convention.sourceSets.getByName("author").output)
    }
}

aem {
    bundle("jarAuthor") {
        jar.classifier = "author"
        javaPackage = "com.company.example.aem.app.core.author"
    }
}

tasks {
    named<Compose>(Compose.NAME) {
        fromBundle(aem.bundle("jarAuthor"))
    }
}

