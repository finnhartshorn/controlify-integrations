package com.finnethen.controlifyintegrations.mixin;

import com.finnethen.controlifyintegrations.integrations.EmiSupport;
import com.finnethen.controlifyintegrations.integrations.FTBSupport;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = VirtualMouseHandler.class, remap = false)
public abstract class MixinVirtualMouseHandler {

    @Shadow
    private Set<SnapPoint> snapPoints;

    // TODO: Make this more generic? Integrations could register callbacks that provide snap points?
    // Only needed for mods that add overlays on top of container screens (HandledScreen?)
    @Inject(at = @At(value = "FIELD", target = "Ldev/isxander/controlify/virtualmouse/VirtualMouseHandler;snapPoints:Ljava/util/Set;",
            shift = At.Shift.AFTER, ordinal = 0),
            method = "handleControllerInput(Ldev/isxander/controlify/controller/ControllerEntity;)V",
            remap = false)
    void ipn$VirtualMouseHandler$handleControllerInput(CallbackInfo ci) {
        if (!EmiSupport.isEmiHidden()) {
            snapPoints.addAll(EmiSupport.getSnapPoints());
        }
        snapPoints.addAll(FTBSupport.getSnapPoints());
    }
}