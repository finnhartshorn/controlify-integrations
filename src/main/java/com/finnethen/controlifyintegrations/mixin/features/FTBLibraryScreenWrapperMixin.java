package com.finnethen.controlifyintegrations.mixin.features;

import com.finnethen.controlifyintegrations.ControlifyIntegrations;
import com.finnethen.controlifyintegrations.integrations.FTBLibraryScreenWrapperProcessor;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftbquests.client.gui.quests.*;
import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(value = ScreenWrapper.class, remap = false)
public abstract class FTBLibraryScreenWrapperMixin extends Screen implements ISnapBehaviour, ScreenProcessorProvider {

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

        wrappedGui.getWidgets().forEach(widget -> {
            if (!widget.isEnabled() || !widget.shouldDraw()) {
                return;
            }
            if (widget instanceof SimpleButton) {
                points.add(new SnapPoint(widget.getX() + widget.width / 2, widget.getY() + widget.height / 2, Math.min(widget.width, widget.height) / 2));
                return;
            }

            switch (widget.getClass().getSimpleName()) {
//                case "SimpleTooltipButton":
//                case "SimpleButton":
//                case "ClearDeathPointButton":
//                case "SettingsButton":
//                case "InviteButton":
//                case "AllyButton":
                case "ExpandChaptersButton":
                    points.add(new SnapPoint(widget.getX() + widget.width / 2, widget.getY() + widget.height / 2, Math.min(widget.width, widget.height) / 2));
//                    addWidgetSnapPoint(points, widget);
                    break;
                case "ChapterPanel":
                    FTBQuestsChapterPanelWidgetAccessor chapterPanel = (FTBQuestsChapterPanelWidgetAccessor) widget;
                    QuestScreen questScreen = chapterPanel.getQuestScreen();
                    if (questScreen != null) {
                        questScreen.getWidgets().forEach(subwidget -> {
                            controlifyIntegrations$addWidgetSnapPoint(points, subwidget);
                        });
                        if (questScreen.questPanel != null) {
                            questScreen.questPanel.getWidgets().forEach(subwidget -> {
                                controlifyIntegrations$addWidgetSnapPoint(points, subwidget);
                            });
                        }
                        if (questScreen.isViewingQuest()) {
                            controlifyIntegrations$addViewQuestSnapPoints(points, questScreen.viewQuestPanel);
//                            ControlifyIntegrations.LOGGER.info("Adding snap points for quest screen");
//                            questScreen.viewQuestPanel.getWidgets().forEach(subwidget -> {
//                                ControlifyIntegrations.LOGGER.info("Adding snap point for " + subwidget.getClass().getSimpleName());
//                                addWidgetSnapPoint(points, subwidget);
//                            });
                        }
                    }
                    if (!chapterPanel.callIsPinned() || !chapterPanel.getExpanded() || !widget.isMouseOver()) {
                        ControlifyIntegrations.LOGGER.info("Chapter panel is not pinned, expanded, or moused over");
                        return;
                    }
                case "QuestPanel": // TODO: Test with a modpack with quests
//                    QuestPanel questPanel = (QuestPanel) widget;
//                    if questPanel.screen
                case "OtherButtonsPanelBottom":
                case "OtherButtonsPanelTop":
                case "CustomTopPanel":
                case "BottomPanel":
                    Panel panel = (Panel) widget;
                    panel.getWidgets().forEach(subwidget -> {
                        controlifyIntegrations$addWidgetSnapPoint(points, subwidget);
//                        points.add(new SnapPoint(subwidget.getX() + subwidget.width / 2, subwidget.getY() + subwidget.height / 2, Math.min(subwidget.width, subwidget.height) / 2));
                    });
                    break;
                default:
                    break;
            }
        });

        return points;
    }

    @Unique
    private void controlifyIntegrations$addWidgetSnapPoint(Set<SnapPoint> points, Widget widget) {
        if (!widget.isEnabled() || !widget.shouldDraw()) {
            return;
        }
        if (widget.getX() < 0
                || widget.getY() < 0
                || widget.getX() > MinecraftClient.getInstance().getWindow().getScaledWidth()
                || widget.getY() > MinecraftClient.getInstance().getWindow().getScaledHeight()) {
            return;
        }
        points.add(new SnapPoint(widget.getX() + widget.width / 2, widget.getY() + widget.height / 2, Math.min(widget.width, widget.height) / 2));
    }

    @Unique
    private void controlifyIntegrations$addViewQuestSnapPoints(Set<SnapPoint> points, ViewQuestPanel viewQuestPanel) {
        if (viewQuestPanel == null) {
            return;
        }
        viewQuestPanel.getWidgets().forEach(subwidget -> {
            if (subwidget instanceof SimpleButton) {
                points.add(new SnapPoint(subwidget.getX() + subwidget.width / 2, subwidget.getY() + subwidget.height / 2, Math.min(subwidget.width, subwidget.height) / 2));
                return;
            }
            switch (subwidget.getClass().getSimpleName()) {
//                case "QuestDescriptionField":
//                case "BlankPanel":
                case "CloseViewQuestButton":
                case "PinViewQuestButton":
                case "ViewQuestLinksButton":
                    controlifyIntegrations$addWidgetSnapPoint(points, subwidget);
                    return;
//                case "QuestDescriptionField":
//                    TextField questDescriptionField = (TextField) subwidget;
                case "BlankPanel":
                    BlankPanel blankPanel = (BlankPanel) subwidget;
                    blankPanel.getWidgets().forEach(subsubwidget -> {
                        ControlifyIntegrations.LOGGER.info("#### ViewQuest #### Adding snap point for " + subsubwidget.getClass().getSimpleName());
                        controlifyIntegrations$addWidgetSnapPoint(points, subsubwidget);
                    });
//                default:
//                    break;
            }
//              ControlifyIntegrations.LOGGER.info("#### ViewQuest #### Not adding snap point for " + subwidget.getClass().getSimpleName());
//            controlifyIntegrations$addWidgetSnapPoint(points, subwidget);
        });



//        Adding snap points for quest screen
//[15:54:37] [Render thread/INFO] [controlifyintegrations/]: Adding snap point for QuestDescriptionField
//                [15:54:37] [Render thread/INFO] [controlifyintegrations/]: Adding snap point for BlankPanel
//                [15:54:37] [Render thread/INFO] [controlifyintegrations/]: Adding snap point for CloseViewQuestButton
//                [15:54:37] [Render thread/INFO] [controlifyintegrations/]: Adding snap point for PinViewQuestButton
//                [15:54:37] [Render thread/INFO] [controlifyintegrations/]: Adding snap point for ViewQuestLinksButton
//                [15:54:37] [Render thread/INFO] [controlifyintegrations/]: Adding snap point for SimpleButton
//                [15:54:37] [Render thread/INFO] [controlifyintegrations/]: Adding snap point for SimpleButton
    }
}
