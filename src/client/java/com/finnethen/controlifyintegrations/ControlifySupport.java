package com.finnethen.controlifyintegrations;

import com.finnethen.controlifyintegrations.integrations.EmiSupport;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.bindings.BindContext;
import net.minecraft.text.Text;

public class ControlifySupport implements ControlifyEntrypoint {
    public static final BindContext EMI_SEARCH_HOVER = new BindContext(ControlifyIntegrations.id("emi_search_hover"), mc -> EmiSupport.isSearchBarHovered());
    public static final BindContext EMI_RECIPES_HOVER = new BindContext(ControlifyIntegrations.id("emi_recipes_hover"), mc -> EmiSupport.isRecipesSidebarHovered());
    public static final BindContext EMI_FAVORITES_HOVER = new BindContext(ControlifyIntegrations.id("emi_favorites_hover"), mc -> EmiSupport.isFavoritesSidebarHovered());
    public static final BindContext EMI_VISIBLE = new BindContext(ControlifyIntegrations.id("emi_visible"), mc -> EmiSupport.isEmiVisible());

    public static InputBindingSupplier FOCUS_LEFT;
    public static InputBindingSupplier FOCUS_RIGHT;
    public static InputBindingSupplier TAB_LEFT;
    public static InputBindingSupplier TAB_RIGHT;

//    public static final BindContext CONTAINER = register("container", (mc) -> {
//        return mc.currentScreen instanceof HandledScreen;
//    });
//    public static final BindContext V_MOUSE_CURSOR = register("vmouse_cursor", (mc) -> {
//        return mc.currentScreen != null && ScreenProcessorProvider.provide(mc.currentScreen).virtualMouseBehaviour().hasCursor() && Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled();
//    });


    @Override
    public void onControlifyInit(ControlifyApi controlify) {
        ControlifyIntegrations.LOGGER.info("#### ControlifyIntegrations is initializing...");
        ControlifyEntrypoint.super.onControlifyInit(controlify);
        var bindings = ControlifyBindApi.get();
        bindings.registerBindContext(EMI_SEARCH_HOVER);
        bindings.registerBindContext(EMI_RECIPES_HOVER);
        bindings.registerBindContext(EMI_FAVORITES_HOVER);
        bindings.registerBindContext(EMI_VISIBLE);

        FOCUS_LEFT = bindings.registerBinding(builder -> builder
                .id(ControlifyIntegrations.id("focus_left"))
                .category(Text.of("Emi"))
                .allowedContexts(EMI_VISIBLE)
        );
        FOCUS_RIGHT = bindings.registerBinding(builder -> builder
                .id(ControlifyIntegrations.id("focus_right"))
                .category(Text.of("Emi"))
                .allowedContexts(EMI_VISIBLE)
        );
        TAB_LEFT = bindings.registerBinding(builder -> builder
                .id(ControlifyIntegrations.id("tab_left"))
                .category(Text.of("Emi"))
                .allowedContexts(EMI_VISIBLE)
        );
        TAB_RIGHT = bindings.registerBinding(builder -> builder
                .id(ControlifyIntegrations.id("tab_right"))
                .category(Text.of("Emi"))
                .allowedContexts(EMI_VISIBLE)
        );

        ControlifyEvents.ACTIVE_CONTROLLER_TICKED.register(controller -> {
            if (TAB_LEFT.on(controller.controller()).justPressed()) {
//                ControlifyIntegrations.LOGGER.info("#### Tab left pressed");
                EmiSupport.onTabLeftPressed();
            }
            if (TAB_RIGHT.on(controller.controller()).justPressed()) {
//                ControlifyIntegrations.LOGGER.info("#### Tab right pressed");
                EmiSupport.onTabRightPressed();
            }

//            if (EMI_SEARCH_HOVER.on(controller)) {
//                EmiSupport.onSearchBarHovered();
//            }
//            if (EMI_RECIPES_HOVER.on(controller)) {
//                EmiSupport.onRecipesSidebarHovered();
//            }
//            if (EMI_FAVORITES_HOVER.on(controller)) {
//                EmiSupport.onFavoritesSidebarHovered();
//            }
        });
    }


    @Override
    public void onControllersDiscovered(ControlifyApi controlify) {}
}
