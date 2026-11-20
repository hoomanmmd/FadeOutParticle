plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

android {
    namespace = "com.github.hoomanmmd.fadeoutparticle.xml"
    compileSdk {
        version = release(37) {
            minorApiLevel = 2
        }
    }

    defaultConfig {
        aarMetadata { minCompileSdk = 27 }
        minSdk = 27
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    publishing {
        singleVariant("release") { withSourcesJar() }
    }
}
publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.hoomanmmd"
            artifactId = "xml"
            version = "2.0.6"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {
    implementation(project(":core"))
}
