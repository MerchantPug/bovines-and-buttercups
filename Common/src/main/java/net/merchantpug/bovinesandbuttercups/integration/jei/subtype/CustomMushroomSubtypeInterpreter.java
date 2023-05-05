package net.merchantpug.bovinesandbuttercups.integration.jei.subtype;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CustomMushroomSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final CustomMushroomSubtypeInterpreter INSTANCE = new CustomMushroomSubtypeInterpreter();

    @Override
    public String apply(ItemStack stack, UidContext context) {
        if (!stack.hasTag()) {
            return IIngredientSubtypeInterpreter.NONE;
        }
        CompoundTag compound = stack.getTag().getCompound("BlockEntityTag");
        if (compound.contains("Type")) {
            MushroomType mushroomType = BovineRegistryUtil.getMushroomTypeFromKey(ResourceLocation.tryParse(compound.getString("Type")));
            if (mushroomType != null && !mushroomType.equals(MushroomType.MISSING)) {
                return compound.getString("Type");
            }
        }
        return IIngredientSubtypeInterpreter.NONE;
    }
}
