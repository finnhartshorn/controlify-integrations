package com.finnethen.controlifyintegrations;

import com.finnethen.controlifyintegrations.integrations.EmiSupport;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.bindings.BindContext;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
public class ControlifySupport implements ControlifyEntrypoint {
    public static final BindContext EMI_SEARCH_HOVER = new BindContext(ControlifyIntegrations.id("emi_search_hover"), mc -> EmiSupport.isSearchBarHovered());
    public static final BindContext EMI_RECIPES_HOVER = new BindContext(ControlifyIntegrations.id("emi_recipes_hover"), mc -> EmiSupport.isRecipesSidebarHovered());
    public static final BindContext EMI_FAVORITES_HOVER = new BindContext(ControlifyIntegrations.id("emi_favorites_hover"), mc -> EmiSupport.isFavoritesSidebarHovered());
    public static final BindContext EMI_VISIBLE = new BindContext(ControlifyIntegrations.id("emi_visible"), mc -> !EmiSupport.isEmiHidden());

    public static InputBindingSupplier FOCUS_LEFT;
    public static InputBindingSupplier FOCUS_RIGHT;
    public static InputBindingSupplier TAB_LEFT;
    public static InputBindingSupplier TAB_RIGHT;
    public static InputBindingSupplier LEFT_STICK_PRESS;
    public static InputBindingSupplier RIGHT_STICK_PRESS;

    @Override
    public void onControlifyInit(ControlifyApi controlify) {
        ControlifyIntegrations.LOGGER.info("Controlify support initialized");
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

        LEFT_STICK_PRESS = bindings.registerBinding(builder -> builder
                        .id(ControlifyIntegrations.id("left_stick_click"))
                        .category(Text.of("Emi"))
//                .allowedContexts(EMI_VISIBLE)
        );

        RIGHT_STICK_PRESS = bindings.registerBinding(builder -> builder
                .id(ControlifyIntegrations.id("right_stick_click"))
                .category(Text.of("Emi"))
                .allowedContexts(EMI_VISIBLE)
        );

        ControlifyEvents.ACTIVE_CONTROLLER_TICKED.register(controller -> {
            if (TAB_LEFT.on(controller.controller()).justPressed()) {
                EmiSupport.onTabLeftPressed();
            }
            if (TAB_RIGHT.on(controller.controller()).justPressed()) {
                EmiSupport.onTabRightPressed();
            }
            if (FOCUS_LEFT.on(controller.controller()).justPressed()) {
                EmiSupport.onLeftTriggerPressed();
            }
            if (FOCUS_RIGHT.on(controller.controller()).justPressed()) {
                EmiSupport.onRightTriggerPressed();
            }
            if (LEFT_STICK_PRESS.on(controller.controller()).justPressed()) {
                EmiSupport.debugStuff();
            }
            if (RIGHT_STICK_PRESS.on(controller.controller()).justPressed()) {
//                EmiSupport.onRightStickPress();
            }
        });

//        Controlify.instance().config().globalSettings().virtualMouseScreens.addAll(List.of(RecipeScreen.class));

//        ScreenProcessorProvider.registerProvider(RecipeScreen.class, Factory);

    }


    @Override
    public void onControllersDiscovered(ControlifyApi controlify) {}
}
