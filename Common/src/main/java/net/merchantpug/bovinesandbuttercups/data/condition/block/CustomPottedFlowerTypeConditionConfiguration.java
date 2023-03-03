package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.content.block.CustomFlowerPotBlock;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerPotBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class CustomPottedFlowerTypeConditionConfiguration extends ConditionConfiguration<BlockInWorld> {
    public static final MapCodec<CustomPottedFlowerTypeConditionConfiguration> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("location").forGetter(CustomPottedFlowerTypeConditionConfiguration::getLocation)
    ).apply(builder, CustomPottedFlowerTypeConditionConfiguration::new));

    private final ResourceLocation location;

    public CustomPottedFlowerTypeConditionConfiguration(ResourceLocation location) {
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
