package com.github.merchantpug.bovinesandbuttercups.data.entity.mushroom;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ICowType;
import com.github.merchantpug.bovinesandbuttercups.api.ICowTypeInstance;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomType;
import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import com.github.merchantpug.bovinesandbuttercups.util.JsonParsingUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;

public class MushroomCowType implements ICowType<MushroomCowType.Instance> {

    public static final Instance MISSING = new Instance(Constants.resourceLocation("missing_mooshroom"), Integer.MAX_VALUE, MushroomType.MISSING, false, null, null, 0);

    @Override
    public ResourceLocation getId() {
        return Constants.resourceLocation("mooshroom");
    }

    @Override
    public ICowTypeInstance getMissingCow() {
        return MISSING;
    }

    public void write(Instance instance, FriendlyByteBuf buf) {
        buf.writeResourceLocation(instance.getId());
        buf.writeInt(instance.getLoadingPriority());

        MushroomType.write(instance.getMushroom(), buf);

        buf.writeBoolean(instance.canMakeSuspiciousStew());

        buf.writeBoolean(instance.getBiomeKey() != null);
        if (instance.getBiomeKey() != null) {
            buf.writeResourceLocation(instance.getBiomeKey().location());
        }
        buf.writeBoolean(instance.getBiomeTagKey() != null);
        if (instance.getBiomeTagKey() != null) {
            buf.writeResourceLocation(instance.getBiomeTagKey().location());
        }

        buf.writeInt(instance.getNaturalSpawnWeight());
    }

    public Instance read(FriendlyByteBuf buf) {
        ResourceLocation resourceLocation = buf.readResourceLocation();

        int loadingPriority = buf.readInt();

        MushroomType mushroomType = MushroomType.read(buf);

        boolean canMakeSuspiciousStew = buf.readBoolean();

        boolean hasSpawnBiome = buf.readBoolean();
        ResourceKey<Biome> spawnBiome = null;
        if (hasSpawnBiome) {
            ResourceLocation biomeResourceLocation = buf.readResourceLocation();
            spawnBiome = ResourceKey.create(Registry.BIOME_REGISTRY, biomeResourceLocation);
        }

        boolean hasSpawnBiomeTag = buf.readBoolean();
        TagKey<Biome> spawnBiomeTag = null;
        if (hasSpawnBiomeTag) {
            ResourceLocation biomeTagResourceLocation = buf.readResourceLocation();
            spawnBiomeTag = TagKey.create(Registry.BIOME_REGISTRY, biomeTagResourceLocation);
        }

        int naturalSpawnWeight = buf.readInt();

        MushroomTypeRegistry.update(mushroomType.getResourceLocation(), mushroomType);

        return new Instance(
                resourceLocation,
                loadingPriority,
                mushroomType,
                canMakeSuspiciousStew,
                spawnBiome,
                spawnBiomeTag,
                naturalSpawnWeight);
    }

    public Instance fromJson(ResourceLocation resourceLocation, JsonObject json) {
        int loadingPriority = 0;
        if (json.has("loading_priority")) {
            loadingPriority = json.getAsJsonPrimitive("loading_priority").getAsInt();
        }

        if (!json.has("mushroom")) {
            throw new NullPointerException("Mooshroom JSON requires field: 'mushroom'.");
        }

        MushroomType mushroomType = MushroomType.fromJson(resourceLocation, json.getAsJsonObject("mushroom"));

        MushroomTypeRegistry.update(mushroomType.getResourceLocation(), mushroomType);

        boolean canMakeSuspiciousStew = false;
        if (json.has("can_make_suspicious_stew")) {
            canMakeSuspiciousStew = json.getAsJsonPrimitive("can_make_suspicious_stew").getAsBoolean();
        }

        ResourceKey<Biome> spawnBiome = null;
        if (json.has("spawn_biome")) {
            spawnBiome = ResourceKey.create(Registry.BIOME_REGISTRY, JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("spawn_biome").getAsString()));
        }

        TagKey<Biome> spawnBiomeTag = null;
        if (json.has("spawn_biome_tag")) {
            spawnBiomeTag = TagKey.create(Registry.BIOME_REGISTRY, JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("spawn_biome_tag").getAsString()));
        }

        int naturalSpawnWeight = 0;
        if (json.has("natural_spawn_weight")) {
            naturalSpawnWeight = json.getAsJsonPrimitive("natural_spawn_weight").getAsInt();
        }

        return new Instance(
                resourceLocation,
                loadingPriority,
                mushroomType,
                canMakeSuspiciousStew,
                spawnBiome,
                spawnBiomeTag,
                naturalSpawnWeight);
    }

    public static class Instance implements ICowTypeInstance {
        final ResourceLocation resourceLocation;
        final int loadingPriority;
        final MushroomType mushroom;
        final boolean canMakeSuspiciousStew;
        @Nullable final ResourceKey<Biome> biomeKey;
        @Nullable final TagKey<Biome> biomeTagKey;
        final int naturalSpawnWeight;

        Instance(ResourceLocation resourceLocation, int loadingPriority, MushroomType mushroom, boolean canMakeSuspiciousStew, @Nullable ResourceKey<Biome> biomeKey, @Nullable TagKey<Biome> biomeTagKey, int naturalSpawnWeight) {
            this.resourceLocation = resourceLocation;
            this.loadingPriority = loadingPriority;
            this.mushroom = mushroom;
            this.canMakeSuspiciousStew = canMakeSuspiciousStew;
            this.biomeKey = biomeKey;
            this.biomeTagKey = biomeTagKey;
            this.naturalSpawnWeight = naturalSpawnWeight;
        }

        @Override
        public ResourceLocation getId() {
            return resourceLocation;
        }

        public MushroomType getMushroom() {
            return this.mushroom;
        }

        public boolean canMakeSuspiciousStew() {
            return this.canMakeSuspiciousStew;
        }

        @Nullable public ResourceKey<Biome> getBiomeKey() {
            return this.biomeKey;
        }

        @Nullable public TagKey<Biome> getBiomeTagKey() {
            return this.biomeTagKey;
        }

        public int getNaturalSpawnWeight() {
            return this.naturalSpawnWeight;
        }

        @Override
        public int getLoadingPriority() {
            return this.loadingPriority;
        }

        @Override
        public ICowType getType() {
            return BovineCowTypes.MUSHROOM_COW_TYPE;
        }
    }
}