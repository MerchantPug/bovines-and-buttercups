package com.github.merchantpug.bovinesandbuttercups.data.entity.flower;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ICowType;
import com.github.merchantpug.bovinesandbuttercups.api.ICowTypeInstance;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import com.github.merchantpug.bovinesandbuttercups.util.JsonParsingUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FlowerCowType implements ICowType<FlowerCowType.Instance> {

    public static final Instance MISSING = new Instance(Constants.resourceLocation("missing_moobloom"), Integer.MAX_VALUE, FlowerType.MISSING, FlowerType.MISSING, null, null, null, 0);

    @Override
    public ResourceLocation getId() {
        return Constants.resourceLocation("moobloom");
    }

    @Override
    public ICowTypeInstance getMissingCow() {
        return MISSING;
    }

    public void write(Instance instance, FriendlyByteBuf buf) {
        buf.writeResourceLocation(instance.getId());
        buf.writeInt(instance.getLoadingPriority());

        FlowerType.write(instance.getFlower(), buf);
        FlowerType.write(instance.getBud(), buf);

        buf.writeBoolean(instance.getNectarEffectInstance() != null);
        if (instance.getNectarEffectInstance() != null) {
            buf.writeResourceLocation(Objects.requireNonNull(Registry.MOB_EFFECT.getKey(instance.getNectarEffectInstance().getEffect())));
            buf.writeInt(instance.getNectarEffectInstance().getDuration());
        }

        buf.writeBoolean(instance.getBiomeKey() != null);
        if (instance.getBiomeKey() != null) {
            buf.writeResourceLocation(instance.getBiomeKey().location());
        }
        buf.writeBoolean(instance.getBiomeTagKey() != null);
        if (instance.getBiomeTagKey() != null) {
            buf.writeResourceLocation(instance.getBiomeTagKey().location());
        }

        buf.writeInt(instance.getNaturalSpawnWeight());

        buf.writeInt(instance.getBreedingRequirementsList().size());
        instance.getBreedingRequirementsList().forEach(flowerCowBreedingRequirements -> FlowerCowBreedingRequirements.write(flowerCowBreedingRequirements, buf));
    }

    public Instance read(FriendlyByteBuf buf) {
        ResourceLocation resourceLocation = buf.readResourceLocation();

        int loadingPriority = buf.readInt();

        FlowerType flowerType = FlowerType.read(buf);
        FlowerType budFlowerType = FlowerType.read(buf);

        boolean hasNectarEffectInstance = buf.readBoolean();
        MobEffectInstance nectarEffectInstance = null;
        if (hasNectarEffectInstance) {
            ResourceLocation nectarEffectLocation = buf.readResourceLocation();
            Optional<MobEffect> optionalEffect = Registry.MOB_EFFECT.getOptional(nectarEffectLocation);
            if (optionalEffect.isEmpty()) {
                Constants.LOG.warn("Could not find mob effect with id: " + nectarEffectLocation);
            }
            int duration = buf.readInt();
            if (optionalEffect.isPresent()) {
                nectarEffectInstance = new MobEffectInstance(optionalEffect.get(), duration);
            }
        }

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

        List<FlowerCowBreedingRequirements> breedingRequirementsList = new ArrayList<>();
        int breedingRequirementsListSize = buf.readInt();
        for (int i = 0; i < breedingRequirementsListSize; i++) {
            FlowerCowBreedingRequirements breedingRequirements = FlowerCowBreedingRequirements.read(buf);
            breedingRequirementsList.add(breedingRequirements);
        }

        Instance type = new Instance(
                resourceLocation,
                loadingPriority,
                flowerType,
                budFlowerType,
                nectarEffectInstance,
                spawnBiome,
                spawnBiomeTag,
                naturalSpawnWeight);

        type.addAllBreedingRequirements(breedingRequirementsList);


        FlowerTypeRegistry.update(flowerType.getResourceLocation(), flowerType);
        FlowerTypeRegistry.update(budFlowerType.getResourceLocation(), budFlowerType);

        return type;
    }

    public Instance fromJson(ResourceLocation resourceLocation, JsonObject json) {
        int loadingPriority = 0;
        if (json.has("loading_priority")) {
            loadingPriority = json.getAsJsonPrimitive("loading_priority").getAsInt();
        }

        if (!json.has("flower")) {
            throw new NullPointerException("Moobloom JSON requires field: 'flower'.");
        }

        if (!json.has("bud")) {
            throw new NullPointerException("Moobloom JSON requires field: 'bud'.");
        }

        FlowerType flowerType = FlowerType.fromJson(resourceLocation, json.getAsJsonObject("flower"));
        FlowerType budFlowerType = FlowerType.fromJson(new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + "_bud"), json.getAsJsonObject("bud"));

        FlowerTypeRegistry.update(flowerType.getResourceLocation(), flowerType);
        FlowerTypeRegistry.update(budFlowerType.getResourceLocation(), budFlowerType);

        MobEffectInstance nectarEffectInstance = null;
        if (json.has("nectar_effect")) {
            nectarEffectInstance = JsonParsingUtil.readNectarEffectInstance(json.getAsJsonObject("nectar_effect"));
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

        List<FlowerCowBreedingRequirements> breedingRequirementsList = new ArrayList<>();
        if (json.has("breeding_requirements")) {
            JsonElement breedingRequirements = json.get("breeding_requirements");
            if (breedingRequirements.isJsonArray()) {
                breedingRequirements.getAsJsonArray().forEach(jsonElement -> breedingRequirementsList.add(FlowerCowBreedingRequirements.fromJson(jsonElement.getAsJsonObject())));
            } else if (breedingRequirements.isJsonObject()) {
                breedingRequirementsList.add(FlowerCowBreedingRequirements.fromJson(breedingRequirements.getAsJsonObject()));
            } else {
                Constants.LOG.warn("Failed to parse 'breeding_requirements' field for Moobloom type '" + resourceLocation + "'. Expected either a JSON Object or JSON Array.");
            }
        }

        FlowerCowType.Instance type = new FlowerCowType.Instance(
                resourceLocation,
                loadingPriority,
                flowerType,
                budFlowerType,
                nectarEffectInstance,
                spawnBiome,
                spawnBiomeTag,
                naturalSpawnWeight);

        type.addAllBreedingRequirements(breedingRequirementsList);

        return type;
    }

    public static class Instance implements ICowTypeInstance {
        final ResourceLocation resourceLocation;
        final int loadingPriority;
        final FlowerType flower;
        FlowerType bud;
        @Nullable final MobEffectInstance nectarEffectInstance;
        @Nullable final ResourceKey<Biome> biomeKey;
        @Nullable final TagKey<Biome> biomeTagKey;
        final int naturalSpawnWeight;
        final List<FlowerCowBreedingRequirements> breedingRequirementsList = new ArrayList<>();

        Instance(ResourceLocation resourceLocation, int loadingPriority, FlowerType flower, FlowerType bud, @Nullable MobEffectInstance nectarEffectInstance, @Nullable ResourceKey<Biome> biomeKey, @Nullable TagKey<Biome> biomeTagKey, int naturalSpawnWeight) {
            if (nectarEffectInstance == null && flower.getBlockState() == null && flower.getStewEffectInstance() == null && !resourceLocation.equals(Constants.resourceLocation("missing"))) {
                Constants.LOG.warn("Moobloom Type Instance '" + resourceLocation + "' does not have a flower blockstate, flower stew effect instance or a nectar effect specified, its nectar will have no associated effect");
            }
            this.resourceLocation = resourceLocation;
            this.loadingPriority = loadingPriority;
            this.flower = flower;
            this.bud = bud;
            this.nectarEffectInstance = nectarEffectInstance;
            this.biomeKey = biomeKey;
            this.biomeTagKey = biomeTagKey;
            this.naturalSpawnWeight = naturalSpawnWeight;
        }

        @Override
        public ResourceLocation getId() {
            return resourceLocation;
        }

        @Nullable public MobEffectInstance getNectarEffectInstance() {
            return this.nectarEffectInstance;
        }

        public FlowerType getFlower() {
            return this.flower;
        }

        public FlowerType getBud() {
            return this.bud;
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

        public void addAllBreedingRequirements(List<FlowerCowBreedingRequirements> breedingRequirements) {
            breedingRequirementsList.addAll(breedingRequirements);
        }

        public void addBreedingRequirement(FlowerCowBreedingRequirements breedingRequirements) {
            breedingRequirementsList.add(breedingRequirements);
        }

        public List<FlowerCowBreedingRequirements> getBreedingRequirementsList() {
            return this.breedingRequirementsList;
        }

        @Override
        public ICowType getType() {
            return BovineCowTypes.FLOWER_COW_TYPE;
        }
    }
}