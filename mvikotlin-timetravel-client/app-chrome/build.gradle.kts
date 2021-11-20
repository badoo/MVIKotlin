plugins {
    id("org.jetbrains.kotlin.js")
}

kotlin {
    js(IR) {
        browser()
        binaries.library()
    }
}
