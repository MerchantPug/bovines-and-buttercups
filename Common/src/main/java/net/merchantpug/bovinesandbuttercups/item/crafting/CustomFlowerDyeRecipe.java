package net.merchantpug.bovinesandbuttercups.item.crafting;

import net.merchantpug.bovinesandbuttercups.access.CraftingContainerAccess;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineRecipeSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CustomFlowerDyeRecipe extends CustomRecipe {
    public CustomFlowerDyeRecipe(ResourceLocation location) {
        super(location);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack stack = ItemStack.EMPTY;

        if (!stack.isEmpty()) return false;

        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack2 = container.getItem(i);
            if (stack2.isEmpty()) continue;
            if (stack2.is(Services.PLATFORM.getCustomFlowerItem())) {
                stack = stack2;
            }
        }

        return !stack.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        ItemStack stack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;

        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack2 = container.getItem(i);
            if (stack2.isEmpty()) continue;
            if (stack2.is(Services.PLATFORM.getCustomFlowerItem())) {
                stack = stack2;
            }
        }

        if (stack.isEmpty()) {
            return stack;
        }
        Optional<FlowerType> flowerType = CustomFlowerItem.getFlowerTypeFromTag(((CraftingContainerAccess)container).bovinesandbuttercups$getLevel(), stack);
        if (flowerType.isPresent() && flowerType.get().dyeCraftResult().isPresent()) {
            dyeStack = flowerType.get().dyeCraftResult().get().copy();
        }
        return dyeStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BovineRecipeSerializers.CUSTOM_FLOWER_DYE.get();
    }
}
