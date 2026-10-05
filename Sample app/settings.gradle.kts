import com.github.javaparser.utils.Log
import java.io.File
import java.util.Properties
import kotlin.math.log

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
val props = Properties()
val localPropsFile = File(settingsDir, "local.properties")
if (localPropsFile.exists()) {
    localPropsFile.inputStream().use { props.load(it) }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "UIDAIFaceAuthPackage"
            url = uri("https://maven.pkg.github.com/UIDAITechCenter/FaceRdOfflineAuthSDK")
            credentials {

                username = props.getProperty("gpr.user") ?: props.getProperty("GITHUB_USER")
                password = props.getProperty("gpr.key") ?: props.getProperty("GITHUB_TOKEN")
            }
        }
    }
}

rootProject.name = "FaceAuthLib_Android"
include(":app")
include(":face-auth-sdk")
