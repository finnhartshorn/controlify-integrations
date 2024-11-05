package com.finnethen.controlifyintegrations.mixin.base;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Controlify.class, remap = false)
public interface ControlifyAccessor {
    @Accessor()
    VirtualMouseHandler getVirtualMouseHandler();
}
