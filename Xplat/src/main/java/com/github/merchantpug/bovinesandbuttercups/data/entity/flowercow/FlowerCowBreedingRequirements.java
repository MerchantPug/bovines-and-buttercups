package com.github.merchantpug.bovinesandbuttercups.data.entity.flowercow;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.util.JsonParsingUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlowerCowBreedingRequirements {
    private final List<ResourceLocation> possiblePrimaryParents;
    private final List<ResourceLocation> possibleOtherParents;
    @Nullable
    public final Item associatedItem;
    public final float chance;
    public final float boostedChance;

    public FlowerCowBreedingRequirements(List<ResourceLocation> possiblePrimaryParents, List<ResourceLocation> possibleOtherParents, @Nullable Item flower, float chance, float boostedChance) {
        this.possiblePrimaryParents = possiblePrimaryParents;
        this.possibleOtherParents = possibleOtherParents;
        this.associatedItem = flower;
        this.chance = chance;
        this.boostedChance = boostedChance;
    }

    public boolean doesApply(FlowerCowType.Instance parent, FlowerCowType.Instance  otherParent) {
        return possiblePrimaryParents.stream().anyMatch(rl -> rl.equals(parent.getId())) && possibleOtherParents.stream().anyMatch(rl -> rl.equals(otherParent.getId())) || possibleOtherParents.stream().anyMatch(rl -> rl.equals(parent.getId())) && possiblePrimaryParents.stream().anyMatch(rl -> rl.equals(otherParent.getId()));
    }

    public boolean isBoosted(FlowerCowType.Instance parent, FlowerCowType.Instance  otherParent) {
        return false;
    }

    @Nullable
    public static FlowerCowBreedingRequirements read(FriendlyByteBuf buf) {
        int primaryParentListSize = buf.readInt();
        List<ResourceLocation> possiblePrimaryParentList = new ArrayList<>();
        for (int i = 0; i < primaryParentListSize; i++) {
            possiblePrimaryParentList.add(buf.readResourceLocation());
        }
        int secondaryParentListSize = buf.readInt();
        List<ResourceLocation> possibleSecondaryParentList = new ArrayList<>();
        for (int i = 0; i < secondaryParentListSize; i++) {
            possibleSecondaryParentList.add(buf.readResourceLocation());
        }
        Item associatedItem = null;
        boolean hasAssociatedItem = buf.readBoolean();
        if (hasAssociatedItem) {
            ResourceLocation resourceLocation = buf.readResourceLocation();
            Optional<Item> potentialItem = Registry.ITEM.getOptional(resourceLocation);
            if (potentialItem.isEmpty()) {
                Constants.LOG.warn("Could not find item '" + resourceLocation + "' in item registry.");
                return null;
            }
            associatedItem = potentialItem.get();
        }
        float chance = buf.readFloat();
        float boostedChance = buf.readFloat();
        return new FlowerCowBreedingRequirements(possiblePrimaryParentList, possibleSecondaryParentList, associatedItem, chance, boostedChance);
    }

    public static void write(FlowerCowBreedingRequirements requirements, FriendlyByteBuf buf) {
        buf.writeInt(requirements.possiblePrimaryParents.size());
        requirements.possiblePrimaryParents.forEach(buf::writeResourceLocation);
        buf.writeInt(requirements.possibleOtherParents.size());
        requirements.possibleOtherParents.forEach(buf::writeResourceLocation);
        buf.writeBoolean(requirements.associatedItem != null);
        if (requirements.associatedItem != null) {
            buf.writeResourceLocation(Registry.ITEM.getKey(requirements.associatedItem));
        }
        buf.writeFloat(requirements.chance);
        buf.writeFloat(requirements.boostedChance);
    }


    public static FlowerCowBreedingRequirements fromJson(JsonObject json) {
        List<ResourceLocation> possiblePrimaryParentList = new ArrayList<>();
        JsonElement primaryParent = json.get("primary_parent");
        if (primaryParent.isJsonArray()) {
            primaryParent.getAsJsonArray().forEach(jsonElement -> possiblePrimaryParentList.add(JsonParsingUtil.readResourceLocation(jsonElement.getAsString())));
        } else if (primaryParent.isJsonPrimitive()) {
            possiblePrimaryParentList.add(JsonParsingUtil.readResourceLocation(primaryParent.getAsString()));
        }

        List<ResourceLocation> possibleSecondaryParentList = new ArrayList<>();
        JsonElement secondaryParent = json.get("secondary_parent");
        if (secondaryParent.isJsonArray()) {
            secondaryParent.getAsJsonArray().forEach(jsonElement -> possibleSecondaryParentList.add(JsonParsingUtil.readResourceLocation(jsonElement.getAsString())));
        } else if (secondaryParent.isJsonPrimitive()) {
            possibleSecondaryParentList.add(JsonParsingUtil.readResourceLocation(secondaryParent.getAsString()));
        }

        Item associatedItem = null;
        if (json.has("effective_item")) {
            ResourceLocation resourceLocation = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("effective_item").getAsString());
            Optional<Item> potentialItem = Registry.ITEM.getOptional(resourceLocation);
            if (potentialItem.isEmpty()) {
                throw new NullPointerException("Could not find item '" + resourceLocation + "' in item registry.");
            }
            associatedItem = potentialItem.get();
        }

        float chance = json.getAsJsonPrimitive("chance").getAsFloat();
        if (chance < 0.0 || chance > 1.0) {
            throw new IllegalArgumentException("Moobloom breeding chance is either below 0.0 or 1.0.");
        }
        float boostedChance = chance;
        if (json.has("boosted_chance")) {
            boostedChance = json.getAsJsonPrimitive("boosted_chance").getAsFloat();
            if (boostedChance < 0.0 || boostedChance > 1.0) {
                throw new IllegalArgumentException("Moobloom boosted breeding chance is either below 0.0 or 1.0.");
            }
        }

        return new FlowerCowBreedingRequirements(possiblePrimaryParentList, possibleSecondaryParentList, associatedItem, chance, boostedChance);
    }
}
