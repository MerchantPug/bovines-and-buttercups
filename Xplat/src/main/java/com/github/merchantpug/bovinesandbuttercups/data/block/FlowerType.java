package com.github.merchantpug.bovinesandbuttercups.data.block;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import com.github.merchantpug.bovinesandbuttercups.util.ConfiguredCowTypeRegistryUtil;
import com.github.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public record FlowerType(
        Optional<ResourceLocation> key,
        Optional<String> name,
        Optional<BlockState> blockState,
        Optional<ResourceLocation> modelLocation,
        String modelVariant,
        Optional<ResourceLocation> itemModelLocation,
        String itemModelVariant,
        Optional<ResourceLocation> pottedModelLocation,
        String pottedModelVariant,
        Optional<MobEffectInstance> stewEffectInstance,
        boolean withFlowerBlock) {

    public static final MapCodec<FlowerType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.optionalFieldOf("key").orElseGet(Optional::empty).forGetter(FlowerType::key),
            Codec.STRING.optionalFieldOf("name").orElseGet(Optional::empty).forGetter(FlowerType::name),
            BlockState.CODEC.optionalFieldOf("block_state").orElseGet(Optional::empty).forGetter(FlowerType::blockState),
            ResourceLocation.CODEC.optionalFieldOf("model_location").orElseGet(Optional::empty).forGetter(FlowerType::modelLocation),
            Codec.STRING.optionalFieldOf("model_variant", "bovines").forGetter(FlowerType::modelVariant),
            ResourceLocation.CODEC.optionalFieldOf("item_model_location").forGetter(FlowerType::itemModelLocation),
            Codec.STRING.optionalFieldOf("item_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.itemModelVariant)),
            ResourceLocation.CODEC.optionalFieldOf("potted_model_location").orElseGet(Optional::empty).forGetter(FlowerType::pottedModelLocation),
            Codec.STRING.optionalFieldOf("potted_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.pottedModelVariant)),
            MobEffectUtil.CODEC.optionalFieldOf("stew_effect").orElseGet(Optional::empty).forGetter(FlowerType::stewEffectInstance),
            Codec.BOOL.optionalFieldOf("with_blocks", false).forGetter(FlowerType::withFlowerBlock)
    ).apply(builder, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> new FlowerType(t1.isEmpty() && t3.isPresent() ? Optional.of(Registry.BLOCK.getKey(t3.get().getBlock())) : t1, t2, t3, t4, t5, t4.map(resourceLocation -> t6.orElse(new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + "_item"))), t7.orElse(t5), t4.map(resourceLocation -> t8.orElse(new ResourceLocation(resourceLocation.getNamespace(), "potted_" + resourceLocation.getPath()))), t9.orElse(t5), t10, t11)));

    public static final FlowerType MISSING = new FlowerType(Optional.of(Constants.resourceLocation("missing_flower")), Optional.of("block.bovinesandbuttercups.custom_flower"), Optional.empty(), Optional.of(Constants.resourceLocation("missing_flower")), "bovines", Optional.of(Constants.resourceLocation("missing_flower_item")), "bovines", Optional.of(Constants.resourceLocation("potted_missing_flower")), "bovines", Optional.of(new MobEffectInstance(MobEffects.REGENERATION, 4)), false);

    public static FlowerType fromKey(LevelAccessor level, ResourceLocation key) {
        try {
            List<FlowerType> flowerTypes = ConfiguredCowTypeRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration && ((FlowerCowConfiguration) configuredCowType.getConfiguration()).flower().key().isPresent() && ((FlowerCowConfiguration) configuredCowType.getConfiguration()).flower().key().get().equals(key)).map(configuredCowType -> ((FlowerCowConfiguration) configuredCowType.getConfiguration()).flower()).toList();
            if (!flowerTypes.isEmpty()) {
                return flowerTypes.get(0);
            }
            List<FlowerType> budTypes = ConfiguredCowTypeRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration && ((FlowerCowConfiguration) configuredCowType.getConfiguration()).bud().key().isPresent() && ((FlowerCowConfiguration) configuredCowType.getConfiguration()).bud().key().get().equals(key)).map(configuredCowType -> ((FlowerCowConfiguration) configuredCowType.getConfiguration()).bud()).toList();
            if (!budTypes.isEmpty()) {
                return budTypes.get(0);
            }
        } catch (Exception exception) {
            Constants.LOG.warn("Could not get Flower type from name '" + key.toString() + "'.");
        }
        return MISSING;
    }

    public MutableComponent getOrCreateNameTranslationKey() {
        return key.map((resourceLocation -> name.map(Component::translatable).orElseGet(() -> Component.translatable("block." + key.get().getNamespace() + "." + key.get().getPath())))).orElseGet(() -> Component.translatable("block.bovinesandbuttercups.custom_flower"));
    }
}