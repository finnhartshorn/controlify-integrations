package com.finnethen.controlifyintegrations.mixin.features;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ScreenWrapper.class, remap = false)
public interface FTBLibraryScreenWrapperAccessor {

    @Accessor
    BaseScreen getWrappedGui();
}
