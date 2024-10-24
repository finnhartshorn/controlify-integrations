package com.finnethen.controlifyintegrations.integrations;

import com.finnethen.controlifyintegrations.ControlifyIntegrations;
import dev.emi.emi.config.SidebarType;
import dev.emi.emi.screen.EmiScreenManager;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;

public class EmiSupport {
    public static List<SnapPoint> getSnapPoints() {
        List<SnapPoint> points = new ArrayList<>();
        if (EmiScreenManager.isDisabled()) {
            return points;
        }
        int offset = 9;
        for (SidebarType sidebar : Arrays.asList(SidebarType.INDEX, SidebarType.FAVORITES)) {
            EmiScreenManager.SidebarPanel panel = EmiScreenManager.getPanelFor(sidebar);
            if (panel != null) {
                if (panel.pageLeft.visible) {
                    points.add(getWidgetSnapPoint(panel.pageLeft));
                }
                if (panel.pageRight.visible) {
                    points.add(getWidgetSnapPoint(panel.pageRight));
                }
                if (panel.cycle.visible) {
                    points.add(getWidgetSnapPoint(panel.cycle));
                }

                for (EmiScreenManager.ScreenSpace space : panel.getSpaces()) {
                    int numStacks = min(space.getStacks().size() - space.pageSize * panel.page, space.pageSize);
                    for (int i = 0; i < numStacks; i++) {
                        points.add(new SnapPoint(new Vector2i(space.getRawX(i) + offset, space.getRawY(i) + offset), offset));
                    }
                }
            }
        }
        if (EmiScreenManager.emi.visible) {
            points.add(getWidgetSnapPoint(EmiScreenManager.emi));
        }
        if (EmiScreenManager.tree.visible) {
            points.add(getWidgetSnapPoint(EmiScreenManager.tree));
        }
        if (EmiScreenManager.search.visible) {
            points.add(getWidgetSnapPoint(EmiScreenManager.search));
        }

        return points;
    }

    private static SnapPoint getWidgetSnapPoint(ClickableWidget widget) {
        return new SnapPoint(new Vector2i(widget.getX() + widget.getWidth() / 2, widget.getY() + widget.getHeight() / 2), Math.min(widget.getWidth(), widget.getHeight()) / 2);
    }

    public static Boolean isRecipesSidebarHovered() {
        if (EmiScreenManager.isDisabled()) {
            return false;
        }

        var x = EmiScreenManager.lastMouseX;
        var y = EmiScreenManager.lastMouseY;

        EmiScreenManager.SidebarPanel panel = EmiScreenManager.getPanelFor(SidebarType.INDEX);
        if (panel != null && !panel.getSpaces().isEmpty() && panel.getBounds().contains(x, y) && panel.isVisible()) {
            ControlifyIntegrations.LOGGER.info("#### Emi recipes sidebar is hovered");
            return true;
        }

        return false;
    }

    public static Boolean isFavoritesSidebarHovered() {
        if (EmiScreenManager.isDisabled()) {
            return false;
        }

        var x = EmiScreenManager.lastMouseX;
        var y = EmiScreenManager.lastMouseY;

        EmiScreenManager.SidebarPanel panel = EmiScreenManager.getPanelFor(SidebarType.FAVORITES);
        return panel != null && !panel.getSpaces().isEmpty() && panel.getBounds().contains(x, y) && panel.isVisible();
    }
    public static Boolean isSearchBarHovered() {
        if (EmiScreenManager.isDisabled()) {
            return false;
        }

        var x = EmiScreenManager.lastMouseX;
        var y = EmiScreenManager.lastMouseY;

        return EmiScreenManager.search.visible && EmiScreenManager.search.isMouseOver(x, y);
    }

    public static Boolean isEmiVisible() {
        if (EmiScreenManager.isDisabled()) {
            return false;
        }
        boolean anythingVisible = false;
        for (SidebarType sidebar : Arrays.asList(SidebarType.INDEX, SidebarType.FAVORITES, SidebarType.CRAFTABLES)) {
            EmiScreenManager.SidebarPanel panel = EmiScreenManager.getPanelFor(sidebar);
            if (panel != null && panel.space != null && panel.isVisible()) {
                anythingVisible = true;
                break;
            }
        }
        return anythingVisible;
    }

    public static void onTabLeftPressed() {
        if (isFavoritesSidebarHovered()) {
            var panel = EmiScreenManager.getPanelFor(SidebarType.FAVORITES);
            if (panel != null) {
                panel.pageLeft.onPress();
            }
        } else if (isRecipesSidebarHovered() || isSearchBarHovered()
                || (MinecraftClient.getInstance().currentScreen != null
                &&!(MinecraftClient.getInstance().currentScreen instanceof CreativeInventoryScreen))
        ) {
            var panel = EmiScreenManager.getPanelFor(SidebarType.INDEX);
            if (panel != null) {
                panel.pageLeft.onPress();
            }
        }
    }

    public static void onTabRightPressed() {
        if (isFavoritesSidebarHovered()) {
            var panel = EmiScreenManager.getPanelFor(SidebarType.FAVORITES);
            if (panel != null) {
                panel.pageRight.onPress();
            }
        } else if (isRecipesSidebarHovered() || isSearchBarHovered()
                || (MinecraftClient.getInstance().currentScreen != null
                        &&!(MinecraftClient.getInstance().currentScreen instanceof CreativeInventoryScreen))
        ) {
            var panel = EmiScreenManager.getPanelFor(SidebarType.INDEX);
            if (panel != null) {
                panel.pageRight.onPress();
            }
        }
    }
}
