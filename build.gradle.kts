plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java.sourceSets["main"].java {
    srcDirs("src/main/java")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("commons-cli:commons-cli:1.5.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}