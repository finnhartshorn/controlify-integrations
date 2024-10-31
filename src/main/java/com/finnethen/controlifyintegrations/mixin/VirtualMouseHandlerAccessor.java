package com.finnethen.controlifyintegrations.mixin;

import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = VirtualMouseHandler.class, remap = false)
public interface VirtualMouseHandlerAccessor {
    @Accessor()
    SnapPoint getLastSnappedPoint();

    @Accessor()
    boolean getVirtualMouseEnabled();
}
