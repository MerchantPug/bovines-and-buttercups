package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.content.block.CustomMushroomPotBlock;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class CustomPottedMushroomBlockTypeCondition extends ConditionConfiguration<BlockInWorld> {
    public static MapCodec<CustomPottedMushroomBlockTypeCondition> getCodec() {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                ResourceLocation.CODEC.fieldOf("location").forGetter(CustomPottedMushroomBlockTypeCondition::getLocation)
        ).apply(builder, CustomPottedMushroomBlockTypeCondition::new));
    }

    private final ResourceLocation location;

    public CustomPottedMushroomBlockTypeCondition(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public boolean test(BlockInWorld block) {
        return block.getState().getBlock() instanceof CustomMushroomPotBlock && block.getEntity() instanceof CustomMushroomPotBlockEntity be && BovineRegistryUtil.getMushroomTypeKey(be.getMushroomType()).equals(location);
    }

    public ResourceLocation getLocation() {
        return location;
    }
}
