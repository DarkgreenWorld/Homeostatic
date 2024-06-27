package homeostatic.common.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import homeostatic.common.capabilities.CapabilityRegistry;
import homeostatic.Homeostatic;
import homeostatic.util.WaterHelper;

public class FluidHandlerItem extends FluidHandlerItemStack {

    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

    public FluidHandlerItem(@NotNull ItemStack container, int capacity) {
        super(container, capacity);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluidStack) {
        // Allow recipes to register any liquid. Will default to hydration registry if not running datagen
        return Homeostatic.DATA_GEN || WaterHelper.getFluidHydration(fluidStack.getFluid()) != null;
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return CapabilityRegistry.FLUID_ITEM_CAPABILITY.orEmpty(capability, holder);
    }

}