package homeostatic.util;

import net.minecraft.world.level.Level;

import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;

import homeostatic.common.temperature.SubSeason;

public class SereneSeasonsHelper {

    public static int getSeasonDuration(Level level) {
        return SeasonHelper.getSeasonState(level).getSeasonDuration();
    }

    public static boolean isSeasonDimension(Level level) {
        return ServerConfig.isDimensionWhitelisted(level.dimension());
    }

    public static SubSeason getSubSeason(Level level) {
        return SubSeason.values()[SeasonHelper.getSeasonState(level).getSubSeason().ordinal()];
    }

}
