package homeostatic.common.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import static homeostatic.Homeostatic.loc;

public class HomeostaticDamageTypes {

    public static final ResourceKey<DamageType> HYPERTHERMIA_KEY = ResourceKey.create(Registries.DAMAGE_TYPE, loc("hyperthermia"));
    private static final DamageType HYPERTHERMIA = new DamageType("hasHyperthermia", 0.0F);
    public static final ResourceKey<DamageType> SCALDING_KEY = ResourceKey.create(Registries.DAMAGE_TYPE, loc("scalding"));
    private static final DamageType SCALDING = new DamageType("isScalding", 0.0F);
    public static final ResourceKey<DamageType> DEHYDRATION_KEY = ResourceKey.create(Registries.DAMAGE_TYPE, loc("dehydration"));
    private static final DamageType DEHYDRATION = new DamageType("hasDehydration", 0.0F);

    public static void init() {}

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(HYPERTHERMIA_KEY, HYPERTHERMIA);
        context.register(SCALDING_KEY, SCALDING);
        context.register(DEHYDRATION_KEY, DEHYDRATION);
    }

}
