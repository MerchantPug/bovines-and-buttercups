package com.github.merchantpug.bovinesandbuttercups.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ResourceLocationException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class JsonReadingUtil {
    public static ResourceLocation readResourceLocation(String locationString) {
        if (locationString.contains(":")) {
            String[] idSplit = locationString.split(":");
            if (idSplit.length != 2) {
                throw new ResourceLocationException("Invalid number of ':' in resource location. \"" + locationString + "\".");
            }
        }
        return new ResourceLocation(locationString);
    }

    public static BlockState readBlockState(String blockStateString) {
        try {
            return BlockStateParser.parseForBlock(Registry.BLOCK, blockStateString, false).blockState();
        } catch (CommandSyntaxException e) {
            throw new JsonParseException(e);
        }
    }

    public static MobEffectInstance readMobEffectInstance(JsonObject json) {
        if (json.isJsonObject()) {
            String effectString = json.get("effect").getAsString();
            Optional<MobEffect> optionalEffect = Registry.MOB_EFFECT.getOptional(ResourceLocation.tryParse(effectString));
            if (optionalEffect.isEmpty()) {
                throw new JsonSyntaxException("Could not find mob effect with id: " + effectString);
            }
            int duration = json.get("duration").getAsInt();
            int amplifier = 0;
            if (json.has("amplifier")) {
                amplifier = json.get("amplifier").getAsInt();
            }
            return new MobEffectInstance(optionalEffect.get(), duration, amplifier);
        } else {
            throw new JsonSyntaxException("Expected mob effect to be a json object.");
        }
    }
}
