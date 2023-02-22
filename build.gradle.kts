
plugins {
    java
}

group = "de.peterkossek.pinglog"
version = "1.0"

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

tasks.create("fatJar", Jar::class) {
    group = "build"
    description = "Creates a self-contained fat JAR of the application that can be run."
    manifest.attributes["Main-Class"] = "de.peterkossek.pinglog.PingLog"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    val dependencies = configurations.runtimeClasspath.get().map{ if (it.isDirectory) it else zipTree(it)}
    from(dependencies)
    with(tasks.jar.get())
}