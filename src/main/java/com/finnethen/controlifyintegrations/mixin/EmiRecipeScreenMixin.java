package com.finnethen.controlifyintegrations.mixin;

import com.finnethen.controlifyintegrations.integrations.EmiRecipeScreenProcessor;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.widget.TextureWidget;
import dev.emi.emi.screen.RecipeScreen;
import dev.emi.emi.screen.RecipeTab;
import dev.emi.emi.screen.WidgetGroup;
import dev.emi.emi.screen.widget.ResolutionButtonWidget;
import dev.emi.emi.screen.widget.SizedButtonWidget;
import dev.emi.emi.widget.RecipeBackground;
import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.finnethen.controlifyintegrations.integrations.EmiSupport.getWidgetSnapPoint;
import static java.lang.Math.min;

@Mixin(value = RecipeScreen.class, remap = false)
public class EmiRecipeScreenMixin extends Screen implements ISnapBehaviour, ScreenProcessorProvider {
    @Shadow private List<SizedButtonWidget> arrows;
    @Shadow private List<WidgetGroup> currentPage;
    @Shadow private ResolutionButtonWidget resolutionButton;
    @Shadow private List<RecipeTab> tabs;
    @Shadow private int tab;
    @Shadow private int minimumWidth;
    @Shadow int backgroundWidth;
    @Shadow int x;
    @Shadow int y;

    @Unique
    private final EmiRecipeScreenProcessor controlify$processor = new EmiRecipeScreenProcessor((RecipeScreen) (Object) this);

    protected EmiRecipeScreenMixin(Text title) {
        super(title);
    }

    @Override
    public Set<SnapPoint> getSnapPoints() {
        Set<SnapPoint> points = new HashSet<>();

        for (var arrow : arrows) {
            if (arrow.active) {
                points.add(getWidgetSnapPoint(arrow));
            }
        }
        for (var widgetGroup : currentPage) {
            for (var widget: widgetGroup.widgets) {
                if (widget instanceof RecipeBackground || widget instanceof TextureWidget) {
                    continue;
                }
                var bounds = widget.getBounds();
                points.add(new SnapPoint(new Vector2i(widgetGroup.x + bounds.x() + bounds.width() / 2, widgetGroup.y + bounds.y() + bounds.height() / 2), min(bounds.width(), bounds.height()) / 2));
            }
        }
        if (resolutionButton != null && resolutionButton.active) {
            points.add(getWidgetSnapPoint(resolutionButton));
        }

        points.add(getWorkstationHeaderSnapPoint());

        return points;
    }

    @Unique
    private SnapPoint getWorkstationHeaderSnapPoint() {
        RecipeTab tab = tabs.get(this.tab);
        Text text = tab.category.getName();
        if (client.textRenderer.getWidth(text) > minimumWidth - 40) {
            int extraWidth = client.textRenderer.getWidth("...");
            text = EmiPort.literal(client.textRenderer.trimToWidth(text, (minimumWidth - 40) - extraWidth).getString() + "...");
        }
        int labelWidth = client.textRenderer.getWidth(text)/2;
        int labelHeight = client.textRenderer.getWrappedLinesHeight(text,  minimumWidth - 40);
        int snapPointX = x + (backgroundWidth / 2);
        int snapPointY = y + 7 + (labelHeight/2);
        return new SnapPoint(new Vector2i(snapPointX, snapPointY), min(labelWidth, labelHeight) / 2);
    }

    @Override
    public ScreenProcessor<?> screenProcessor() {
        return controlify$processor;
    }
}
