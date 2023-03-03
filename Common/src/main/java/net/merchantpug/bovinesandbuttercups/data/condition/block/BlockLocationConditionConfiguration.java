package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class BlockLocationConditionConfiguration extends ConditionConfiguration<BlockInWorld> {
    public static final MapCodec<BlockLocationConditionConfiguration> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.either(TagKey.hashedCodec(Registry.BLOCK_REGISTRY), Registry.BLOCK.holderByNameCodec()).fieldOf("location").xmap(tagKeyBlockEither ->
                            tagKeyBlockEither.map(Registry.BLOCK::getOrCreateTag, block ->(HolderSet<Block>)HolderSet.direct(block)),
                            holders -> holders.unwrap().mapBoth(tagKey -> tagKey, holders1 -> holders1.get(0))).forGetter(BlockLocationConditionConfiguration::getBlocks)
    ).apply(builder, BlockLocationConditionConfiguration::new));

    private final HolderSet<Block> blocks;

    public BlockLocationConditionConfiguration(HolderSet<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean test(BlockInWorld block) {
        return block.getState().is(blocks);
    }

    public HolderSet<Block> getBlocks() {
        return blocks;
    }
}
