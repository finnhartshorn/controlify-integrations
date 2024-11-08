package com.finnethen.controlifyintegrations.mixin.features;

import com.finnethen.controlifyintegrations.integrations.FTBLibraryScreenWrapperProcessor;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftbquests.client.gui.quests.*;
import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.screenop.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mixin(value = ScreenWrapper.class, remap = false)
public abstract class FTBLibraryScreenWrapperMixin extends Screen implements ISnapBehaviour, ScreenProcessorProvider /*,ScreenControllerEventListener*/ {

    @Final
    @Shadow
    private BaseScreen wrappedGui;

    @Unique
    private final FTBLibraryScreenWrapperProcessor controlify$processor = new FTBLibraryScreenWrapperProcessor((ScreenWrapper) (Object) this);

    protected FTBLibraryScreenWrapperMixin(Text title) {
        super(title);
    }

    @Override
    public ScreenProcessor<?> screenProcessor() {
        return controlify$processor;
    }

    @Override
    public Set<SnapPoint> getSnapPoints() {
        Set<SnapPoint> points = new HashSet<>();

        if (wrappedGui.anyModalPanelOpen()) {
            FTBLibraryBaseScreenAccessor accessor = (FTBLibraryBaseScreenAccessor) wrappedGui;
            accessor.getModalPanels().forEach(panel -> {
                controlifyIntegrations$panelRecursive(points, panel);
            });
            return points;
        }

        wrappedGui.getWidgets().forEach(widget -> {
            controlifyIntegrations$panelRecursive(points, widget);
        });

        return points;
    }


    @Unique
    private boolean controlifyIntegrations$inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x <= MinecraftClient.getInstance().getWindow().getScaledWidth() && y <= MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    @Unique
    private void controlifyIntegrations$panelRecursive(Set<SnapPoint> points, Widget widget) {
        if (widget == null || !widget.isEnabled() || !widget.shouldDraw()) {
            return;
        }

        if (widget instanceof ChapterImageButton) {
            return;
        }

        if (widget instanceof ExpandChaptersButton expandChaptersButton) {
            FTBQuestsExpandChaptersButtonAccessor accessor = (FTBQuestsExpandChaptersButtonAccessor) expandChaptersButton;
            FTBQuestsQuestScreenAccessor accessor2 = (FTBQuestsQuestScreenAccessor) accessor.getQuestScreen();
            FTBQuestsChapterPanelWidgetAccessor accessor3 = (FTBQuestsChapterPanelWidgetAccessor) accessor2.getChapterPanel();
            if (!accessor3.getExpanded() && !accessor3.callIsPinned()) {
                points.add(new SnapPoint(widget.getX() + widget.width / 2, widget.getY() + widget.height / 2, Math.min(widget.width, widget.height) / 2));
            }
            return;
        }

        if (widget instanceof QuestButton) {
            FTBLibraryWidgetAccessor accessor = (FTBLibraryWidgetAccessor) widget;
            int x = (int) (widget.getX() - accessor.getParent().getScrollX()) + widget.width / 2;
            int y = (int) (widget.getY() - accessor.getParent().getScrollY()) + widget.height / 2;
            if (controlifyIntegrations$inBounds(x, y)) {
                points.add(new SnapPoint(x, y, Math.min(widget.width, widget.height) / 2));
            }
            return;
        }

        if (widget instanceof ChapterPanel.ModpackButton) {
            int x = widget.getX() + widget.width - 8;
            int y = widget.getY() + widget.height / 2;
            if (controlifyIntegrations$inBounds(x, y)) {
                points.add(new SnapPoint(x, y, 8));
            }
            return;
        }

        if (widget instanceof SimpleButton) {
            if (widget.getTitle() instanceof MutableText text) {
                if (text.getContent() instanceof TranslatableTextContent textContent) {
                    if (Objects.equals(textContent.getKey(), "ftbquests.gui.no_dependants")
                    || Objects.equals(textContent.getKey(), "ftbquests.gui.no_dependencies")) {
                        return;
                    }
                }
            }
        }

        if (widget instanceof Button) {
            int x = widget.getX() + widget.width / 2;
            int y = widget.getY() + widget.height / 2;
            if (controlifyIntegrations$inBounds(x, y)) {
                points.add(new SnapPoint(x, y, Math.min(widget.width, widget.height) / 2));
            }
            return;
        }

        if (widget instanceof Panel panel) {
            panel.getWidgets().forEach(subwidget -> {
                controlifyIntegrations$panelRecursive(points, subwidget);
            });
            return;
        }
    }
}
