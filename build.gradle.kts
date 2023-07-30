plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {

    // Paper
    compileOnly("io.papermc.paper", "paper-api", "1.20.1-R0.1-SNAPSHOT")

    // Plugins
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7")

    // Others
    paperLibrary("org.spongepowered", "configurate-hocon", "4.1.2")
    paperLibrary("net.kyori", "adventure-serializer-configurate4", "4.12.0")
    paperLibrary("cloud.commandframework", "cloud-paper", "1.8.3")
    implementation("org.incendo.interfaces", "interfaces-paper", "1.0.0-SNAPSHOT") // class org.incendo.interfaces.paper.view.ChestView is not provided by a interface io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader
    paperLibrary("com.google.inject", "guice", "7.0.0")
}

version = "0.2.2"

paper {
    authors = listOf("Unitarou")
    website = "https://github.com/NamiUni"
    apiVersion = "1.20"
    generateLibrariesJson = true
    foliaSupported = false

    val mainPackage = "party.morino.moripaevent"
    main = "$mainPackage.MoripaEvent"
    bootstrapper = "$mainPackage.MoripaEventBootstrap"
    loader = "$mainPackage.MoripaEventPluginLoader"

    serverDependencies {
        register("Vault") {
            required = true
        }
    }
}

tasks {
    compileJava {
        this.options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    shadowJar {
        this.archiveClassifier.set(null as String?)
    }

    runServer {
        minecraftVersion("1.20.1")
    }
}
