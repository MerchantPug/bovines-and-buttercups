package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class BlockLocationCondition extends ConditionConfiguration<BlockInWorld> {
    public static final MapCodec<BlockLocationCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.either(TagKey.hashedCodec(Registries.BLOCK), BuiltInRegistries.BLOCK.holderByNameCodec()).fieldOf("location").xmap(tagKeyBlockEither ->
                            tagKeyBlockEither.map(BuiltInRegistries.BLOCK::getOrCreateTag, block ->(HolderSet<Block>)HolderSet.direct(block)),
                            holders -> holders.unwrap().mapBoth(tagKey -> tagKey, holders1 -> holders1.get(0))).forGetter(BlockLocationCondition::getBlocks)
    ).apply(builder, BlockLocationCondition::new));

    private final HolderSet<Block> blocks;

    public BlockLocationCondition(HolderSet<Block> blocks) {
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
