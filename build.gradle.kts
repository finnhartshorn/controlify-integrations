plugins {
	id("dev.architectury.loom") version "1.7.+"
}

class ModData {
	val id = property("mod.id").toString()
	val name = property("mod.name")
	val version = property("mod.version")
	val group = property("mod.group").toString()
	val description = property("mod.description")
	val source = property("mod.source")
	val issues = property("mod.issues")
	val license = property("mod.license").toString()
	val modrinth = property("mod.modrinth")
	val curseforge = property("mod.curseforge")
	val kofi = property("mod.kofi")
	val discord = property("mod.discord")
}

class Dependencies {
	val modmenuVersion = property("deps.modmenu_version")
	val yaclVersion = property("deps.yacl_version")
	val devauthVersion = property("deps.devauth_version")
}

class LoaderData {
	val loader = loom.platform.get().name.lowercase()
	val isFabric = loader == "fabric"
	val isNeoforge = loader == "neoforge"
}

class McData {
	val version = property("mod.mc_version")
	val dep = property("mod.mc_dep")
	val targets = property("mod.mc_targets").toString().split(", ")
}

val mc = McData()
val mod = ModData()
val deps = Dependencies()
val loader = LoaderData()

version = "${mod.version}+${mc.version}-${loader.loader}"
group = mod.group
base { archivesName.set(mod.id) }

stonecutter.const("fabric", loader.isFabric)
stonecutter.const("neoforge", loader.isNeoforge)

loom {
	silentMojangMappingsLicense()

	runConfigs.all {
		ideConfigGenerated(stonecutter.current.isActive)
		runDir = "../../run"
	}

	runConfigs.remove(runConfigs["server"])
}

repositories {
	maven("https://maven.parchmentmc.org") // Parchment
	maven("https://maven.isxander.dev/releases") // YACL
	maven("https://thedarkcolour.github.io/KotlinForForge") // Kotlin for Forge - required by YACL
	maven("https://maven.terraformersmc.com") // Mod Menu
	maven("https://maven.neoforged.net/releases") // NeoForge
	maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") // DevAuth
}

dependencies {
	minecraft("com.mojang:minecraft:${mc.version}")

	@Suppress("UnstableApiUsage")
	mappings(loom.layered {
		// Mojmap mappings
		officialMojangMappings()

		// Parchment mappings (it adds parameter mappings & javadoc)
		optionalProp("deps.parchment_version") {
			if (mc.version == "1.21.3") parchment("org.parchmentmc.data:parchment-1.21:$it@zip") // TODO: remove when parchment 1.21.3
			else parchment("org.parchmentmc.data:parchment-${property("mod.mc_version")}:$it@zip")
		}
	})

	modRuntimeOnly("me.djtheredstoner:DevAuth-${loader.loader}:${deps.devauthVersion}")

	if (loader.isFabric) {
		modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
		if (mc.version == "1.21.3") modImplementation("net.fabricmc.fabric-api:fabric-api:0.106.1+1.21.3") // TODO: remove when I know why this is needed lol
		if (mc.version == "1.21.3") modImplementation("dev.isxander:yet-another-config-lib:${deps.yaclVersion}+1.21.2-${loader.loader}") // TODO: remove when YACL 1.21.3
		else modImplementation("dev.isxander:yet-another-config-lib:${deps.yaclVersion}+${mc.version}-${loader.loader}")
		modImplementation("com.terraformersmc:modmenu:${deps.modmenuVersion}")
	} else if (loader.isNeoforge) {
		"neoForge"("net.neoforged:neoforge:${findProperty("deps.neoforge")}")
		if (mc.version == "1.21.3") implementation("dev.isxander:yet-another-config-lib:${deps.yaclVersion}+1.21.2-${loader.loader}") {isTransitive = false} // TODO: remove when YACL 1.21.3
		else implementation("dev.isxander:yet-another-config-lib:${deps.yaclVersion}+1.21.2-${loader.loader}") {isTransitive = false}
	}
}

java {
	val java = if (stonecutter.compare(
			stonecutter.current.version,
			"1.20.6"
		) >= 0
	) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
	sourceCompatibility = java
	targetCompatibility = java
}

tasks.processResources {
	val props = buildMap {
		put("id", mod.id)
		put("name", mod.name)
		put("version", mod.version)
		put("mcdep", mc.dep)
		put("description", mod.description)
		put("source", mod.source)
		put("issues", mod.issues)
		put("license", mod.license)
		put("modrinth", mod.modrinth)
		put("curseforge", mod.curseforge)
		put("kofi", mod.kofi)
		put("discord", mod.discord)
		put("modmenu_version", deps.modmenuVersion)
		put("yacl_version", deps.yaclVersion)

		if (loader.isNeoforge) {
			put("forgeConstraint", findProperty("modstoml.forge_constraint"))
		}
		if (mc.version == "1.20.1" || mc.version == "1.20.4") {
			put("forge_id", loader.loader)
		}
	}

	props.forEach(inputs::property)

	if (loader.isFabric) {
		filesMatching("fabric.mod.json") { expand(props) }
		exclude(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml"))
	}

	if (loader.isNeoforge) {
		if (mc.version == "1.20.4") {
			filesMatching("META-INF/mods.toml") { expand(props) }
			exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
		} else {
			filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
			exclude("fabric.mod.json", "META-INF/mods.toml")
		}
	}
}

if (stonecutter.current.isActive) {
	rootProject.tasks.register("buildActive") {
		group = "project"
		dependsOn(tasks.named("build"))
	}
}

@Suppress("TYPE_MISMATCH", "UNRESOLVED_REFERENCE")
fun <T> optionalProp(property: String, block: (String) -> T?): T? =
	findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)

fun isPropDefined(property: String): Boolean {
	return property(property)?.toString()?.isNotBlank() ?: false
}
