package com.finnethen.controlifyintegrations.integrations;

import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ScreenProcessor;

public class FTBLibraryScreenWrapperProcessor extends ScreenProcessor<ScreenWrapper> {
    public FTBLibraryScreenWrapperProcessor(ScreenWrapper screen) {
        super(screen);
    }

    @Override
    protected boolean handleComponentButtonOverride(ControllerEntity controller) {
        if (ControlifyBindings.GUI_BACK.on(controller).justPressed()
                && screen.getGui().anyModalPanelOpen()) {
            screen.getGui().popModalPanel();
            return true;
        }

        return super.handleComponentButtonOverride(controller);
    }
}
