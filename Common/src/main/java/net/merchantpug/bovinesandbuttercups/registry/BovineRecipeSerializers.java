package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.item.crafting.CustomFlowerDyeRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public class BovineRecipeSerializers {
    public static final RegistrationProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrationProvider.get(Registry.RECIPE_SERIALIZER, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer<CustomFlowerDyeRecipe>> CUSTOM_FLOWER_DYE = RECIPE_SERIALIZERS.register("crafting_special_customflowerdye", () -> new SimpleRecipeSerializer<>(CustomFlowerDyeRecipe::new));

    public static void init() {

    }
}
