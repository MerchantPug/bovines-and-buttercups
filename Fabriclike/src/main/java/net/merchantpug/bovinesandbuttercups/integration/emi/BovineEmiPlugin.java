package net.merchantpug.bovinesandbuttercups.integration.emi;

import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.integration.emi.recipe.EmiCustomFlowerSuspiciousStewRecipe;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BovineEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        EmiStack customFlower = EmiStack.of(BovineItems.CUSTOM_FLOWER.get()).comparison(comparison -> comparison.copy().nbt(true).build());
        EmiStack customMushroom = EmiStack.of(BovineItems.CUSTOM_MUSHROOM.get()).comparison(comparison -> comparison.copy().nbt(true).build());
        EmiStack customMushroomBlock = EmiStack.of(BovineItems.CUSTOM_MUSHROOM_BLOCK.get()).comparison(comparison -> comparison.copy().nbt(true).build());

        registry.addEmiStack(customFlower);
        registry.addEmiStack(customMushroom);
        registry.addEmiStack(customMushroomBlock);

        BovineRegistryUtil.flowerTypeStream().forEach(flowerType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
            ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(flowerType);

            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("Type", flowerTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", compoundTag);

            if (flowerType.dyeCraftResult().isPresent()) {
                registry.addRecipe(new EmiCraftingRecipe(List.of(EmiStack.of(stack)), EmiStack.of(flowerType.dyeCraftResult().get().copy()), BovinesAndButtercups.asResource("custom_flower_dye/" + EmiUtil.subId(flowerTypeLocation))));
            }
        });

        if (!BovineRegistryUtil.flowerTypeStream().filter(flowerType -> flowerType.stewEffectInstance().isPresent()).toList().isEmpty()) {
            registry.addRecipe(new EmiCustomFlowerSuspiciousStewRecipe());
        }
    }
}
