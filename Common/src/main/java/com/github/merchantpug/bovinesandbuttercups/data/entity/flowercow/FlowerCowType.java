package com.github.merchantpug.bovinesandbuttercups.data.entity.flowercow;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.util.JsonParsingUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FlowerCowType {
    final ResourceLocation resourceLocation;
    final int loadingPriority;
    final FlowerType flower;
    FlowerType bud;
    @Nullable final MobEffectInstance nectarEffectInstance;
    @Nullable final ResourceKey<Biome> biomeKey;
    @Nullable final TagKey<Biome> biomeTagKey;
    final int naturalSpawnWeight;
    final List<FlowerCowBreedingRequirements> breedingRequirementsList = new ArrayList<>();

    public static final FlowerCowType MISSING = new FlowerCowType(Constants.resourceLocation("missing"), Integer.MAX_VALUE, FlowerType.MISSING, FlowerType.MISSING, null, null, null, 0);

    FlowerCowType(ResourceLocation resourceLocation, int loadingPriority, FlowerType flower, FlowerType bud, @Nullable MobEffectInstance nectarEffectInstance, @Nullable ResourceKey<Biome> biomeKey, @Nullable TagKey<Biome> biomeTagKey, int naturalSpawnWeight) {
        if (nectarEffectInstance == null && flower == null && !resourceLocation.equals(Constants.resourceLocation("missing"))) {
            Constants.LOG.warn("Moobloom Type '" + resourceLocation + "' does not have a flower or a mob effect, its nectar will have no associated effect");
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

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
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

    public static FlowerCowType fromName(String name) {
        try {
            ResourceLocation id = ResourceLocation.tryParse(name);
            return FlowerCowTypeRegistry.get(id);
        } catch (Exception exception) {
            Constants.LOG.warn("Could not get Moobloom type from name '" + name + "'.");
        }
        return MISSING;
    }

    public static void write(FlowerCowType type, FriendlyByteBuf buf) {
        buf.writeResourceLocation(type.getResourceLocation());
        buf.writeInt(type.getLoadingPriority());

        FlowerType.write(type.getFlower(), buf);
        FlowerType.write(type.getBud(), buf);

        buf.writeBoolean(type.getNectarEffectInstance() != null);
        if (type.getNectarEffectInstance() != null) {
            buf.writeResourceLocation(Objects.requireNonNull(Registry.MOB_EFFECT.getKey(type.getNectarEffectInstance().getEffect())));
            buf.writeInt(type.getNectarEffectInstance().getDuration());
        }

        buf.writeBoolean(type.getBiomeKey() != null);
        if (type.getBiomeKey() != null) {
            buf.writeResourceLocation(type.getBiomeKey().location());
        }
        buf.writeBoolean(type.getBiomeTagKey() != null);
        if (type.getBiomeTagKey() != null) {
            buf.writeResourceLocation(type.getBiomeTagKey().location());
        }

        buf.writeInt(type.getNaturalSpawnWeight());

        buf.writeInt(type.getBreedingRequirementsList().size());
        type.getBreedingRequirementsList().forEach(flowerCowBreedingRequirements -> FlowerCowBreedingRequirements.write(flowerCowBreedingRequirements, buf));
    }

    public static FlowerCowType read(FriendlyByteBuf buf) {
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

        FlowerCowType type = new FlowerCowType(
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

    public static FlowerCowType fromJson(ResourceLocation resourceLocation, JsonObject json) {
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

        FlowerTypeRegistry.register(flowerType);
        FlowerTypeRegistry.register(budFlowerType);

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

        FlowerCowType type = new FlowerCowType(
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

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FlowerCowType otherType))
            return false;

        return otherType.resourceLocation.equals(this.resourceLocation)
                && (otherType.flower == null && this.flower == null || otherType.flower != null && this.flower != null && otherType.flower.equals(this.flower))
                && (otherType.bud == null && this.bud == null || otherType.bud != null && this.bud != null && otherType.bud.equals(this.bud))
                && (otherType.nectarEffectInstance == null && this.nectarEffectInstance == null || otherType.nectarEffectInstance != null && this.nectarEffectInstance != null && otherType.nectarEffectInstance.equals(this.nectarEffectInstance))
                && (otherType.biomeKey == null && this.biomeKey == null || otherType.biomeKey != null && this.biomeKey != null && otherType.biomeKey.equals(this.biomeKey))
                && (otherType.biomeTagKey == null && this.biomeTagKey == null || otherType.biomeTagKey != null && this.biomeTagKey != null && otherType.biomeTagKey.equals(this.biomeTagKey))
                && otherType.naturalSpawnWeight == this.naturalSpawnWeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.resourceLocation, this.flower, this.bud, this.nectarEffectInstance, this.biomeKey, this.biomeTagKey, this.naturalSpawnWeight);
    }
}