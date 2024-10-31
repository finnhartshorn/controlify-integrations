package com.finnethen.controlifyintegrations.integrations;

import com.finnethen.controlifyintegrations.ControlifyIntegrations;
import com.finnethen.controlifyintegrations.mixin.ControlifyAccessor;
import com.finnethen.controlifyintegrations.mixin.VirtualMouseHandlerAccessor;
import dev.emi.emi.config.SidebarSide;
import dev.emi.emi.config.SidebarType;
import dev.emi.emi.screen.EmiScreenManager;
import dev.emi.emi.screen.RecipeScreen;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.Widget;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class EmiSupport {
    public static List<SnapPoint> getSnapPoints() {
        List<SnapPoint> points = new ArrayList<>();

        points.addAll(getPanelSnapPoints(EmiScreenManager.getPanelFor(SidebarSide.LEFT)));
        points.addAll(getPanelSnapPoints(EmiScreenManager.getPanelFor(SidebarSide.RIGHT)));
        if (EmiScreenManager.search.visible) {
            points.add(getWidgetSnapPoint(EmiScreenManager.search));
        }

        return points;
    }

    private static List<SnapPoint> getPanelSnapPoints(EmiScreenManager.SidebarPanel panel) {
        List<SnapPoint> points = new ArrayList<>();
        int offset = 9;
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
        if ((panel != null ? panel.getType() : null) == SidebarType.FAVORITES) {
            if (EmiScreenManager.emi.visible) {
                points.add(getWidgetSnapPoint(EmiScreenManager.emi));
            }
            if (EmiScreenManager.tree.visible) {
                points.add(getWidgetSnapPoint(EmiScreenManager.tree));
            }
        }
        return points;
    }

    public static SnapPoint getWidgetSnapPoint(Widget widget) {
        return new SnapPoint(new Vector2i(widget.getX() + widget.getWidth() / 2, widget.getY() + widget.getHeight() / 2), min(widget.getWidth(), widget.getHeight()) / 2);
    }

    public static boolean isEmiHidden() {
        return EmiScreenManager.isDisabled() || !(isEmiScreen());
    }

    private static boolean isEmiScreen() {
        var currentScreen = MinecraftClient.getInstance().currentScreen;
        return currentScreen instanceof HandledScreen
                || currentScreen instanceof RecipeScreen;
    }

    public static Boolean isRecipesSidebarHovered() {
        if (isEmiHidden()) {
            return false;
        }

        var x = EmiScreenManager.lastMouseX;
        var y = EmiScreenManager.lastMouseY;

        EmiScreenManager.SidebarPanel panel = EmiScreenManager.getPanelFor(SidebarType.INDEX);
        return panel != null && !panel.getSpaces().isEmpty() && panel.getBounds().contains(x, y) && panel.isVisible();
    }

    public static Boolean isFavoritesSidebarHovered() {
        if (isEmiHidden()) {
            return false;
        }

        var x = EmiScreenManager.lastMouseX;
        var y = EmiScreenManager.lastMouseY;

        EmiScreenManager.SidebarPanel panel = EmiScreenManager.getPanelFor(SidebarType.FAVORITES);
        return panel != null && !panel.getSpaces().isEmpty() && panel.getBounds().contains(x, y) && panel.isVisible();
    }
    public static Boolean isSearchBarHovered() {
        if (isEmiHidden()) {
            return false;
        }

        var x = EmiScreenManager.lastMouseX;
        var y = EmiScreenManager.lastMouseY;

        return EmiScreenManager.search.visible && EmiScreenManager.search.isMouseOver(x, y);
    }

    public static void onTabLeftPressed() {
        if (isFavoritesSidebarHovered()) {
            var panel = EmiScreenManager.getPanelFor(SidebarType.FAVORITES);
            if (panel != null) {
                panel.pageLeft.onPress();
            }
        } else if (isRecipesSidebarHovered() || isSearchBarHovered()
                || (MinecraftClient.getInstance().currentScreen != null
                &&!(MinecraftClient.getInstance().currentScreen instanceof CreativeInventoryScreen
                    || MinecraftClient.getInstance().currentScreen instanceof RecipeScreen))
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
        } else if (isRecipesSidebarHovered()
                || isSearchBarHovered()
                || (MinecraftClient.getInstance().currentScreen != null
                &&!(MinecraftClient.getInstance().currentScreen instanceof CreativeInventoryScreen
                    || MinecraftClient.getInstance().currentScreen instanceof RecipeScreen))
        ) {
            var panel = EmiScreenManager.getPanelFor(SidebarType.INDEX);
            if (panel != null) {
                panel.pageRight.onPress();
            }
        }
    }

    public static void onLeftTriggerPressed() {
        FocusArea oldFocusArea = getCurrentFocusArea();
        if (oldFocusArea == FocusArea.LEFT_SIDEBAR || oldFocusArea == FocusArea.INVALID) {
            return;
        }
        var handler = getContolifyVirtualMouseHandler();
        for (int i = 0; i < 20; i++) {
            handler.snapInDirection(NavigationDirection.LEFT);
            if (isFocusAreaChanged(oldFocusArea)) {
                break;
            }
        }
    }

    public static void onRightTriggerPressed() {
        FocusArea oldFocusArea = getCurrentFocusArea();
        if (oldFocusArea == FocusArea.RIGHT_SIDEBAR || oldFocusArea == FocusArea.INVALID) {
            return;
        }
        var handler = getContolifyVirtualMouseHandler();
        for (int i = 0; i < 20; i++) {
            handler.snapInDirection(NavigationDirection.RIGHT);
            if (isFocusAreaChanged(oldFocusArea)) {
                break;
            }
        }
    }

    private static boolean isFocusAreaChanged(FocusArea oldFocusArea) {
        var handler = (VirtualMouseHandlerAccessor) getContolifyVirtualMouseHandler();
        if (oldFocusArea == FocusArea.LEFT_SIDEBAR) {
            return !getPanelSnapPoints(EmiScreenManager.getPanelFor(SidebarSide.LEFT)).contains(handler.getLastSnappedPoint());
        } else if (oldFocusArea == FocusArea.RIGHT_SIDEBAR) {
            return !getPanelSnapPoints(EmiScreenManager.getPanelFor(SidebarSide.RIGHT)).contains(handler.getLastSnappedPoint());
        } else {
            return getPanelSnapPoints(EmiScreenManager.getPanelFor(SidebarSide.LEFT)).contains(handler.getLastSnappedPoint())
                    || getPanelSnapPoints(EmiScreenManager.getPanelFor(SidebarSide.RIGHT)).contains(handler.getLastSnappedPoint());
        }
    }

    private static VirtualMouseHandler getContolifyVirtualMouseHandler() {
        var accessor = (ControlifyAccessor) Controlify.instance();
        return accessor.getVirtualMouseHandler();
    }

    private enum FocusArea {
        INVALID,
        INVENTORY,
        LEFT_SIDEBAR,
        RIGHT_SIDEBAR,
    }

    private static FocusArea getCurrentFocusArea() {
        if (MinecraftClient.getInstance().currentScreen == null) {
            return FocusArea.INVALID;
        }
        if (isFavoritesSidebarHovered()) {
            return FocusArea.LEFT_SIDEBAR;
        }
        if (isRecipesSidebarHovered()) {
            return FocusArea.RIGHT_SIDEBAR;
        }
        return FocusArea.INVENTORY;
    }

    public static void debugStuff() {
        ControlifyIntegrations.LOGGER.info("#### Debug Stuff");
        if (MinecraftClient.getInstance().currentScreen != null) {
            ControlifyIntegrations.LOGGER.info("#### currentScreen: {} {}", MinecraftClient.getInstance().currentScreen, MinecraftClient.getInstance().currentScreen.getTitle());
        }
        ControlifyIntegrations.LOGGER.info("#### virtualMouseEnabled: {}", ((VirtualMouseHandlerAccessor) getContolifyVirtualMouseHandler()).getVirtualMouseEnabled());
        ControlifyIntegrations.LOGGER.info("#### emiEnabled: {}", !EmiScreenManager.isDisabled());
        ControlifyIntegrations.LOGGER.info("#### focusArea: {}", getCurrentFocusArea());
    }
}
