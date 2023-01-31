# Bovines and Buttercups

Here is the GitHub repository for the Bovines and Buttercups Minecraft mod.

You can download the mod on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bovines-and-buttercups), or [Modrinth](https://modrinth.com/mod/bovines-and-buttercups)

### Datapack/Addon/Integration Creation
Please check out the [Wiki](https://github.com/MerchantPug/bovines-and-buttercups/wiki) for more information on how to get started with these topics.

### Implementing this mod into your project

**build.gradle**
```groovy
repositories {
    maven {
        name = "Pug's Maven"
        url = 'https://maven.merchantpug.net/releases/'
    }
}

dependencies {
    // If you have a Common sourceset shared between Forge, Fabric, and Quilt
    compileOnly "net.merchantpug:Bovines-And-Buttercups-Common:${project.bovines_version}"
    
    // Forge
    implementation fg.deobf("net.merchantpug:Bovines-And-Buttercups-Forge:${project.bovines_version}")
    
    // Fabric
    modImplementation "net.merchantpug:Bovines-And-Buttercups-Fabric:${project.bovines_version}"
    
    // Quilt
    modImplementation "net.merchantpug:Bovines-And-Buttercups-Quilt:${project.bovines_version}"
}
```

**gradle.properties**
```properties
bovines_version=[INSERT VERSION HERE]
```