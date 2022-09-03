package com.github.merchantpug.bovinesandbuttercups.data.block;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import com.github.merchantpug.bovinesandbuttercups.util.ConfiguredCowTypeRegistryUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public record MushroomType(
        Optional<ResourceLocation> key,
        Optional<String> name,
        Optional<BlockState> blockState,
        Optional<ResourceLocation> modelLocation,
        String modelVariant,
        Optional<ResourceLocation> itemModelLocation,
        String itemModelVariant,
        Optional<ResourceLocation> pottedModelLocation,
        String pottedModelVariant,
        Optional<String> hugeBlockName,
        Optional<ResourceLocation> hugeBlockModelLocation,
        String hugeBlockModelVariant,
        Optional<ResourceLocation> hugeBlockItemModelLocation,
        String hugeBlockItemModelVariant,
        Optional<List<ResourceLocation>> hugeMushroomStructureList,
        boolean withMushroomBlocks) {

    public static final MapCodec<MushroomType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.optionalFieldOf("key").orElseGet(Optional::empty).forGetter(MushroomType::key),
            Codec.STRING.optionalFieldOf("name").orElseGet(Optional::empty).forGetter(MushroomType::name),
            BlockState.CODEC.optionalFieldOf("block_state").orElseGet(Optional::empty).forGetter(MushroomType::blockState),
            ResourceLocation.CODEC.optionalFieldOf("model_location").orElseGet(Optional::empty).forGetter(MushroomType::modelLocation),
            Codec.STRING.optionalFieldOf("model_variant", "bovines").forGetter(MushroomType::modelVariant),
            ResourceLocation.CODEC.optionalFieldOf("item_model_location").forGetter(MushroomType::itemModelLocation),
            Codec.STRING.optionalFieldOf("item_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.itemModelVariant)),
            ResourceLocation.CODEC.optionalFieldOf("potted_model_location").orElseGet(Optional::empty).forGetter(MushroomType::pottedModelLocation),
            Codec.STRING.optionalFieldOf("potted_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.pottedModelVariant)),
            Codec.STRING.optionalFieldOf("huge_name").orElseGet(Optional::empty).forGetter(MushroomType::hugeBlockName),
            ResourceLocation.CODEC.optionalFieldOf("huge_model_location").orElseGet(Optional::empty).forGetter(MushroomType::hugeBlockModelLocation),
            Codec.STRING.optionalFieldOf("huge_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.hugeBlockModelVariant)),
            ResourceLocation.CODEC.optionalFieldOf("huge_item_model_location").orElseGet(Optional::empty).forGetter(MushroomType::hugeBlockItemModelLocation),
            Codec.STRING.optionalFieldOf("huge_item_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.hugeBlockItemModelVariant)),
            Codec.list(ResourceLocation.CODEC).optionalFieldOf("huge_structures").orElseGet(Optional::empty).forGetter(MushroomType::hugeMushroomStructureList),
            Codec.BOOL.optionalFieldOf("with_mushroom_blocks", false).forGetter(MushroomType::withMushroomBlocks)
    ).apply(builder, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> new MushroomType(t1.isEmpty() && t3.isPresent() ? Optional.of(Registry.BLOCK.getKey(t3.get().getBlock())) : t1, t2, t3, t4, t5, t4.map(resourceLocation -> t6.orElse(new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + "_item"))), t7.orElse(t5), t4.map(resourceLocation -> t8.orElse(new ResourceLocation(resourceLocation.getNamespace(), "potted_" + resourceLocation.getPath()))), t9.orElse(t5), t10, t4.map(resourceLocation -> t11.orElse(new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + "_block"))), t12.orElse(t5), t4.map(resourceLocation -> t13.orElse(new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + "_block_item"))), t14.orElse(t5), t15, t16)));

    public static final MushroomType MISSING = new MushroomType(Optional.of(Constants.resourceLocation("missing_mushroom")), Optional.of("block.bovinesandbuttercups.custom_mushroom"), Optional.empty(), Optional.of(Constants.resourceLocation("missing_mushroom")), "bovines", Optional.of(Constants.resourceLocation("missing_mushroom_item")), "bovines", Optional.of(Constants.resourceLocation("potted_missing_mushroom")), "bovines", Optional.of("block.bovinesandbuttercups.custom_mushroom_block"), Optional.of(Constants.resourceLocation("missing_mushroom_block")), "bovines", Optional.of(Constants.resourceLocation("missing_mushroom_block_item")), "bovines", Optional.empty(), false);

    public static MushroomType fromKey(LevelAccessor level, ResourceLocation key) {
        try {
            List<MushroomType> mushroomTypes = ConfiguredCowTypeRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof MushroomCowConfiguration && ((MushroomCowConfiguration) configuredCowType.getConfiguration()).mushroom().key().isPresent() && ((MushroomCowConfiguration) configuredCowType.getConfiguration()).mushroom().key().get().equals(key)).map(configuredCowType -> ((MushroomCowConfiguration) configuredCowType.getConfiguration()).mushroom()).toList();
            if (!mushroomTypes.isEmpty()) {
                return mushroomTypes.get(0);
            }
        } catch (Exception exception) {
            Constants.LOG.warn("Could not get Flower type from name '" + key.toString() + "'.");
        }
        return MISSING;
    }

    public MutableComponent getOrCreateNameTranslationKey() {
        return key.map(resourceLocation -> name.map(Component::translatable).orElseGet(() -> Component.translatable("block." + resourceLocation.getNamespace() + "." + resourceLocation.getPath()))).orElseGet(() -> Component.translatable("block.bovinesandbuttercups.custom_mushroom"));
    }

    public MutableComponent getOrCreateHugeNameTranslationKey() {
        return key.map(resourceLocation -> hugeBlockName.map(Component::translatable).orElseGet(() -> Component.translatable("block." + resourceLocation.getNamespace() + "." + resourceLocation.getPath()))).orElseGet(() -> Component.translatable("block.bovinesandbuttercups.custom_mushroom_block"));
    }
}