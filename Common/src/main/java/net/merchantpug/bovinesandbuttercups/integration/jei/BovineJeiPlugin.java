package net.merchantpug.bovinesandbuttercups.integration.jei;

// TODO: Reintroduce JEI compat
/*
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.integration.jei.recipe.CustomFlowerDyeRecipeMaker;
import net.merchantpug.bovinesandbuttercups.integration.jei.recipe.CustomFlowerSuspiciousStewRecipeMaker;
import net.merchantpug.bovinesandbuttercups.integration.jei.subtype.CustomFlowerSubtypeInterpreter;
import net.merchantpug.bovinesandbuttercups.integration.jei.subtype.CustomMushroomSubtypeInterpreter;
import net.merchantpug.bovinesandbuttercups.integration.jei.subtype.NectarBowlSubtypeInterpreter;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.Optional;

public class BovineJeiPlugin implements IModPlugin {
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Optional<RecipeType<?>> recipeType = registration.getJeiHelpers().getRecipeType(new ResourceLocation(ModIds.MINECRAFT_ID, "crafting"));
        recipeType.ifPresent(craftingRecipeType -> {
            registration.addRecipes((RecipeType<CraftingRecipe>) craftingRecipeType, CustomFlowerDyeRecipeMaker.createRecipes());
            registration.addRecipes((RecipeType<CraftingRecipe>) craftingRecipeType, CustomFlowerSuspiciousStewRecipeMaker.createRecipes());
        });
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(BovineItems.CUSTOM_FLOWER.get(), CustomFlowerSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(BovineItems.CUSTOM_MUSHROOM.get(), CustomMushroomSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(BovineItems.CUSTOM_MUSHROOM_BLOCK.get(), CustomMushroomSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(BovineItems.NECTAR_BOWL.get(), NectarBowlSubtypeInterpreter.INSTANCE);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return BovinesAndButtercups.asResource("bovinesandbuttercups");
    }

}
 */
