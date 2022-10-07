package net.merchantpug.bovinesandbuttercups.data.block;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record FlowerType(
        Optional<String> name,
        ResourceLocation modelLocation,
        String modelVariant,
        ResourceLocation itemModelLocation,
        String itemModelVariant,
        ResourceLocation pottedModelLocation,
        String pottedModelVariant,
        Optional<MobEffectInstance> stewEffectInstance) {

    public static final MapCodec<FlowerType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.optionalFieldOf("name").orElseGet(Optional::empty).forGetter(FlowerType::name),
            ResourceLocation.CODEC.fieldOf("model_location").forGetter(FlowerType::modelLocation),
            Codec.STRING.optionalFieldOf("model_variant", "bovines").forGetter(FlowerType::modelVariant),
            ResourceLocation.CODEC.optionalFieldOf("item_model_location").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.itemModelLocation)),
            Codec.STRING.optionalFieldOf("item_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.itemModelVariant)),
            ResourceLocation.CODEC.optionalFieldOf("potted_model_location").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.pottedModelLocation)),
            Codec.STRING.optionalFieldOf("potted_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.pottedModelVariant)),
            MobEffectUtil.CODEC.optionalFieldOf("stew_effect").orElseGet(Optional::empty).forGetter(FlowerType::stewEffectInstance)
    ).apply(builder, (t1, t2, t3, t4, t5, t6, t7, t8) -> new FlowerType(t1, t2, t3, t4.orElse(new ResourceLocation(t2.getNamespace(), t2.getPath() + "_item")), t5.orElse(t3), t6.orElse(new ResourceLocation(t2.getNamespace(), "potted_" + t2.getPath())), t7.orElse(t3), t8)));

    public static final FlowerType MISSING = new FlowerType(Optional.of("block.bovinesandbuttercups.custom_flower"), BovinesAndButtercups.asResource("missing_flower"), "bovines", BovinesAndButtercups.asResource("missing_flower_item"), "bovines", BovinesAndButtercups.asResource("potted_missing_flower"), "bovines", Optional.of(new MobEffectInstance(MobEffects.REGENERATION, 4)));

    public MutableComponent getOrCreateNameTranslationKey(LevelAccessor level) {
        return name.map(Component::translatable).orElse(Component.translatable("block." + BovineRegistryUtil.getFlowerTypeKey(level, this).getNamespace() + "." + BovineRegistryUtil.getFlowerTypeKey(level, this).getPath()));
    }

    public static Holder<FlowerType> bootstrap(Registry<FlowerType> registry) {
        return BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.FLOWER_TYPE_KEY, BovinesAndButtercups.asResource("missing")), FlowerType.MISSING);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FlowerType other))
            return false;

        return other.name.equals(this.name) && other.modelLocation.equals(this.modelLocation) && other.modelVariant.equals(this.modelVariant) && other.itemModelLocation.equals(this.itemModelLocation) && other.itemModelVariant.equals(this.itemModelVariant) && other.pottedModelLocation.equals(this.pottedModelLocation) && other.pottedModelVariant.equals(this.pottedModelVariant) && other.stewEffectInstance.equals(this.stewEffectInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.modelLocation, this.modelVariant, this.itemModelLocation, this.itemModelVariant, this.pottedModelLocation, this.pottedModelVariant, this.stewEffectInstance);
    }
}