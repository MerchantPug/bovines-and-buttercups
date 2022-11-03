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
                           ModelInformation mushroomModel,
                           ModelInformation itemModel,
                           ModelInformation pottedModel,
                           Optional<String> hugeBlockName,
                           ModelInformation hugeModel,
                           ModelInformation hugeItemModel,
                           Optional<List<ResourceLocation>> hugeMushroomStructureList) {

    public static final MapCodec<MushroomType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.optionalFieldOf("name").orElseGet(Optional::empty).forGetter(MushroomType::name),
            ModelInformation.CODEC.fieldOf("mushroom_model").forGetter(MushroomType::mushroomModel),
            ModelInformation.CODEC.optionalFieldOf("item_model").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.itemModel)),
            ModelInformation.CODEC.optionalFieldOf("potted_model").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.pottedModel)),
            Codec.STRING.optionalFieldOf("huge_name").orElseGet(Optional::empty).forGetter(MushroomType::hugeBlockName),
            ModelInformation.CODEC.optionalFieldOf("huge_model").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.hugeModel)),
            ModelInformation.CODEC.optionalFieldOf("huge_item_model").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.hugeItemModel)),
            Codec.list(ResourceLocation.CODEC).optionalFieldOf("huge_structures").orElseGet(Optional::empty).forGetter(MushroomType::hugeMushroomStructureList)
    ).apply(builder, (t1, t2, t3, t4, t5, t6, t7, t8) -> {
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
        return new MushroomType(t1, t2, t3.orElse(new ModelInformation(new ResourceLocation(t2.location().getNamespace(), t2.location().getPath() + "_item"), t2.variant())), t4.orElse(new ModelInformation(new ResourceLocation(t2.location().getNamespace(), pottedPath), t2.variant())), t5, t6.orElse(new ModelInformation(new ResourceLocation(t2.location().getNamespace(), t2.location().getPath() + "_block"), t2.variant())), t7.orElse(t6.map(model -> new ModelInformation(new ResourceLocation(model.location().getNamespace(), model.location().getPath() + "_item"), model.variant())).orElseGet(() -> new ModelInformation(new ResourceLocation(t2.location().getNamespace(), t2.location().getPath() + "_block_item"), t2.variant()))), t8);
    }));

    public static final MushroomType MISSING = new MushroomType(Optional.of("block.bovinesandbuttercups.custom_mushroom"), new ModelInformation(BovinesAndButtercups.asResource("missing_mushroom"), "bovines"), new ModelInformation(BovinesAndButtercups.asResource("missing_mushroom_item"), "bovines"), new ModelInformation(BovinesAndButtercups.asResource("potted_missing_mushroom"), "bovines"), Optional.of("block.bovinesandbuttercups.custom_mushroom_block"), new ModelInformation(BovinesAndButtercups.asResource("missing_mushroom_block"), "bovines"), new ModelInformation(BovinesAndButtercups.asResource("missing_mushroom_block_item"), "bovines"), Optional.empty());

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

        return other.name.equals(this.name) && other.mushroomModel.equals(this.mushroomModel) && other.itemModel.equals(this.itemModel) && other.pottedModel.equals(this.pottedModel) && other.hugeBlockName.equals(this.hugeBlockName) && other.hugeModel.equals(this.hugeModel) && other.hugeItemModel.equals(this.hugeItemModel) && other.hugeMushroomStructureList.equals(this.hugeMushroomStructureList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.mushroomModel, this.itemModel, this.pottedModel, this.hugeBlockName, this.hugeModel, this.hugeItemModel, this.hugeMushroomStructureList);
    }
}