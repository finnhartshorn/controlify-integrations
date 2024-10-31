package com.finnethen.controlifyintegrations;

import com.finnethen.controlifyintegrations.config.ControlifyIntegrationsConfig;
import net.minecraft.util.Identifier;
//? if fabric
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? if neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
*///?}


//? if neoforge
/*@Mod(value = "controlifyintegrations", dist = Dist.CLIENT)*/
public class ControlifyIntegrations /*? if fabric {*/ implements ModInitializer /*?}*/ {
    public static final String MODID = "controlifyintegrations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String VERSION = "0.1.0";
    //? if fabric {
    @Override
    public void onInitialize() {
        ControlifyIntegrationsConfig.CONFIG.load();
    }
    //?}

    //? if neoforge {
    /*public ControlifyIntegrations() {
        ControlifyIntegrationsConfig.CONFIG.load();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> ControlifyIntegrationsConfig.configScreen(parent));
    }
	*///?}

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}
