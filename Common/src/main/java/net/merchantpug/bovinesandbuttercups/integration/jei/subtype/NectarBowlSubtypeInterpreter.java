package net.merchantpug.bovinesandbuttercups.integration.jei.subtype;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
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
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Source")) {
            ConfiguredCowType<?, ?> configuredCowType = BovineRegistryUtil.getConfiguredCowTypeFromKey(ResourceLocation.tryParse(tag.getString("Source")), BovineCowTypes.FLOWER_COW_TYPE.get());
            if (configuredCowType != configuredCowType.getCowType().getDefaultCowType().getSecond()) {
                return tag.getString("Source");
            }
        }
        return IIngredientSubtypeInterpreter.NONE;
    }
}
