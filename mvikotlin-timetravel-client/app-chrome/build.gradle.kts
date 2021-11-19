plugins {
//    id("org.jetbrains.kotlin.js")
    id("kotlin-multiplatform")
}

kotlin {
    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":mvikotlin-timetravel-proto-internal"))
            }
        }
    }
}
