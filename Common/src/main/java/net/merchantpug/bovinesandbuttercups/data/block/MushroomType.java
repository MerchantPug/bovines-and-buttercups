package net.merchantpug.bovinesandbuttercups.data.block;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record MushroomType(Optional<String> name,
                           ResourceLocation modelLocation,
                           String modelVariant,
                           ResourceLocation itemModelLocation,
                           String itemModelVariant,
                           ResourceLocation pottedModelLocation,
                           String pottedModelVariant,
                           Optional<String> hugeBlockName,
                           ResourceLocation hugeBlockModelLocation,
                           String hugeBlockModelVariant,
                           ResourceLocation hugeBlockItemModelLocation,
                           String hugeBlockItemModelVariant,
                           Optional<List<ResourceLocation>> hugeMushroomStructureList) {

    public static final MapCodec<MushroomType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.optionalFieldOf("name").orElseGet(Optional::empty).forGetter(MushroomType::name),
            ResourceLocation.CODEC.fieldOf("model_location").forGetter(MushroomType::modelLocation),
            Codec.STRING.optionalFieldOf("model_variant", "bovines").forGetter(MushroomType::modelVariant),
            ResourceLocation.CODEC.optionalFieldOf("item_model_location").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.itemModelLocation)),
            Codec.STRING.optionalFieldOf("item_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.itemModelVariant)),
            ResourceLocation.CODEC.optionalFieldOf("potted_model_location").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.pottedModelLocation)),
            Codec.STRING.optionalFieldOf("potted_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.pottedModelVariant)),
            Codec.STRING.optionalFieldOf("huge_name").orElseGet(Optional::empty).forGetter(MushroomType::hugeBlockName),
            ResourceLocation.CODEC.optionalFieldOf("huge_model_location").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.hugeBlockModelLocation)),
            Codec.STRING.optionalFieldOf("huge_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.hugeBlockModelVariant)),
            ResourceLocation.CODEC.optionalFieldOf("huge_item_model_location").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.hugeBlockItemModelLocation)),
            Codec.STRING.optionalFieldOf("huge_item_model_variant").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.hugeBlockItemModelVariant)),
            Codec.list(ResourceLocation.CODEC).optionalFieldOf("huge_structures").orElseGet(Optional::empty).forGetter(MushroomType::hugeMushroomStructureList)
    ).apply(builder, (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> new MushroomType(t1, t2, t3, t4.orElse(new ResourceLocation(t2.getNamespace(), t2.getPath() + "_item")), t5.orElse(t3), t6.orElse(new ResourceLocation(t2.getNamespace(), "potted_" + t2.getPath())), t7.orElse(t3), t8, t9.orElse(new ResourceLocation(t2.getNamespace(), t2.getPath() + "_block")), t10.orElse(t3), t11.orElse(new ResourceLocation(t2.getNamespace(), t2.getPath() + "_block_item")), t12.orElse(t3), t13)));

    public static final MushroomType MISSING = new MushroomType(Optional.of("block.bovinesandbuttercups.custom_mushroom"), BovinesAndButtercups.asResource("missing_mushroom"), "bovines", BovinesAndButtercups.asResource("missing_mushroom_item"), "bovines", BovinesAndButtercups.asResource("potted_missing_mushroom"), "bovines", Optional.of("block.bovinesandbuttercups.custom_mushroom_block"), BovinesAndButtercups.asResource("missing_mushroom_block"), "bovines", BovinesAndButtercups.asResource("missing_mushroom_block_item"), "bovines", Optional.empty());

    public MutableComponent getOrCreateNameTranslationKey(LevelAccessor level) {
        return name.map(Component::translatable).orElse(Component.translatable("block." + BovineRegistryUtil.getMushroomTypeKey(level, this).getNamespace() + "." + BovineRegistryUtil.getMushroomTypeKey(level, this).getPath()));
    }

    public MutableComponent getOrCreateHugeNameTranslationKey(LevelAccessor level) {
        return hugeBlockName.map(Component::translatable).orElse(Component.translatable("block." + BovineRegistryUtil.getMushroomTypeKey(level, this).getNamespace() + "." + BovineRegistryUtil.getMushroomTypeKey(level, this).getPath() + "_block"));
    }

    public static Holder<MushroomType> bootstrap(Registry<MushroomType> registry) {
        return BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.MUSHROOM_TYPE_KEY, BovinesAndButtercups.asResource("missing")), MushroomType.MISSING);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof MushroomType other))
            return false;

        return other.name.equals(this.name) && other.modelLocation.equals(this.modelLocation) && other.modelVariant.equals(this.modelVariant) && other.itemModelLocation.equals(this.itemModelLocation) && other.itemModelVariant.equals(this.itemModelVariant) && other.pottedModelLocation.equals(this.pottedModelLocation) && other.pottedModelVariant.equals(this.pottedModelVariant) && other.hugeBlockName.equals(this.hugeBlockName) && other.hugeBlockModelLocation.equals(this.hugeBlockModelLocation) && other.hugeBlockModelVariant.equals(this.hugeBlockModelVariant) && other.hugeBlockItemModelLocation.equals(this.hugeBlockItemModelLocation) && other.hugeBlockItemModelVariant.equals(this.hugeBlockItemModelVariant) && other.hugeMushroomStructureList.equals(this.hugeMushroomStructureList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.modelLocation, this.modelVariant, this.itemModelLocation, this.itemModelVariant, this.pottedModelLocation, this.pottedModelVariant, this.hugeBlockName, this.hugeBlockModelLocation, this.hugeBlockModelVariant, this.hugeBlockItemModelLocation, this.hugeBlockItemModelVariant, this.hugeMushroomStructureList);
    }
}