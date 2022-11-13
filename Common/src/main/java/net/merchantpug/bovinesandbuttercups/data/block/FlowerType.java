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
        Optional<String> name,
        ResourceLocation flowerModel,
        ResourceLocation pottedModel,
        Optional<MobEffectInstance> stewEffectInstance,
        Optional<ItemStack> dyeCraftResult) {

    public static final MapCodec<FlowerType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.optionalFieldOf("name").orElseGet(Optional::empty).forGetter(FlowerType::name),
            ResourceLocation.CODEC.fieldOf("flower_model").forGetter(FlowerType::flowerModel),
            ResourceLocation.CODEC.optionalFieldOf("potted_model").forGetter(x -> Optional.of(x.pottedModel)),
            MobEffectUtil.CODEC.optionalFieldOf("stew_effect").orElseGet(Optional::empty).forGetter(FlowerType::stewEffectInstance),
            ItemStack.CODEC.optionalFieldOf("dye_craft_result").orElseGet(Optional::empty).forGetter(FlowerType::dyeCraftResult)
    ).apply(builder, (t1, t2, t3, t4, t5) -> {
        String basePath = t2.getPath();
        String pottedPath = "";
        if (t3.isEmpty()) {
            if (basePath.contains("/")) {
                String[] splitBasePath = basePath.split("/");
                String endPath = splitBasePath[splitBasePath.length - 1];
                pottedPath = t2.getPath().substring(0, t2.getPath().length() - endPath.length()) + "potted_" + endPath;
            } else {
                pottedPath = "potted_" + basePath;
            }
        }
        return new FlowerType(t1, t2, t3.orElse(new ResourceLocation(t2.getNamespace(), pottedPath)), t4, t5);
    }));

    public static final FlowerType MISSING = new FlowerType(Optional.of("block.bovinesandbuttercups.custom_flower"), BovinesAndButtercups.asResource("missing_flower"), BovinesAndButtercups.asResource("potted_missing_flower"), Optional.of(new MobEffectInstance(MobEffects.REGENERATION, 4)), Optional.empty());

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

        return other.name.equals(this.name) && other.flowerModel.equals(this.flowerModel) && other.pottedModel.equals(this.pottedModel) && other.stewEffectInstance.equals(this.stewEffectInstance) && other.dyeCraftResult.equals(this.dyeCraftResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.flowerModel, this.pottedModel, this.stewEffectInstance, this.dyeCraftResult);
    }
}