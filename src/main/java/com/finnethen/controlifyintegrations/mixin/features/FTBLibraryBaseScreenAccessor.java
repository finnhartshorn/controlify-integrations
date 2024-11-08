package com.finnethen.controlifyintegrations.mixin.features;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.ModalPanel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Deque;

@Mixin(value = BaseScreen.class, remap = false)
public interface FTBLibraryBaseScreenAccessor {
    @Accessor
    Deque<ModalPanel> getModalPanels();
}
