package net.merchantpug.bovinesandbuttercups.api.condition.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class BlockConditionType<CC extends ConditionConfiguration<BlockInWorld>> extends ConditionType<BlockInWorld, CC> {
    public static final Codec<BlockConditionType<?>> CODEC = Services.PLATFORM.getBlockConditionTypeCodec();

    public BlockConditionType(MapCodec<CC> codec) {
        this.configuredCodec = RecordCodecBuilder.create(instance ->
                instance.group(
                        codec.forGetter(ConfiguredCondition::getConfiguration)
                ).apply(instance, (t1) -> new ConfiguredCondition<>(this, t1)));
    }

    @Override
    public Codec<ConfiguredCondition<BlockInWorld, CC, ConditionType<BlockInWorld, CC>>> getCodec() {
        return configuredCodec;
    }
}
