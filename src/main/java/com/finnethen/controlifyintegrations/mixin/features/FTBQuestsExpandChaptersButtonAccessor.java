package com.finnethen.controlifyintegrations.mixin.features;

import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = dev.ftb.mods.ftbquests.client.gui.quests.ExpandChaptersButton.class, remap = false)
public interface FTBQuestsExpandChaptersButtonAccessor {
    @Final
    @Accessor
    QuestScreen getQuestScreen();



}
