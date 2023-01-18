package net.merchantpug.bovinesandbuttercups.integration.jei.recipe;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.access.ItemStackAccess;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomFlowerDyeRecipeMaker {
    public static List<CraftingRecipe> createRecipes(Level level) {
        List<CraftingRecipe> recipes = new ArrayList<>();

        BovineRegistryUtil.flowerTypeStream(level).forEach(flowerType -> {
            Optional<CraftingRecipe> recipe = createRecipe(level, flowerType);
            recipe.ifPresent(recipes::add);
        });

        return recipes;
    }

    private static Optional<CraftingRecipe> createRecipe(Level level, FlowerType flowerType) {
        ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
        ((ItemStackAccess)(Object)stack).bovinesandbuttercups$setLevel(level);
        ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(level, flowerType);

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("Type", flowerTypeLocation.toString());
        stack.addTagElement("BlockEntityTag", compoundTag);

        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(stack));

        Optional<ItemStack> output = flowerType.dyeCraftResult();
        if (flowerType.dyeCraftResult().isEmpty()) {
            return Optional.empty();
        }

        ResourceLocation id = BovinesAndButtercups.asResource("bovinesandbuttercups.custom.flower.dye." + flowerTypeLocation.toLanguageKey());
        return Optional.of(new ShapelessRecipe(id, "bovinesandbuttercups.custom.flower.dye", output.get().copy(), inputs));
    }
}
