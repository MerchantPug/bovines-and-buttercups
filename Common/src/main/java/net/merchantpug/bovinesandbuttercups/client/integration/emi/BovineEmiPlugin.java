package net.merchantpug.bovinesandbuttercups.client.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.client.integration.emi.recipe.EmiCustomFlowerSuspiciousStewRecipe;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BovineEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        EmiStack customFlower = EmiStack.of(BovineItems.CUSTOM_FLOWER.get()).comparison(Comparison.compareNbt());
        EmiStack customMushroom = EmiStack.of(BovineItems.CUSTOM_MUSHROOM.get()).comparison(Comparison.compareNbt());
        EmiStack customMushroomBlock = EmiStack.of(BovineItems.CUSTOM_MUSHROOM_BLOCK.get()).comparison(Comparison.compareNbt());
        EmiStack nectarBowl = EmiStack.of(BovineItems.NECTAR_BOWL.get()).comparison(Comparison.compareNbt());

        registry.addEmiStack(customFlower);
        registry.addEmiStack(customMushroom);
        registry.addEmiStack(customMushroomBlock);
        registry.addEmiStack(nectarBowl);

        registry.removeEmiStacks(emiStack -> emiStack.getItemStack().getItem() instanceof CustomFlowerItem && (CustomFlowerItem.getFlowerTypeFromTag(emiStack.getItemStack()).isEmpty() || CustomFlowerItem.getFlowerTypeFromTag(emiStack.getItemStack()).get() == FlowerType.MISSING));
        registry.removeEmiStacks(emiStack -> emiStack.getItemStack().getItem() instanceof CustomMushroomItem && (CustomMushroomItem.getMushroomTypeFromTag(emiStack.getItemStack()).isEmpty() || CustomMushroomItem.getMushroomTypeFromTag(emiStack.getItemStack()).get() == MushroomType.MISSING));
        registry.removeEmiStacks(emiStack -> emiStack.getItemStack().getItem() instanceof CustomHugeMushroomItem && (CustomHugeMushroomItem.getMushroomTypeFromTag(emiStack.getItemStack()).isEmpty() || CustomHugeMushroomItem.getMushroomTypeFromTag(emiStack.getItemStack()).get() == MushroomType.MISSING));
        registry.removeEmiStacks(EmiStack.of(BovineItems.NECTAR_BOWL.get()));

        BovineRegistryUtil.flowerTypeStream().forEach(flowerType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
            ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(flowerType);

            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("Type", flowerTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", compoundTag);

            if (flowerType.dyeCraftResult().isPresent()) {
                registry.addRecipe(new EmiCraftingRecipe(List.of(EmiStack.of(stack)), EmiStack.of(flowerType.dyeCraftResult().get().copy()), BovinesAndButtercups.asResource("custom_flower_dye/" + flowerTypeLocation.getNamespace() + "/" + flowerTypeLocation.getPath())));
            }
        });

        if (!BovineRegistryUtil.flowerTypeStream().filter(flowerType -> flowerType.stewEffectInstance().isPresent()).toList().isEmpty()) {
            registry.addRecipe(new EmiCustomFlowerSuspiciousStewRecipe());
        }
    }
}
