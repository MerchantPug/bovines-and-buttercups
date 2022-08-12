package com.github.merchantpug.bovinesandbuttercups.data.block.flower;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.util.JsonParsingUtil;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class FlowerType {
    final ResourceLocation resourceLocation;
    @Nullable final String name;
    @Nullable final BlockState blockState;
    @Nullable final ResourceLocation modelLocation;
    final String modelVariant;
    @Nullable final ResourceLocation itemModelLocation;
    final String itemModelVariant;
    @Nullable final ResourceLocation pottedModelLocation;
    final String pottedModelVariant;
    final MobEffectInstance stewEffectInstance;
    final boolean withFlowerBlock;

    public FlowerType(ResourceLocation resourceLocation, @Nullable String name, @Nullable BlockState blockState, @Nullable ResourceLocation modelLocation, String modelVariant, @Nullable ResourceLocation itemModelLocation, String itemModelVariant, @Nullable ResourceLocation pottedModelLocation, String pottedModelVariant, @Nullable MobEffectInstance stewEffectInstance, boolean withFlowerBlock) {
        this.resourceLocation = resourceLocation;
        this.name = name;
        this.blockState = blockState;
        this.modelLocation = modelLocation;
        this.modelVariant = modelVariant;
        this.itemModelLocation = itemModelLocation;
        this.itemModelVariant = itemModelVariant;
        this.pottedModelLocation = pottedModelLocation;
        this.pottedModelVariant = pottedModelVariant;
        this.stewEffectInstance = stewEffectInstance;
        this.withFlowerBlock = withFlowerBlock;
    }

    public static final FlowerType MISSING = new FlowerType(Constants.resourceLocation("missing"), "block.bovinesandbuttercups.custom_flower", null, Constants.resourceLocation("missing_flower"), "bovines", Constants.resourceLocation("missing_flower_item"), "bovines", Constants.resourceLocation("potted_missing_flower"), "bovines", new MobEffectInstance(MobEffects.REGENERATION, 4), false);

    public static FlowerType fromKey(String name) {
        try {
            ResourceLocation id = ResourceLocation.tryParse(name);
            return FlowerTypeRegistry.get(id);
        } catch (Exception exception) {
            Constants.LOG.warn("Could not get Flower type from name '" + name + "'.");
        }
        return MISSING;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    @Nullable public String getName() {
        return name;
    }

    public MutableComponent getOrCreateNameTranslationKey() {
        return name != null ? Component.translatable(name) : Component.translatable("block." + resourceLocation.getNamespace() + "." + resourceLocation.getPath());
    }

    @Nullable
    public BlockState getBlockState() {
        return this.blockState;
    }

    @Nullable public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }

    public String getModelVariant() {
        return this.modelVariant;
    }

    @Nullable public ResourceLocation getItemModelLocation() {
        return this.itemModelLocation;
    }

    public String getItemModelVariant() {
        return this.itemModelVariant;
    }

    @Nullable public ResourceLocation getPottedModelLocation() {
        return this.pottedModelLocation;
    }

    public String getPottedModelVariant() {
        return this.pottedModelVariant;
    }

    @Nullable public MobEffectInstance getStewEffectInstance() {
        return this.stewEffectInstance;
    }

    public boolean isWithFlowerBlock() {
        return this.withFlowerBlock;
    }

    public static FlowerType read(FriendlyByteBuf buf) {
        ResourceLocation resourceLocation = buf.readResourceLocation();

        boolean hasName = buf.readBoolean();
        String name = null;
        if (hasName) {
            name = buf.readUtf(32767);
        }

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

        boolean hasFlowerModel = buf.readBoolean();
        ResourceLocation flowerModel = null;
        if (hasFlowerModel) {
            flowerModel = buf.readResourceLocation();
        }

        String flowerModelVariant = buf.readUtf(32767);


        boolean hasItemModel = buf.readBoolean();
        ResourceLocation itemModel = null;
        if (hasItemModel) {
            itemModel = buf.readResourceLocation();
        }

        String itemModelVariant = buf.readUtf(32767);


        boolean hasPottedModel = buf.readBoolean();
        ResourceLocation pottedModel = null;
        if (hasPottedModel) {
            pottedModel = buf.readResourceLocation();
        }

        String pottedModeLVariant = buf.readUtf(32767);

        boolean hasStewEffect = buf.readBoolean();
        MobEffectInstance stewEffect = null;
        if (hasStewEffect) {
            ResourceLocation stewEffectLocation = buf.readResourceLocation();
            Optional<MobEffect> optionalEffect = Registry.MOB_EFFECT.getOptional(stewEffectLocation);
            if (optionalEffect.isEmpty()) {
                Constants.LOG.warn("Could not find mob effect with id: " + stewEffectLocation);
            }
            int duration = buf.readInt();
            if (optionalEffect.isPresent()) {
                stewEffect = new MobEffectInstance(optionalEffect.get(), duration);
            }
        }

        boolean withFlowerBlock = buf.readBoolean();

        return new FlowerType(resourceLocation, name, flowerBlock, flowerModel, flowerModelVariant, itemModel, itemModelVariant, pottedModel, pottedModeLVariant, stewEffect, withFlowerBlock);
    }

    public static void write(FlowerType type, FriendlyByteBuf buf) {
        buf.writeResourceLocation(type.resourceLocation);
        buf.writeBoolean(type.name != null);
        if (type.name != null) {
            buf.writeUtf(type.name);
        }
        buf.writeBoolean(type.blockState != null);
        if (type.blockState != null) {
            buf.writeUtf(BlockStateParser.serialize(type.blockState), 32767);
        }
        buf.writeBoolean(type.modelLocation != null);
        if (type.modelLocation != null) {
            buf.writeResourceLocation(type.modelLocation);
        }
        buf.writeUtf(type.modelVariant);
        buf.writeBoolean(type.itemModelLocation != null);
        if (type.itemModelLocation != null) {
            buf.writeResourceLocation(type.itemModelLocation);
        }
        buf.writeUtf(type.itemModelVariant);
        buf.writeBoolean(type.pottedModelLocation != null);
        if (type.pottedModelLocation != null) {
            buf.writeResourceLocation(type.pottedModelLocation);
        }
        buf.writeUtf(type.pottedModelVariant);
        buf.writeBoolean(type.stewEffectInstance != null);
        if (type.stewEffectInstance != null) {
            buf.writeResourceLocation(Objects.requireNonNull(Registry.MOB_EFFECT.getKey(type.stewEffectInstance.getEffect())));
            buf.writeInt(type.stewEffectInstance.getDuration());
        }
        buf.writeBoolean(type.withFlowerBlock);
    }

    public static FlowerType fromJson(ResourceLocation resourceLocation, JsonObject json) {
        ResourceLocation resourceLocation2 = resourceLocation;
        if (json.has("location")) {
            resourceLocation2 = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("location").getAsString());
        }

        String name = null;
        if (json.has("name")) {
            name = json.getAsJsonPrimitive("name").getAsString();
        }
        BlockState blockState = null;
        if (json.has("block_state")) {
            blockState = JsonParsingUtil.readBlockState(json.getAsJsonPrimitive("block_state").getAsString());
        }
        ResourceLocation modelLocation = null;
        if (json.has("model_location")) {
            modelLocation = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("model_location").getAsString());
        }
        String modelVariant = "bovines";
        if (json.has("model_variant")) {
            modelVariant = json.getAsJsonPrimitive("model_variant").getAsString();
        }
        ResourceLocation itemModelLocation = null;
        if (modelLocation != null) {
            itemModelLocation = new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath() + "_item");
        }
        if (json.has("item_model_location")) {
            itemModelLocation = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("item_model_location").getAsString());
        }
        String itemModelVariant = modelVariant;
        if (json.has("item_model_variant")) {
            itemModelVariant = json.getAsJsonPrimitive("item_model_variant").getAsString();
        }
        ResourceLocation pottedModelLocaton = null;
        if (modelLocation != null) {
            pottedModelLocaton = new ResourceLocation(modelLocation.getNamespace(), "potted_" + modelLocation.getPath());
        }
        if (json.has("potted_model_location")) {
            pottedModelLocaton = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("potted_model_location").getAsString());
        }
        String pottedModelVariant = modelVariant;
        if (json.has("potted_model_variant")) {
            pottedModelVariant = json.getAsJsonPrimitive("potted_model_variant").getAsString();
        }
        MobEffectInstance stewEffectInstance = null;
        if (json.has("stew_effect") && json.get("stew_effect").isJsonObject()) {
            stewEffectInstance = JsonParsingUtil.readMobEffectInstance(json.getAsJsonObject("stew_effect"));
        }
        boolean withFlowerBlock = false;
        if (json.has("with_block")) {
            withFlowerBlock = json.getAsJsonPrimitive("with_block").getAsBoolean();
        }

        if (blockState == null && modelLocation == null) {
            throw new NullPointerException("Could not find value for either 'block_state' or 'model_location' field in FlowerType '" + resourceLocation.toString() + "'. Set a value for either one and try again.");
        }

        return new FlowerType(resourceLocation2, name, blockState, modelLocation, modelVariant, itemModelLocation, itemModelVariant, pottedModelLocaton, pottedModelVariant, stewEffectInstance, withFlowerBlock);
    }
}