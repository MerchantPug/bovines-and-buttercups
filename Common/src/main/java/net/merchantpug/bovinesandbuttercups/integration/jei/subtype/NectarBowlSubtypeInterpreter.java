package net.merchantpug.bovinesandbuttercups.integration.jei.subtype;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class NectarBowlSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final NectarBowlSubtypeInterpreter INSTANCE = new NectarBowlSubtypeInterpreter();

    @Override
    public String apply(ItemStack stack, UidContext context) {
        if (!stack.hasTag()) {
            return IIngredientSubtypeInterpreter.NONE;
        }
        CompoundTag compound = stack.getTag().getCompound("BlockEntityTag");
        if (compound.contains("Source")) {
            ConfiguredCowType<?, ?> configuredCowType = BovineRegistryUtil.getConfiguredCowTypeFromKey(ResourceLocation.tryParse(compound.getString("Source")));
            if (configuredCowType != configuredCowType.cowType().getDefaultCowType().getSecond()) {
                return compound.getString("Source");
            }
        }
        return IIngredientSubtypeInterpreter.NONE;
    }
}