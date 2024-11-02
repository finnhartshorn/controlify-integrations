package com.finnethen.controlifyintegrations;

import com.finnethen.controlifyintegrations.config.ControlifyIntegrationsConfig;
//? if fabric
/*import net.fabricmc.api.ModInitializer;*/
import net.minecraft.util.Identifier;
import net.neoforged.fml.ModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? if neoforge {
import net.neoforged.fml.common.Mod;
//?}


//? if neoforge {
//? if >=1.20.6 {
/*@Mod(value = "controlifyintegrations", dist = Dist.CLIENT)
*///?} else {
@Mod(value = "controlifyintegrations")
//?}
//?}
public class ControlifyIntegrations /*? if fabric {*/ /*implements ModInitializer *//*?}*/ {
    public static final String MODID = "controlifyintegrations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String VERSION = "0.1.0";
    //? if fabric {
    /*@Override
    public void onInitialize() {
        ControlifyIntegrationsConfig.CONFIG.load();
    }
    *///?}

    //? if neoforge {
    public ControlifyIntegrations() {
        LOGGER.info("Controlify Integrations initialized");
        ControlifyIntegrationsConfig.CONFIG.load();
        ModLoadingContext.get().registerExtensionPoint(

                //? if >=1.20.6 {
                /*net.neoforged.neoforge.client.gui.IConfigScreenFactory.class,
                () -> (client, parent) -> ControlifyIntegrationsConfig.configScreen(parent)
                *///?} else {
                net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> ControlifyIntegrationsConfig.configScreen(parent))
                //?}

        );
    }
    //?}

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}