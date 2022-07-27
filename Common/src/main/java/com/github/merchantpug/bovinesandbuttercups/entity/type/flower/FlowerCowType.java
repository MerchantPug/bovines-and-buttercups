package com.github.merchantpug.bovinesandbuttercups.entity.type.flower;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import com.github.merchantpug.bovinesandbuttercups.Constants;
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
    @Nullable final BlockState flower;
    @Nullable ResourceLocation flowerModel;
    final String flowerModelVariant;
    @Nullable final BlockState bud;
    @Nullable final ResourceLocation budModel;
    final String budModelVariant;
    @Nullable final MobEffectInstance effectInstance;
    @Nullable final ResourceKey<Biome> biomeKey;
    @Nullable final TagKey<Biome> biomeTagKey;
    final int nectarDuration;
    final int naturalSpawnWeight;
    final List<FlowerCowBreedingRequirements> breedingRequirementsList = new ArrayList<>();

    public static final FlowerCowType MISSING = new FlowerCowType(BovinesAndButtercupsCommon.resourceLocation("missing"), Integer.MAX_VALUE, null, null, "bovines", null, null, "bovines", null, null, null, 0, 0);

    FlowerCowType(ResourceLocation resourceLocation, int loadingPriority, @Nullable BlockState flower, @Nullable ResourceLocation flowerModel, String flowerModelVariant, @Nullable BlockState bud, @Nullable ResourceLocation budModel, String budModelVariant, @Nullable MobEffectInstance nectarEffectInstance, @Nullable ResourceKey<Biome> biomeKey, @Nullable TagKey<Biome> biomeTagKey, @Nullable int nectarDuration, int naturalSpawnWeight) {
        if (nectarEffectInstance == null && flower == null && !resourceLocation.equals(BovinesAndButtercupsCommon.resourceLocation("missing"))) {
            Constants.LOG.warn("Moobloom Type '" + resourceLocation + "' does not have a flower or a mob effect, its nectar will have no associated effect");
        }
        this.resourceLocation = resourceLocation;
        this.loadingPriority = loadingPriority;
        this.flower = flower;
        this.flowerModel = flowerModel;
        this.flowerModelVariant = flowerModelVariant;
        this.bud = bud;
        this.budModel = budModel;
        this.budModelVariant = budModelVariant;
        this.effectInstance = nectarEffectInstance;
        this.biomeKey = biomeKey;
        this.biomeTagKey = biomeTagKey;
        this.nectarDuration = nectarDuration;
        this.naturalSpawnWeight = naturalSpawnWeight;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    @Nullable public MobEffectInstance getNectarEffectInstance() {
        return this.effectInstance;
    }

    @Nullable public BlockState getFlower() {
        return this.flower;
    }

    @Nullable public ResourceLocation getFlowerModel() {
        return this.flowerModel;
    }

    public String getFlowerModelVariant() {
        return this.flowerModelVariant;
    }

    @Nullable public BlockState getBud() {
        return this.bud;
    }

    @Nullable public ResourceLocation getBudModel() {
        return this.budModel;
    }

    public String getBudModelVariant() {
        return this.budModelVariant;
    }

    @Nullable public ResourceKey<Biome> getBiomeKey() {
        return this.biomeKey;
    }

    @Nullable public TagKey<Biome> getBiomeTagKey() {
        return this.biomeTagKey;
    }

    public int getNectarDuration() {
        return this.nectarDuration;
    }

    public int getNaturalSpawnWeight() {
        return this.naturalSpawnWeight;
    }

    public int getLoadingPriority() {
        return this.loadingPriority;
    }

    public void addAllBreedingRequirement(List<FlowerCowBreedingRequirements> breedingRequirements) {
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

        buf.writeBoolean(type.getFlower() != null);
        if (type.getFlower() != null) {
            buf.writeUtf(BlockStateParser.serialize(type.getFlower()), 32767);
        }
        buf.writeBoolean(type.getFlowerModel() != null);
        if (type.getFlowerModel() != null) {
            buf.writeResourceLocation(type.getFlowerModel());
        }
        buf.writeUtf(type.getFlowerModelVariant(), 32767);

        buf.writeBoolean(type.getBud() != null);
        if (type.getBud() != null) {
            buf.writeUtf(BlockStateParser.serialize(type.getBud()), 32767);
        }
        buf.writeBoolean(type.getBudModel() != null);
        if (type.getBudModel() != null) {
            buf.writeResourceLocation(type.getBudModel());
        }
        buf.writeUtf(type.getBudModelVariant(), 32767);

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

        buf.writeInt(type.getNectarDuration());
        buf.writeInt(type.getNaturalSpawnWeight());

        buf.writeInt(type.getBreedingRequirementsList().size());
        type.getBreedingRequirementsList().forEach(flowerCowBreedingRequirements -> FlowerCowBreedingRequirements.write(flowerCowBreedingRequirements, buf));
    }

    public static FlowerCowType read(FriendlyByteBuf buf) {
        ResourceLocation resourceLocation = buf.readResourceLocation();

        int loadingPriority = buf.readInt();

        boolean hasFlowerBlock = buf.readBoolean();
        BlockState flowerBlock = null;
        if (hasFlowerBlock) {
            String flowerBlockString = buf.readUtf(32767);
            try {
                flowerBlock = BlockStateParser.parseForBlock(Registry.BLOCK, flowerBlockString, false).blockState();
            } catch (CommandSyntaxException e) {
                Constants.LOG.warn("Exception reading blockstate in buffer: \"" + flowerBlockString + "\". " + e);
            }
        }

        boolean hasFlowerModelLocation = buf.readBoolean();
        ResourceLocation flowerModelLocation = null;
        if (hasFlowerModelLocation) {
            flowerModelLocation = buf.readResourceLocation();
        }

        String flowerModelVariant = buf.readUtf(32767);

        boolean hasBudBlock = buf.readBoolean();
        BlockState budBlock = null;
        if (hasBudBlock) {
            String budBlockString = buf.readUtf(32767);
            try {
                budBlock = BlockStateParser.parseForBlock(Registry.BLOCK, budBlockString, false).blockState();
            } catch (CommandSyntaxException e) {
                Constants.LOG.warn("Exception reading blockstate in buffer: \"" + budBlockString + "\". " + e);
            }
        }
        boolean hasBudModeLLocation = buf.readBoolean();
        ResourceLocation budModelLocation = null;
        if (hasBudModeLLocation) {
            budModelLocation = buf.readResourceLocation();
        }

        String budModelVariant = buf.readUtf(32767);

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

        int nectarDuration = buf.readInt();

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
                flowerBlock,
                flowerModelLocation,
                flowerModelVariant,
                budBlock,
                budModelLocation,
                budModelVariant,
                nectarEffectInstance,
                spawnBiome,
                spawnBiomeTag,
                nectarDuration,
                naturalSpawnWeight);

        type.addAllBreedingRequirement(breedingRequirementsList);

        return type;
    }

    public static FlowerCowType fromJson(ResourceLocation resourceLocation, JsonObject json) {
        int loadingPriority = 0;
        if (json.has("loading_priority")) {
            loadingPriority = json.getAsJsonPrimitive("loading_priority").getAsInt();
        }

        BlockState flowerBlock = null;
        if (json.has("flower_block")) {
            flowerBlock = JsonParsingUtil.readBlockState(json.getAsJsonPrimitive("flower_block").getAsString());
        }

        ResourceLocation flowerModelLocation = null;
        if (json.has("flower_model")) {
            flowerModelLocation = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("flower_model").getAsString());
        }

        String flowerModelVariant = "bovines";
        if (json.has("flower_model_variant")) {
            flowerModelVariant = json.getAsJsonPrimitive("flower_model_variant").getAsString();
        }

        BlockState budBlock = null;
        if (json.has("bud_block")) {
            budBlock = JsonParsingUtil.readBlockState(json.getAsJsonPrimitive("bud_block").getAsString());
        }

        ResourceLocation budModelLocation = null;
        if (json.has("bud_model")) {
            budModelLocation = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("bud_model").getAsString());
        }

        String budModelVariant = "bovines";
        if (json.has("bud_model_variant")) {
            flowerModelVariant = json.getAsJsonPrimitive("bud_model_variant").getAsString();
        }

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

        int nectarDuration = 400;
        if (json.has("nectar_duration")) {
            json.getAsJsonPrimitive("nectar_duration").getAsInt();
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
                flowerBlock,
                flowerModelLocation,
                flowerModelVariant,
                budBlock,
                budModelLocation,
                budModelVariant,
                nectarEffectInstance,
                spawnBiome,
                spawnBiomeTag,
                nectarDuration,
                naturalSpawnWeight);

        type.addAllBreedingRequirement(breedingRequirementsList);

        return type;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FlowerCowType otherType))
            return false;

        return otherType.resourceLocation.equals(this.resourceLocation) && (otherType.flower == null && this.flower == null || otherType.flower != null && this.flower != null && otherType.flower.equals(this.flower)) && (otherType.flowerModel == null && this.flowerModel == null || otherType.flowerModel != null && this.flowerModel != null && otherType.flowerModel.equals(this.flowerModel)) && (otherType.bud == null && this.bud == null || otherType.bud != null && this.bud != null && otherType.bud.equals(this.bud)) && (otherType.budModel == null && this.budModel == null || otherType.budModel != null && this.budModel != null && otherType.budModel.equals(this.budModel)) && (otherType.effectInstance == null && this.effectInstance == null || otherType.effectInstance != null && this.effectInstance != null && otherType.effectInstance.equals(this.effectInstance)) && otherType.nectarDuration == this.nectarDuration && otherType.naturalSpawnWeight == this.naturalSpawnWeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.resourceLocation, this.flower, this.flowerModel, this.bud, this.budModel, this.effectInstance, this.nectarDuration, this.naturalSpawnWeight);
    }
}