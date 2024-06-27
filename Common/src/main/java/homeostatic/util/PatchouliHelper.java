package homeostatic.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import homeostatic.common.TagManager;
import homeostatic.common.fluid.HomeostaticFluids;
import homeostatic.common.item.HomeostaticItems;
import homeostatic.platform.Services;

public class PatchouliHelper {

    public static Recipe<?> getRecipe(Level level, ResourceLocation loc) {
        Ingredient ingredient = null;
        ItemStack armorStackBase = null;
        ItemStack armorStack = null;
        CompoundTag armorStackTag;
        ItemStack leatherFlaskBase = null;
        ItemStack leatherFlask = null;
        ShapelessRecipe customRecipe = null;
        boolean removalRecipe = false;

        if (loc != null && level != null) {
            String recipe = loc.getPath();

            switch (recipe) {
                case "insulation" -> {
                    ingredient = Ingredient.of(TagManager.Items.INSULATION);
                    armorStackBase = new ItemStack(Items.LEATHER_BOOTS);
                    armorStack = armorStackBase.copy();
                    armorStackTag = armorStack.getOrCreateTag();
                    armorStackTag.putBoolean("insulation", true);
                }
                case "remove_insulation" -> {
                    ingredient = Ingredient.of(Items.SHEARS);
                    armorStackBase = new ItemStack(Items.LEATHER_BOOTS);
                    armorStack = armorStackBase.copy();
                    armorStackTag = armorStackBase.getOrCreateTag();
                    armorStackTag.putBoolean("insulation", true);
                    removalRecipe = true;
                }
                case "waterproof" -> {
                    ingredient = Ingredient.of(TagManager.Items.WATERPROOF);
                    armorStackBase = new ItemStack(Items.LEATHER_HELMET);
                    armorStack = armorStackBase.copy();
                    armorStackTag = armorStack.getOrCreateTag();
                    armorStackTag.putBoolean("waterproof", true);
                }
                case "remove_waterproof" -> {
                    ingredient = Ingredient.of(Items.LAVA_BUCKET);
                    armorStackBase = new ItemStack(Items.LEATHER_HELMET);
                    armorStack = armorStackBase.copy();
                    armorStackTag = armorStackBase.getOrCreateTag();
                    armorStackTag.putBoolean("waterproof", true);
                    removalRecipe = true;
                }
                case "radiation_protection" -> {
                    ingredient = Ingredient.of(TagManager.Items.RADIATION_PROTECTION);
                    armorStackBase = new ItemStack(Items.LEATHER_CHESTPLATE);
                    armorStack = armorStackBase.copy();
                    armorStackTag = armorStack.getOrCreateTag();
                    armorStackTag.putBoolean("radiation_protection", true);
                }
                case "remove_radiation_protection" -> {
                    ingredient = Ingredient.of(Items.WATER_BUCKET);
                    armorStackBase = new ItemStack(Items.LEATHER_CHESTPLATE);
                    armorStack = armorStackBase.copy();
                    armorStackTag = armorStackBase.getOrCreateTag();
                    armorStackTag.putBoolean("radiation_protection", true);
                    removalRecipe = true;
                }
                case "filtered_water_flask" -> {
                    ingredient = Ingredient.of(HomeostaticItems.WATER_FILTER);
                    leatherFlaskBase = new ItemStack(HomeostaticItems.LEATHER_FLASK);
                    leatherFlask = WaterHelper.getFilledItem(
                        leatherFlaskBase,
                        HomeostaticFluids.PURIFIED_WATER,
                        (int) Services.PLATFORM.getFluidCapacity(leatherFlaskBase)
                    );
                }
            }
            if (armorStackBase != null) {
                Ingredient baseArmorIngredient = Ingredient.of(armorStackBase);
                NonNullList<Ingredient> armorInputs;

                if (removalRecipe) {
                    armorInputs = NonNullList.of(Ingredient.EMPTY, baseArmorIngredient, ingredient);
                }
                else {
                    armorInputs = NonNullList.of(Ingredient.EMPTY, baseArmorIngredient, ingredient, Ingredient.EMPTY, ingredient, ingredient);
                }

                customRecipe = new ShapelessRecipe("armor.enhancement", CraftingBookCategory.EQUIPMENT, armorStack, armorInputs);
            }
            else if (leatherFlask != null) {
                Ingredient baseFlaskIngredient = Ingredient.of(leatherFlaskBase.getItem());
                NonNullList<Ingredient> recipeInputs = NonNullList.of(Ingredient.EMPTY, baseFlaskIngredient, ingredient);

                customRecipe = new ShapelessRecipe("flask.filter", CraftingBookCategory.MISC, leatherFlask, recipeInputs);
            }
        }

        return customRecipe;
    }

}
