package net.merchantpug.bovinesandbuttercups.data.block;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import java.util.Objects;
import java.util.Optional;

public record FlowerType(
        Optional<MobEffectInstance> stewEffectInstance,
        Optional<ItemStack> dyeCraftResult) {

    public static final MapCodec<FlowerType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            MobEffectUtil.CODEC.optionalFieldOf("stew_effect").orElseGet(Optional::empty).forGetter(FlowerType::stewEffectInstance),
            ItemStack.CODEC.optionalFieldOf("dye_craft_result").orElseGet(Optional::empty).forGetter(FlowerType::dyeCraftResult)
    ).apply(builder, FlowerType::new));

    public static final FlowerType MISSING = new FlowerType(Optional.empty(), Optional.empty());

    public static Holder<FlowerType> bootstrap(Registry<FlowerType> registry) {
        return BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.FLOWER_TYPE_KEY, BovinesAndButtercups.asResource("missing_flower")), FlowerType.MISSING);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FlowerType other))
            return false;

        return other.stewEffectInstance.equals(this.stewEffectInstance) && other.dyeCraftResult.equals(this.dyeCraftResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.stewEffectInstance, this.dyeCraftResult);
    }
}