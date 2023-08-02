package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.content.block.CustomFlowerBlock;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class CustomFlowerTypeCondition extends ConditionConfiguration<BlockInWorld> {
    public static MapCodec<CustomFlowerTypeCondition> getCodec() {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                ResourceLocation.CODEC.fieldOf("location").forGetter(CustomFlowerTypeCondition::getLocation)
        ).apply(builder, CustomFlowerTypeCondition::new));
    }

    private final ResourceLocation location;

    public CustomFlowerTypeCondition(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public boolean test(BlockInWorld block) {
        return block.getState().getBlock() instanceof CustomFlowerBlock && block.getEntity() instanceof CustomFlowerBlockEntity be && BovineRegistryUtil.getFlowerTypeKey(be.getFlowerType()).equals(location);
    }

    public ResourceLocation getLocation() {
        return location;
    }
}
