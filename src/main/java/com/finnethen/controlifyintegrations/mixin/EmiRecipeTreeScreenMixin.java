package com.finnethen.controlifyintegrations.mixin;

import com.finnethen.controlifyintegrations.ControlifyIntegrations;
import com.finnethen.controlifyintegrations.integrations.EmiRecipeTreeScreenProcessor;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.screen.BoMScreen;
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

import static java.lang.Math.min;

@Mixin(value = BoMScreen.class, remap = false)
public abstract class EmiRecipeTreeScreenMixin extends Screen implements ISnapBehaviour, ScreenProcessorProvider {
    @Shadow
    private List<EmiRecipeTreeScreenNodeAccessor> nodes;

    @Shadow
    private List<EmiRecipeTreeScreenCostAccessor> costs;

    @Shadow
    private int nodeWidth, nodeHeight;

    @Shadow
    private double offX, offY;

    @Shadow
    private Bounds batches, mode, help;

    @Shadow public abstract float getScale();

    @Unique
    private final EmiRecipeTreeScreenProcessor controlify$processor = new EmiRecipeTreeScreenProcessor((BoMScreen) (Object) this);

    protected EmiRecipeTreeScreenMixin(Text title) {
        super(title);
    }

    @Override
    public Set<SnapPoint> getSnapPoints() {
        Set<SnapPoint> points = new HashSet<>();
        float scale = getScale();

        for (var node : nodes) {
            int x = (int) ((node.getX() + offX) * scale + (double) width / 2);
            int y = (int) ((node.getY() + offY) * scale + (double) height / 2);
            if (node.getNode().recipe != null) {
                points.add(new SnapPoint(new Vector2i(x-12, y), min(nodeWidth, nodeHeight) / 2));
                points.add(new SnapPoint(new Vector2i(x+12, y), min(nodeWidth, nodeHeight) / 2));
            } else {
                int tw = EmiRenderHelper.getAmountOverflow(node.callGetAmountText());
                x -= tw / 2;
                points.add(new SnapPoint(new Vector2i(x, y), min(nodeWidth, nodeHeight) / 2));
            }
        }

        for (var cost : costs) {
            int x = (int) ((cost.getX() + offX) * scale + (double) width / 2) + 8;
            int y = (int) ((cost.getY() + offY) * scale + (double) height / 2) + 8;
            points.add(new SnapPoint(new Vector2i(x, y), min(nodeWidth, nodeHeight) / 2));
        }

        // TODO: Need to handle large text offsets a little better
        points.add(new SnapPoint(new Vector2i(help.x() + help.width() / 2, help.y() + help.height() / 2), min(help.width(), help.height()) / 2));
        {
            int x = (int) ((batches.x() + offX) * scale + (double) width / 2) + batches.width() / 2;
            int y = (int) ((batches.y() + offY) * scale + (double) height / 2) + batches.height() / 2;
            points.add(new SnapPoint(new Vector2i(x, y), min(batches.width(), batches.height()) / 2));
        }
        {
            int x = (int) ((mode.x() + offX) * scale + (double) width / 2) + mode.width() / 2;
            int y = (int) ((mode.y() + offY) * scale + (double) height / 2) + mode.height() / 2;
            points.add(new SnapPoint(new Vector2i(x, y), min(mode.width(), mode.height()) / 2));
        }

        return points;
    }

    @Override
    public ScreenProcessor<?> screenProcessor() {
        return controlify$processor;
    }
}
