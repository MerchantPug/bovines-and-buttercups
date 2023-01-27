# Bovines and Buttercups

Here is the GitHub repository for the Bovines and Buttercups Minecraft mod.

You can download the mod on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bovines-and-buttercups, [Modrinth](https://modrinth.com/mod/bovines-and-buttercups) or [GitHub Releases](https://github.com/MerchantPug/bovines-and-buttercups/releases)

### Datapack/Addon/Integration Creation
Please check out the [Wiki](https://github.com/MerchantPug/bovines-and-buttercups/wiki) for more information on how to get started with these topics.

### Implementing this mod into your project

**build.gradle**
```gradle
dependencies {
    // If you have a Common sourceset shared between Forge, Fabric, and Quilt
    modImplementation "net.merchantpug:bovines-and-buttercups-common:${project.bovines_version}"
    
    // If you have a Fabriclike sourceset shared between Fabric and Quilt
    modImplementation "net.merchantpug:bovines-and-buttercups-fabriclike:${project.bovines_version}"
    
    // Forge
    modImplementation "net.merchantpug:bovines-and-buttercups-forge:${project.bovines_version}"
    
    // Fabric
    modImplementation "net.merchantpug:bovines-and-buttercups-fabric:${project.bovines_version}"
    
    // Quilt
    modImplementation "net.merchantpug:bovines-and-buttercups-quilt:${project.bovines_version}"
}
```

**gradle.properties**
```properties
bovines_version=[INSERT VERSION HERE]
```