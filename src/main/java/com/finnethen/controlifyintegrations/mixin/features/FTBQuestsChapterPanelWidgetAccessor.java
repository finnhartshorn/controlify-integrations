package com.finnethen.controlifyintegrations.mixin.features;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import dev.ftb.mods.ftbquests.client.gui.quests.ChapterPanel;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ChapterPanel.class, remap = false)
public interface FTBQuestsChapterPanelWidgetAccessor {
    @Accessor
    boolean getExpanded();

    @Invoker
    boolean callIsPinned();

    @Final
    @Accessor
    QuestScreen getQuestScreen();
}
