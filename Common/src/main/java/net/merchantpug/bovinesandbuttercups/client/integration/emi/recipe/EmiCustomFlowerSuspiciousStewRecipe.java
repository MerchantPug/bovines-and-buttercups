package net.merchantpug.bovinesandbuttercups.client.integration.emi.recipe;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class EmiCustomFlowerSuspiciousStewRecipe extends EmiPatternCraftingRecipe {
    public EmiCustomFlowerSuspiciousStewRecipe() {
        super(List.of(
                        EmiStack.of(Items.BOWL),
                        EmiStack.of(Items.RED_MUSHROOM),
                        EmiStack.of(Items.BROWN_MUSHROOM),
                        EmiIngredient.of(BovineRegistryUtil.flowerTypeStream().filter(flowerType -> flowerType.stewEffectInstance().isPresent()).map(flowerType -> {
                            ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
                            ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(flowerType);

                            CompoundTag compoundTag = new CompoundTag();
                            compoundTag.putString("Type", flowerTypeLocation.toString());
                            stack.addTagElement("BlockEntityTag", compoundTag);
                            return (EmiIngredient) EmiStack.of(stack);

                        }).collect(Collectors.toList()))),
                EmiStack.of(Items.SUSPICIOUS_STEW), BovinesAndButtercups.asResource("suspicious_stew_from_custom_flowers"));
    }

    @Override
    public SlotWidget getInputWidget(int slot, int x, int y) {
        if (slot == 0) {
            return new SlotWidget(EmiStack.of(Items.BOWL), x, y);
        } else if (slot == 1) {
            return new SlotWidget(EmiStack.of(Items.RED_MUSHROOM), x, y);
        } else if (slot == 2) {
            return new SlotWidget(EmiStack.of(Items.BROWN_MUSHROOM), x, y);
        } else if (slot == 3) {
            return new GeneratedSlotWidget(r -> EmiStack.of(getFlower(r)), unique, x, y);
        }
        return new SlotWidget(EmiStack.EMPTY, x, y);
    }

    @Override
    public SlotWidget getOutputWidget(int x, int y) {
        return new GeneratedSlotWidget(r -> {
            Optional<MobEffectInstance> effectInstance = CustomFlowerItem.getFlowerTypeFromTag(getFlower(r)).get().stewEffectInstance();
            if (effectInstance.isPresent()) {
                ItemStack stack = new ItemStack(Items.SUSPICIOUS_STEW);
                SuspiciousStewItem.saveMobEffects(stack, List.of(new SuspiciousEffectHolder.EffectEntry(effectInstance.get().getEffect(), effectInstance.get().getDuration())));
                return EmiStack.of(stack);
            }
            return EmiStack.of(ItemStack.EMPTY);
        }, unique, x, y);
    }

    private ItemStack getFlower(Random random) {
        List<FlowerType> types = BovineRegistryUtil.flowerTypeStream().filter(flowerType -> flowerType.stewEffectInstance().isPresent()).toList();
        FlowerType flowerType = types.get(random.nextInt(types.size()));

        ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
        ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(flowerType);

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("Type", flowerTypeLocation.toString());
        stack.addTagElement("BlockEntityTag", compoundTag);

        return stack;
    }
}
