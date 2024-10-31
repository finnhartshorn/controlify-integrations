package com.finnethen.controlifyintegrations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "dev.emi.emi.screen.BoMScreen$Cost", remap = false)
public interface EmiRecipeTreeScreenCostAccessor {
    @Accessor
    int getX();
    @Accessor
    int getY();
}
