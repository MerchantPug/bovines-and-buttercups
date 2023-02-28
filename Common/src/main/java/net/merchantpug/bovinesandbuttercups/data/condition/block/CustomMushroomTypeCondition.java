package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.content.block.CustomMushroomBlock;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class CustomMushroomTypeCondition extends ConditionConfiguration<BlockInWorld> {
    public static MapCodec<CustomMushroomTypeCondition> getCodec(RegistryAccess registryAccess) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                ResourceLocation.CODEC.fieldOf("location").forGetter(CustomMushroomTypeCondition::getLocation)
        ).apply(builder, CustomMushroomTypeCondition::new));
    }

    private final ResourceLocation location;

    public CustomMushroomTypeCondition(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public boolean test(BlockInWorld block) {
        return block.getState().getBlock() instanceof CustomMushroomBlock && block.getEntity() instanceof CustomMushroomBlockEntity be && BovineRegistryUtil.getMushroomTypeKey(be.getMushroomType()).equals(location);
    }

    public ResourceLocation getLocation() {
        return location;
    }
}
