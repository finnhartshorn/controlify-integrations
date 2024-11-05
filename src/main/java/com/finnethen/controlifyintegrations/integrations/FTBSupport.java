package com.finnethen.controlifyintegrations.integrations;

import com.finnethen.controlifyintegrations.mixin.features.FTBLibrarySidebarGroupGuiButtonAccessor;
import dev.architectury.hooks.client.screen.ScreenHooks;
import dev.ftb.mods.ftblibrary.sidebar.SidebarGroupGuiButton;
import dev.ftb.mods.ftblibrary.sidebar.SidebarGuiButton;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;

import java.util.ArrayList;
import java.util.List;

public class FTBSupport {
    public static List<SnapPoint> getSnapPoints() {
        List<SnapPoint> points = new ArrayList<>();
        for (Drawable drawable: ScreenHooks.getRenderables(MinecraftClient.getInstance().currentScreen)) {
            if (drawable instanceof SidebarGroupGuiButton) {
                FTBLibrarySidebarGroupGuiButtonAccessor buttonGroup = (FTBLibrarySidebarGroupGuiButtonAccessor) drawable;
                for (SidebarGuiButton button: buttonGroup.getButtons()) {
                    points.add(new SnapPoint(button.x + 8, button.y + 8, 8));
                }
            }
        }
        return points;
    }
}
