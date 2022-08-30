package homeostatic.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import homeostatic.common.fluid.HomeostaticFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;

import homeostatic.Homeostatic;
import homeostatic.common.fluid.DrinkingFluid;
import homeostatic.common.fluid.DrinkingFluidManager;

public class DrinkingFluidsProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<ResourceLocation, DrinkingFluid> DRINKING_FLUIDS = new HashMap<>();
    private final DataGenerator dataGenerator;
    private final String modid;

    public DrinkingFluidsProvider(DataGenerator dataGenerator, String modid) {
        this.dataGenerator = dataGenerator;
        this.modid = modid;
    }

    protected void addWaterItems() {
        add(Fluids.WATER.getRegistryName(), 3, 0.0F, 45, 200, 0.2F);
        add(HomeostaticFluids.PURIFIED_WATER.getRegistryName(), 9, 0.7F,  0, 0, 0.0F);
    }

    protected void add(ResourceLocation loc, int amount, float saturation, int potency, int duration, float chance) {
        DRINKING_FLUIDS.put(loc, new DrinkingFluid(loc, amount, saturation, potency, duration, chance));
    }

    @Override
    public String getName() {
        return "Homeostatic - Water Items";
    }

    @Override
    public void run(HashCache pCache) throws IOException {
        addWaterItems();

        Path output = dataGenerator.getOutputFolder();

        for (Map.Entry<ResourceLocation, DrinkingFluid> entry : DRINKING_FLUIDS.entrySet()) {
            Path waterItemPath = getPath(output, entry.getKey());

            try {
                DataProvider.save(GSON, pCache, DrinkingFluidManager.parseWaterItem(entry.getValue()), waterItemPath);
            }
            catch (IOException e) {
                Homeostatic.LOGGER.error("Couldn't save homeostatic water_items %s %s", waterItemPath, e);
            }
        }
    }

    private static Path getPath(Path output, ResourceLocation loc) {
        return output.resolve("data/" + loc.getNamespace() + "/environment/fluids/" + loc.getPath() + ".json");
    }

}
