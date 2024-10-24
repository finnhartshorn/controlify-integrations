package com.finnethen.controlifyintegrations;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlifyIntegrations implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MODID = "controlifyintegrations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String VERSION = "0.1.0";

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");

        //? if !release
        /*LOGGER.warn("I'm still a template!");*/

        //? if fapi: <0.95 {
        LOGGER.info("Fabric API is old on this version");
        LOGGER.info("Please update!");
        //?}
    }

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}