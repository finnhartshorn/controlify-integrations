package com.finnethen.controlifyintegrations.mixin.features;

import dev.emi.emi.bom.MaterialNode;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "dev.emi.emi.screen.BoMScreen$Node", remap = false)
public interface EmiRecipeTreeScreenNodeAccessor {
    @Accessor
    int getX();
    @Accessor
    int getY();
    @Accessor
    MaterialNode getNode();
    @Accessor
    int getWidth();
    @Accessor
    long getAmount();
    @Invoker
    Text callGetAmountText();
}
