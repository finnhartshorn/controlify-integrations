plugins {
    id("dev.kikugie.stonecutter")
//    id("fabric-loom") version "1.7-SNAPSHOT" apply false
    id("dev.architectury.loom") version "1.7.+" apply false
    //id("dev.kikugie.j52j") version "1.0.2" apply false // Enables asset processing by writing json5 files
    //id("me.modmuss50.mod-publish-plugin") version "0.7.+" apply false // Publishes builds to hosting websites
}
stonecutter active "1.20.1" /* [SC] DO NOT EDIT */

// Builds every version into `build/libs/{mod.version}/`
stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

/*
// Publishes every version
stonecutter registerChiseled tasks.register("chiseledPublishMods", stonecutter.chiseled) {
    group = "project"
    ofTask("publishMods")
}
*/

stonecutter configureEach {
    /*
    See src/main/java/com/example/TemplateMod.java
    and https://stonecutter.kikugie.dev/
    */
    // Swaps replace the scope with a predefined value
    swap("mod_version", "\"${property("mod.version")}\";")
    // Constants add variables available in conditions
    const("release", property("mod.id") != "template")
    // Dependencies add targets to check versions against
    // Using `project.property()` in this block gets the versioned property
    dependency("fapi", project.property("deps.fabric_api").toString())


    val platform = project.property("loom.platform")

    fun String.propDefined() = project.findProperty(this)?.toString()?.isNotBlank() ?: false
    consts(
        listOf(
            "fabric" to (platform == "fabric"),
            "forge" to (platform == "forge"),
            "neoforge" to (platform == "neoforge"),
            "forgelike" to (platform == "forge" || platform == "neoforge"),
            "yacl" to "deps.yacl".propDefined(),
            "controlify" to "deps.controlify".propDefined(),
//            "immediately-fast" to "deps.immediatelyFast".propDefined(),
//            "iris" to "deps.iris".propDefined(),
//            "mod-menu" to "deps.modMenu".propDefined(),
//            "sodium" to "deps.sodium".propDefined(),
//            "simple-voice-chat" to "deps.simpleVoiceChat".propDefined(),
//            "reeses-sodium-options" to "deps.reesesSodiumOptions".propDefined(),
//            "fancy-menu" to "deps.fancyMenu".propDefined(),
        )
    )
}