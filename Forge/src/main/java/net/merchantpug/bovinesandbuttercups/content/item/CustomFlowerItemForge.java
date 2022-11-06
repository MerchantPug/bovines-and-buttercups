package net.merchantpug.bovinesandbuttercups.content.item;

import net.merchantpug.bovinesandbuttercups.client.BovinesBEWLR;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class CustomFlowerItemForge extends CustomFlowerItem {
    public CustomFlowerItemForge(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BovinesBEWLR.BLOCK_ENTITY_WITHOUT_LEVEL_RENDERER;
            }
        });
    }
}