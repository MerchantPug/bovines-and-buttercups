package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.content.block.CustomFlowerBlock;
import net.merchantpug.bovinesandbuttercups.content.block.CustomFlowerPotBlock;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerPotBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class CustomPottedFlowerTypeCondition extends ConditionConfiguration<BlockInWorld> {
    public static final MapCodec<CustomPottedFlowerTypeCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("location").forGetter(CustomPottedFlowerTypeCondition::getLocation)
    ).apply(builder, CustomPottedFlowerTypeCondition::new));

    private final ResourceLocation location;

    public CustomPottedFlowerTypeCondition(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public boolean test(BlockInWorld block) {
        return block.getState().getBlock() instanceof CustomFlowerPotBlock && block.getEntity() instanceof CustomFlowerPotBlockEntity be && BovineRegistryUtil.getFlowerTypeKey(be.getFlowerType()).equals(location);
    }

    public ResourceLocation getLocation() {
        return location;
    }
}
