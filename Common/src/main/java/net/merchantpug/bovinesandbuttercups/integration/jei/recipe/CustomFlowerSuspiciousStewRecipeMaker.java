package net.merchantpug.bovinesandbuttercups.integration.jei.recipe;

import mezz.jei.api.constants.ModIds;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomFlowerSuspiciousStewRecipeMaker {
    public static List<CraftingRecipe> createRecipes() {
        List<CraftingRecipe> recipes = new ArrayList<>();

        BovineRegistryUtil.flowerTypeStream().forEach(flowerType -> {
            Optional<CraftingRecipe> recipe = createRecipe(flowerType);
            recipe.ifPresent(recipes::add);
        });

        return recipes;
    }

    private static Optional<CraftingRecipe> createRecipe(FlowerType flowerType) {
        ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
        ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(flowerType);

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("Type", flowerTypeLocation.toString());
        stack.addTagElement("BlockEntityTag", compoundTag);

        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(Blocks.BROWN_MUSHROOM.asItem()), Ingredient.of(Blocks.RED_MUSHROOM.asItem()), Ingredient.of(Items.BOWL), Ingredient.of(stack));

        ItemStack output = new ItemStack(Items.SUSPICIOUS_STEW, 1);
        if (flowerType.stewEffectInstance().isEmpty()) {
            return Optional.empty();
        }
        SuspiciousStewItem.saveMobEffect(output, flowerType.stewEffectInstance().get().getEffect(), flowerType.stewEffectInstance().get().getDuration());

        ResourceLocation id = new ResourceLocation(ModIds.MINECRAFT_ID, "bovinesandbuttercups.custom.flower.suspicious.stew." + flowerTypeLocation.toLanguageKey());
        return Optional.of(new ShapelessRecipe(id, "bovinesandbuttercups.custom.flower.suspicious.stew", CraftingBookCategory.MISC, output, inputs));
    }
}