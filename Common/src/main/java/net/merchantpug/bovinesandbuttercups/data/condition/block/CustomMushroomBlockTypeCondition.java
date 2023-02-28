package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.content.block.CustomHugeMushroomBlock;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomHugeMushroomBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class CustomMushroomBlockTypeCondition extends ConditionConfiguration<BlockInWorld> {
    public static MapCodec<CustomMushroomBlockTypeCondition> getCodec(RegistryAccess registryAccess) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                ResourceLocation.CODEC.fieldOf("location").forGetter(CustomMushroomBlockTypeCondition::getLocation)
        ).apply(builder, CustomMushroomBlockTypeCondition::new));
    }


    private final ResourceLocation location;

    public CustomMushroomBlockTypeCondition(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public boolean test(BlockInWorld block) {
        return block.getState().getBlock() instanceof CustomHugeMushroomBlock && block.getEntity() instanceof CustomHugeMushroomBlockEntity be && BovineRegistryUtil.getMushroomTypeKey(be.getMushroomType()).equals(location);
    }

    public ResourceLocation getLocation() {
        return location;
    }
}
