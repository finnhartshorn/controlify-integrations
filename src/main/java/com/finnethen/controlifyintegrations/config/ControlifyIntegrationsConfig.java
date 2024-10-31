package com.finnethen.controlifyintegrations.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ControlifyIntegrationsConfig {
    public static final ConfigClassHandler<ControlifyIntegrationsConfig> CONFIG = ConfigClassHandler.createBuilder(ControlifyIntegrationsConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve("controlifyintegrations.json"))
                    .build())
            .build();

    @SerialEntry public boolean example = false;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.translatable("controlifyintegrations.controlifyintegrations"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("controlifyintegrations.controlifyintegrations"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("controlifyintegrations.example"))
                                .description(OptionDescription.of(Text.translatable("controlifyintegrations.example.description")))
                                .binding(defaults.example, () -> config.example, newVal -> config.example = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}
