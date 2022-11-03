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
        ModelInformation flowerModel,
        ModelInformation itemModel,
        ModelInformation pottedModel,
        Optional<MobEffectInstance> stewEffectInstance,
        Optional<ItemStack> dyeCraftResult) {

    public static final MapCodec<FlowerType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.optionalFieldOf("name").orElseGet(Optional::empty).forGetter(FlowerType::name),
            ModelInformation.CODEC.fieldOf("flower_model").forGetter(FlowerType::flowerModel),
            ModelInformation.CODEC.optionalFieldOf("item_model").forGetter(x -> Optional.of(x.itemModel)),
            ModelInformation.CODEC.optionalFieldOf("potted_model").forGetter(x -> Optional.of(x.pottedModel)),
            MobEffectUtil.CODEC.optionalFieldOf("stew_effect").orElseGet(Optional::empty).forGetter(FlowerType::stewEffectInstance),
            ItemStack.CODEC.optionalFieldOf("dye_craft_result").orElseGet(Optional::empty).forGetter(FlowerType::dyeCraftResult)
    ).apply(builder, (t1, t2, t3, t4, t5, t6) -> {
        String basePath = t2.location().getPath();
        String pottedPath = "";
        if (t4.isEmpty()) {
            if (basePath.contains("/")) {
                String[] splitBasePath = basePath.split("/");
                String endPath = splitBasePath[splitBasePath.length - 1];
                pottedPath = t2.location().getPath().substring(0, t2.location().getPath().length() - endPath.length()) + "potted_" + endPath;
            } else {
                pottedPath = "potted_" + basePath;
            }
        }
        return new FlowerType(t1, t2, t3.orElse(new ModelInformation(new ResourceLocation(t2.location().getNamespace(), t2.location().getPath() + "_item"), t2.variant())), t4.orElse(new ModelInformation(new ResourceLocation(t2.location().getNamespace(), pottedPath), t2.variant())), t5, t6);
    }));

    public static final FlowerType MISSING = new FlowerType(Optional.of("block.bovinesandbuttercups.custom_flower"), new ModelInformation(BovinesAndButtercups.asResource("missing_flower"), "bovines"), new ModelInformation(BovinesAndButtercups.asResource("missing_flower_item"), "bovines"), new ModelInformation(BovinesAndButtercups.asResource("potted_missing_flower"), "bovines"), Optional.of(new MobEffectInstance(MobEffects.REGENERATION, 4)), Optional.empty());

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

        return other.name.equals(this.name) && other.flowerModel.equals(this.flowerModel) && other.itemModel.equals(this.itemModel) && other.pottedModel.equals(this.pottedModel) && other.stewEffectInstance.equals(this.stewEffectInstance) && other.dyeCraftResult == this.dyeCraftResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.flowerModel, this.itemModel, this.pottedModel, this.stewEffectInstance, this.dyeCraftResult);
    }
}