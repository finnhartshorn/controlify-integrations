package com.finnethen.controlifyintegrations.mixin.features;


import dev.ftb.mods.ftblibrary.sidebar.SidebarGroupGuiButton;
import dev.ftb.mods.ftblibrary.sidebar.SidebarGuiButton;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SidebarGroupGuiButton.class)
public interface FTBLibrarySidebarGroupGuiButtonAccessor {

    @Final
    @Accessor
    List<SidebarGuiButton> getButtons();
}
