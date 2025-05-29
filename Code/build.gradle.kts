// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20" apply false
    id("org.sonarqube") version "4.4.1.3373"
    //id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

sonarqube {
    properties {
        property("sonar.projectKey", "TU_ORG_TU_REPO") // Cambia por tu clave de proyecto SonarCloud
        property("sonar.organization", "TU_ORG")       // Cambia por tu organización SonarCloud
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", System.getenv("SONAR_TOKEN"))
        property("sonar.kotlin.detekt.reportPaths", "$rootDir/app/build/reports/detekt/detekt.xml")
        //property("sonar.kotlin.ktlint.reportPaths", "$rootDir/app/build/reports/ktlint/ktlint.xml")
    }
}
