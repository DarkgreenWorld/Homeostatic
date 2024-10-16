package homeostatic;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import homeostatic.common.biome.BiomeCategory;
import homeostatic.common.biome.BiomeCategoryManager;
import homeostatic.common.biome.BiomeData;
import homeostatic.common.biome.BiomeRegistry;
import homeostatic.common.block.HomeostaticBlocks;
import homeostatic.common.capabilities.CapabilityRegistry;
import homeostatic.common.CreativeTabs;
import homeostatic.common.effect.HomeostaticEffects;
import homeostatic.common.fluid.ForgeFluidType;
import homeostatic.common.fluid.HomeostaticFluids;
import homeostatic.common.HomeostaticModule;
import homeostatic.common.item.HomeostaticItems;
import homeostatic.common.recipe.HomeostaticRecipes;
import homeostatic.event.CapabilityEventHandler;
import homeostatic.event.ServerEventListener;
import homeostatic.network.NetworkHandler;
import homeostatic.util.RegistryHelper;

@Mod(Homeostatic.MODID)
public class HomeostaticForge {

    public HomeostaticForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        registryInit();
        bus.register(RegistryListener.class);
        bus.register(CapabilityRegistry.class);
        bus.addListener(this::setup);
        Homeostatic.init();
        Homeostatic.initConfig();
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::serverStart);
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ServerEventListener.class);
        MinecraftForge.EVENT_BUS.register(CapabilityEventHandler.class);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> HomeostaticClientForge::new);
    }

    public static final class RegistryListener {

        private static boolean setupDone = false;

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void registerEvent(RegisterEvent event) {
            event.register(ForgeRegistries.FLUID_TYPES.get().getRegistryKey(), ForgeFluidType::initTypes);
            CreativeTabs.init();
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void setupRegistries(FMLConstructModEvent event) {
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

            if (setupDone) {
                return;
            }
            setupDone = true;

            HomeostaticModule.initRegistries(bus);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void registerCreativeTab(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES
                    || event.getTab() == CreativeTabs.ALL_ITEMS_TAB.get()) {
                for (Map.Entry<ResourceLocation, Item> entry : HomeostaticItems.getAll().entrySet()) {
                    Item item = entry.getValue();

                    event.accept(new ItemStack(item));
                }
            }
        }

        @SubscribeEvent
        public static void setup(FMLCommonSetupEvent event) {
            NetworkHandler.init();
        }

    }

    private void registryInit() {
        bind(ForgeRegistries.BLOCKS.getRegistryKey(), HomeostaticBlocks::init);
        bind(ForgeRegistries.MOB_EFFECTS.getRegistryKey(), HomeostaticEffects::init);
        bind(ForgeRegistries.FLUIDS.getRegistryKey(), HomeostaticFluids::init);
        bind(ForgeRegistries.RECIPE_SERIALIZERS.getRegistryKey(), HomeostaticRecipes::init);
        bind(ForgeRegistries.ITEMS.getRegistryKey(), HomeostaticItems::init);
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }

    public void serverStart(ServerStartedEvent event) {
        Registry<Biome> biomeRegistry = RegistryHelper.getRegistry(event.getServer(), Registries.BIOME);

        for (Map.Entry<ResourceKey<Biome>, Biome> entry : biomeRegistry.entrySet()) {
            ResourceKey<Biome> biomeResourceKey = entry.getKey();
            ResourceLocation biomeName = biomeResourceKey.location();
            Holder<Biome> biomeHolder = biomeRegistry.getHolderOrThrow(biomeResourceKey);
            BiomeCategory.Type biomeCategory = BiomeCategoryManager.getBiomeCategory(biomeHolder);
            BiomeData biomeData = BiomeRegistry.getDataForBiome(biomeHolder);
            Biome biome = biomeHolder.value();
            Biome.Precipitation precipitation = getPrecipitation(biome);
            String temperatureModifier = biomeData.isFrozen() ? "FROZEN" : "NONE";
            float dayNightOffset = biomeData.getDayNightOffset(precipitation);
            double humidity = biomeData.getHumidity(precipitation);

            if (!biomeName.toString().equals("terrablender:deferred_placeholder")) {
                if (biomeCategory == BiomeCategory.Type.MISSING) {
                    Homeostatic.LOGGER.warn("Missing biome in registry, will set to neutral temperature for: {}", biomeName);
                }

                Homeostatic.LOGGER.debug("Biome: " + biomeName
                    + "\nprecipitation_type=" + precipitation
                    + "\ntemperature=" + biomeData.getTemperature(precipitation)
                    + "\ntemperatureModifier=" + temperatureModifier
                    + "\ndownfall=" + biome.getModifiedClimateSettings().downfall()
                    + "\ndayNightOffset=" + dayNightOffset
                    + "\nhumidity=" + humidity
                    + "\nbiomeCategory=" + biomeCategory);
            }
        }
    }

    /*
     * Mock for debugging purposes. Will not be 100% accurate, but should help map to older versions.
     */
    private Biome.Precipitation getPrecipitation(Biome biome) {
        if (!biome.hasPrecipitation()) {
            return Biome.Precipitation.NONE;
        }
        else {
            return biome.getBaseTemperature() <= 0.15F ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN;
        }
    }

}