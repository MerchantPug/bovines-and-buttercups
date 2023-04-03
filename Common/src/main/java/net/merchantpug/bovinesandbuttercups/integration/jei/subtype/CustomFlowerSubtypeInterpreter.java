package net.merchantpug.bovinesandbuttercups.integration.jei.subtype;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CustomFlowerSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final CustomFlowerSubtypeInterpreter INSTANCE = new CustomFlowerSubtypeInterpreter();

    @Override
    public String apply(ItemStack stack, UidContext context) {
        if (!stack.hasTag()) {
            return IIngredientSubtypeInterpreter.NONE;
        }
        CompoundTag compound = stack.getTag().getCompound("BlockEntityTag");
        if (compound.contains("Type")) {
            FlowerType flowerType = BovineRegistryUtil.getFlowerTypeFromKey(ResourceLocation.tryParse(compound.getString("Type")));
            if (!flowerType.equals(FlowerType.MISSING)) {
                return compound.getString("Type");
            }
        }
        return IIngredientSubtypeInterpreter.NONE;
    }
}
