package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.item.crafting.CustomFlowerDyeRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class BovineRecipeSerializers {
    private static final RegistrationProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrationProvider.get(Registries.RECIPE_SERIALIZER, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<CustomFlowerDyeRecipe>> CUSTOM_FLOWER_DYE = RECIPE_SERIALIZERS.register("crafting_special_customflowerdye", () -> new SimpleCraftingRecipeSerializer<>(CustomFlowerDyeRecipe::new));

    public static void register() {

    }
}
