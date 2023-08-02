package net.merchantpug.bovinesandbuttercups.client.util;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.bovinestate.BovineBlockstateTypeRegistry;
import net.merchantpug.bovinesandbuttercups.api.bovinestate.BovineStatesAssociationRegistry;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

// TODO: Rewrite this entire thing.
public class BovineStateModelUtil {
    private static final BlockModelDefinition.Context CONTEXT = new BlockModelDefinition.Context();

    public static void initModels(ResourceManager manager, Consumer<ResourceLocation> consumer) {
        BovineStatesAssociationRegistry.clear();
        Map<ResourceLocation, Resource> blocks = manager.listResources("blockstates/bovinesandbuttercups", fileName -> fileName.getPath().endsWith(".json"));

        for (Map.Entry<ResourceLocation, Resource> resourceEntry : blocks.entrySet()) {
            StringBuilder newIdBuilder = new StringBuilder(resourceEntry.getKey().getPath());
            newIdBuilder.replace(0, 12, "");
            newIdBuilder.replace(newIdBuilder.length() - 5, newIdBuilder.length(), "");
            String newId = newIdBuilder.toString();
            ResourceLocation resourceLocation = new ResourceLocation(resourceEntry.getKey().getNamespace(), newId);

            try {
                Reader reader = resourceEntry.getValue().openAsReader();
                JsonElement json = JsonParser.parseReader(reader);
                reader.close();
                if (json instanceof JsonObject jsonObject) {
                    if (!jsonObject.has("type")) {
                        BovinesAndButtercups.LOG.error("Could not find 'type' field inside bovinestate json: {}.", resourceLocation);
                        continue;
                    }
                    ResourceLocation typeLocation = ResourceLocation.tryParse(jsonObject.get("type").getAsString());
                    StateDefinition<Block, BlockState> tempStateDefinition = null;
                    if (!Objects.equals(typeLocation, BovinesAndButtercups.asResource("item"))) {
                        try {
                            tempStateDefinition = BovineBlockstateTypeRegistry.get(typeLocation);
                        } catch (NullPointerException e) {
                            BovinesAndButtercups.LOG.warn("Could not find 'type' field value in bovinestate type registry. (Skipping). {}", e.getMessage());
                            continue;
                        }
                    }

                    if (tempStateDefinition == null) {
                        if (jsonObject.has("inventory")) {
                            ResourceLocation resourceLocation1 = ResourceLocation.tryParse(jsonObject.get("inventory").getAsString());
                            if (resourceLocation1 == null) {
                                BovinesAndButtercups.LOG.warn("Could not create valid resource location from string '{}'.", jsonObject.get("inventory").getAsString());
                            } else {
                                BovineStatesAssociationRegistry.registerItem(resourceLocation, null, true, resourceLocation1);
                                ModelResourceLocation inventoryModelLocation = new ModelResourceLocation(resourceLocation1, "inventory");
                                consumer.accept(inventoryModelLocation);
                            }
                        }
                        continue;
                    }

                    StateDefinition<Block, BlockState> stateDefinition = tempStateDefinition;
                    ImmutableList<BlockState> possibleStates = stateDefinition.getPossibleStates();
                    CONTEXT.setDefinition(stateDefinition);

                    if (jsonObject.has("linked_block_type")) {
                        ResourceLocation linkedType = ResourceLocation.tryParse(jsonObject.get("linked_block_type").getAsString());

                        if (linkedType == null) {
                            BovinesAndButtercups.LOG.info("Could not parse linked_block_type from key: {}.", jsonObject.get("linked_block_type").getAsString());
                            continue;
                        }

                        BovineStatesAssociationRegistry.registerBlock(linkedType, tempStateDefinition, resourceLocation);

                        if (jsonObject.has("inventory")) {
                            ResourceLocation resourceLocation1 = ResourceLocation.tryParse(jsonObject.get("inventory").getAsString());
                            if (resourceLocation1 == null) {
                                BovinesAndButtercups.LOG.warn("Could not create valid resource location from string '{}'.", jsonObject.get("inventory").getAsString());
                            } else {
                                BovineStatesAssociationRegistry.registerItem(linkedType, stateDefinition, false, resourceLocation1);
                                ModelResourceLocation inventoryModelLocation = new ModelResourceLocation(resourceLocation1, "inventory");
                                consumer.accept(inventoryModelLocation);
                            }
                        }
                    }

                    for (BlockState state : possibleStates) {
                        consumer.accept(new ModelResourceLocation(resourceLocation, "bovinesandbuttercups_" + BlockModelShaper.statePropertiesToString(state.getValues())));
                    }
                }
            } catch (Exception ignored) {

            }
        }
    }

    public static UnbakedModel getVariantModel(ResourceManager manager, ModelResourceLocation modelId) {
        if (modelId.getVariant().startsWith("bovinesandbuttercups_")) {
            ResourceLocation modelLocation = new ResourceLocation(modelId.getNamespace(), "blockstates/" + modelId.getPath() + ".json");
            Optional<Resource> resource = manager.getResource(modelLocation);

            if (resource.isEmpty()) {
                BovinesAndButtercups.LOG.error("Could not find BovineState file at location: {}.", modelLocation);
                return null;
            }

            JsonElement json = null;
            try {
                var reader = resource.get().openAsReader();
                json = JsonParser.parseReader(reader);
                reader.close();
            } catch (IOException e) {
                BovinesAndButtercups.LOG.error("Exception in reading Bovinestate JSON at location '"  + modelLocation + "'.", e);
            }

            if (json == null || !json.isJsonObject()) {
                BovinesAndButtercups.LOG.error("BovineState JSON at location '{}' was not found or is not a JSON object.", modelLocation);
                return null;
            }

            JsonObject jsonObject = json.getAsJsonObject();

            ResourceLocation typeLocation = ResourceLocation.tryParse(jsonObject.get("type").getAsString());
            StateDefinition<Block, BlockState> stateDefinition = BovineBlockstateTypeRegistry.get(typeLocation);
            if (stateDefinition == null) {
                return null;
            }
            CONTEXT.setDefinition(stateDefinition);

            BlockModelDefinition modelDefinition = null;
            try {
                Reader modelReader = resource.get().openAsReader();
                modelDefinition = BlockModelDefinition.fromStream(CONTEXT, modelReader);
                modelReader.close();
            } catch (Exception e) {
                BovinesAndButtercups.LOG.error("Could not create BlockModelDefinition from bovinestate '" + modelLocation + "' context:", e);
            }

            if (modelDefinition == null) {
                return null;
            }

            if (modelDefinition.isMultiPart()) {
                return modelDefinition.getMultiPart();
            }

            if (modelDefinition.hasVariant(modelId.getVariant().replaceFirst("bovinesandbuttercups_", ""))) {
                return modelDefinition.getVariant(modelId.getVariant().replaceFirst("bovinesandbuttercups_", ""));
            } else {
                return modelDefinition.getVariant("");
            }
        }
        return null;
    }

}
