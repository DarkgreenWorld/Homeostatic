package homeostatic.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;

import homeostatic.common.temperature.TemperatureRange;
import homeostatic.config.ConfigHandler;
import homeostatic.platform.Services;
import homeostatic.util.Alignment;
import homeostatic.util.ColorHelper;
import homeostatic.util.FontHelper;
import homeostatic.util.TempHelper;
import homeostatic.common.temperature.TemperatureDirection;

public class TemperatureInfo extends Overlay {

    public TemperatureInfo() {}

    @Override
    public void render(GuiGraphics guiGraphics, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight) {
        final Player player = mc.player;

        if (player == null) return;

        Services.PLATFORM.getTemperatureData(player).ifPresent(data -> {
            TemperatureDirection skinTemperatureDirection = TempHelper.getSkinTemperatureDirection(
                data.getLocalTemperature(), data.getSkinTemperature());
            TemperatureDirection coreTemperatureDirection = TempHelper.getCoreTemperatureDirection(
                data.getLastSkinTemperature(), data.getCoreTemperature(), data.getSkinTemperature());
            String skinIcon = skinTemperatureDirection.icon;
            String coreIcon = coreTemperatureDirection.icon;

            String localTemp = String.format(" %.2f",
                TempHelper.convertMcTemp(data.getLocalTemperature(), ConfigHandler.Client.useFahrenheit()));
            String skinTemp = String.format("s: %.2f%s",
                TempHelper.convertMcTemp(data.getSkinTemperature(), ConfigHandler.Client.useFahrenheit()), skinIcon);
            String coreTemp = String.format(" c: %.2f%s ",
                TempHelper.convertMcTemp(data.getCoreTemperature(), ConfigHandler.Client.useFahrenheit()), coreIcon);
            Tuple<TemperatureRange, Integer> localRangeStep =
                TempHelper.getLocalTemperatureRangeStep(data.getLocalTemperature());
            Tuple<TemperatureRange, Integer> coreRangeStep =
                TempHelper.getBodyTemperatureRangeStep(data.getCoreTemperature());
            Tuple<TemperatureRange, Integer> skinRangeStep =
                TempHelper.getBodyTemperatureRangeStep(data.getSkinTemperature());

            int localTempWidth = mc.font.width(localTemp);
            int skinTempWidth = mc.font.width(skinTemp);
            int coreTempWidth = mc.font.width(coreTemp);
            int y = Alignment.getTextY(ConfigHandler.Client.debugPosition(), scaledHeight, 1, mc.font.lineHeight,
                ConfigHandler.Client.debugOffsetY(), 1.0F);

            FontHelper.draw(mc, guiGraphics, localTemp,
                Alignment.getTextX(ConfigHandler.Client.debugPosition(), scaledWidth, localTempWidth,
                ConfigHandler.Client.debugOffsetX(), 1.0F), y, ColorHelper.getLocalTemperatureColor(localRangeStep), false);
            FontHelper.draw(mc, guiGraphics, skinTemp,
                Alignment.getTextX(ConfigHandler.Client.debugPosition(), scaledWidth, skinTempWidth,
                ConfigHandler.Client.debugOffsetX(), 1.0F), y + mc.font.lineHeight,
                ColorHelper.getTemperatureColor(skinRangeStep), false);
            FontHelper.draw(mc, guiGraphics, coreTemp,
                Alignment.getTextX(ConfigHandler.Client.debugPosition(), scaledWidth, coreTempWidth,
                ConfigHandler.Client.debugOffsetX(), 1.0F), y + (mc.font.lineHeight * 2),
                ColorHelper.getTemperatureColor(coreRangeStep), false);
        });
    }

}
