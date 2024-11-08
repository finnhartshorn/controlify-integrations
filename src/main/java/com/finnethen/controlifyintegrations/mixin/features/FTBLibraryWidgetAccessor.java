package com.finnethen.controlifyintegrations.mixin.features;

import dev.ftb.mods.ftblibrary.ui.Panel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = dev.ftb.mods.ftblibrary.ui.Widget.class, remap = false)
public interface FTBLibraryWidgetAccessor {
    @Final
    @Accessor
    Panel getParent();
}
