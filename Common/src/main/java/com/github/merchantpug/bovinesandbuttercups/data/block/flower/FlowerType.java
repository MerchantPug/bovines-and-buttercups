package com.github.merchantpug.bovinesandbuttercups.data.block.flower;

import com.github.merchantpug.bovinesandbuttercups.Constants;
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
    final String name;
    @Nullable final BlockState flower;
    @Nullable final ResourceLocation flowerModel;
    final String flowerModelVariant;
    final MobEffectInstance stewEffectInstance;
    final boolean withFlowerBlock;

    public FlowerType(ResourceLocation resourceLocation, @Nullable String name, @Nullable BlockState flower, @Nullable ResourceLocation flowerModel, String flowerModelVariant, @Nullable MobEffectInstance stewEffectInstance, boolean withFlowerBlock) {
        this.resourceLocation = resourceLocation;
        this.name = name;
        this.flower = flower;
        this.flowerModel = flowerModel;
        this.flowerModelVariant = flowerModelVariant;
        this.stewEffectInstance = stewEffectInstance;
        this.withFlowerBlock = withFlowerBlock;
    }

    public static final FlowerType MISSING = new FlowerType(Constants.resourceLocation("missing"), "block.bovinesandbuttercups.custom_flower", null, Constants.resourceLocation("missing_flower"), "bovines", new MobEffectInstance(MobEffects.REGENERATION, 4), false);

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

    public MutableComponent getOrCreateNameTranslationKey() {
        return name != null ? Component.translatable(name) : Component.translatable("block." + resourceLocation.getNamespace() + "." + resourceLocation.getPath());
    }

    @Nullable
    public BlockState getFlower() {
        return this.flower;
    }

    @Nullable public ResourceLocation getFlowerModel() {
        return this.flowerModel;
    }

    public String getFlowerModelVariant() {
        return this.flowerModelVariant;
    }

    @Nullable public MobEffectInstance getStewEffectInstance() {
        return this.stewEffectInstance;
    }

    public boolean isWithFlowerBlock() {
        return this.withFlowerBlock;
    }

    @Nullable
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

        return new FlowerType(resourceLocation, name, flowerBlock, flowerModel, flowerModelVariant, stewEffect, withFlowerBlock);
    }

    public static void write(FlowerType type, FriendlyByteBuf buf) {
        buf.writeResourceLocation(type.resourceLocation);
        buf.writeBoolean(type.name != null);
        if (type.name != null) {
            buf.writeUtf(type.name);
        }
        buf.writeBoolean(type.flower != null);
        if (type.flower != null) {
            buf.writeUtf(BlockStateParser.serialize(type.flower), 32767);
        }
        buf.writeBoolean(type.flowerModel != null);
        if (type.flowerModel != null) {
            buf.writeResourceLocation(type.flowerModel);
        }
        buf.writeUtf(type.flowerModelVariant);
        buf.writeBoolean(type.stewEffectInstance != null);
        if (type.stewEffectInstance != null) {
            buf.writeResourceLocation(Objects.requireNonNull(Registry.MOB_EFFECT.getKey(type.stewEffectInstance.getEffect())));
            buf.writeInt(type.stewEffectInstance.getDuration());
        }
        buf.writeBoolean(type.withFlowerBlock);
    }
}