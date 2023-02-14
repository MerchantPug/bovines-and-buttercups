package net.merchantpug.bovinesandbuttercups.integration.rei;

import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.forge.REIPluginCommon;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;

@REIPluginCommon
public class BovineReiPlugin implements REIServerPlugin {
    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.registerNbt(BovineItems.CUSTOM_FLOWER.get());
        registry.registerNbt(BovineItems.CUSTOM_MUSHROOM.get());
        registry.registerNbt(BovineItems.CUSTOM_MUSHROOM_BLOCK.get());
        registry.registerNbt(BovineItems.NECTAR_BOWL.get());
    }
}
