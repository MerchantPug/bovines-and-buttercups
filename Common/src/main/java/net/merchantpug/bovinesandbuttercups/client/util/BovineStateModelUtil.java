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
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BovineStateModelUtil {
    private static final BlockModelDefinition.Context CONTEXT = new BlockModelDefinition.Context();

    public static void initModels(ModelBakery modelBakery, ResourceManager resourceManager, Map<ResourceLocation, UnbakedModel> unbakedCache, Map<ResourceLocation, UnbakedModel> topLevelModels) {
        BovineStatesAssociationRegistry.clear();
        UnbakedModel missingModel = unbakedCache.get(ModelBakery.MISSING_MODEL_LOCATION);
        Map<ResourceLocation, Resource> blocks = resourceManager.listResources("bovinesandbuttercups", fileName -> fileName.getPath().endsWith(".json"));

        for (Map.Entry<ResourceLocation, Resource> resourceEntry : blocks.entrySet()) {
            StringBuilder newIdBuilder = new StringBuilder(resourceEntry.getKey().getPath());
            newIdBuilder.replace(newIdBuilder.length() - 5, newIdBuilder.length(), "");
            String newId = newIdBuilder.toString();
            ResourceLocation resourceLocation = new ResourceLocation(resourceEntry.getKey().getNamespace(), newId);

            try {
                Reader reader = resourceEntry.getValue().openAsReader();
                JsonElement json = JsonParser.parseReader(reader);
                reader.close();
                if (json instanceof JsonObject jsonObject) {
                    ResourceLocation typeLocation = ResourceLocation.tryParse(jsonObject.get("type").getAsString());
                    StateDefinition<Block, BlockState> tempStateDefinition = null;
                    if (!Objects.equals(typeLocation, BovinesAndButtercups.asResource("item"))) {
                        try {
                            tempStateDefinition = BovineBlockstateTypeRegistry.get(typeLocation);
                        } catch (NullPointerException e) {
                            BovinesAndButtercups.LOG.warn("Could not find 'type' field value in registry. (Skipping). {}", e.getMessage());
                        }
                    }

                    if (jsonObject.has("inventory")) {
                        ModelResourceLocation inventoryModelLocation = new ModelResourceLocation(jsonObject.get("inventory").getAsString(), "inventory");
                        UnbakedModel model = modelBakery.getModel(inventoryModelLocation);
                        ModelResourceLocation remappedModelLocation = new ModelResourceLocation(resourceLocation, "inventory");
                        unbakedCache.put(remappedModelLocation, model);
                        topLevelModels.put(remappedModelLocation, model);
                    }

                    if (tempStateDefinition == null) continue;

                    if (jsonObject.has("linked_block_type")) {
                        BovineStatesAssociationRegistry.register(ResourceLocation.tryParse(jsonObject.get("linked_block_type").getAsString()), tempStateDefinition.getOwner(), resourceLocation);
                    }

                    StateDefinition<Block, BlockState> stateDefinition = tempStateDefinition;
                    CONTEXT.setDefinition(stateDefinition);

                    Reader modelReader = resourceEntry.getValue().openAsReader();
                    BlockModelDefinition modelDefinition = BlockModelDefinition.fromStream(CONTEXT, modelReader);
                    modelReader.close();

                    ImmutableList<BlockState> possibleStates = stateDefinition.getPossibleStates();

                    MultiPart multipart = null;
                    HashMap<BlockState, UnbakedModel> map = new HashMap<>();

                    if (modelDefinition.isMultiPart()) {
                        multipart = modelDefinition.getMultiPart();
                        MultiPart finalMultipart = multipart;
                        possibleStates.forEach((state) -> map.put(state, finalMultipart));
                    }

                    MultiPart finalMultipart = multipart;

                    modelDefinition.getVariants().forEach((string, multiVariant) -> {
                        try {
                            possibleStates.stream().filter(ModelBakery.predicate(stateDefinition, string)).forEach((state) -> {
                                UnbakedModel unbakedModel = map.put(state, multiVariant);
                                if (unbakedModel != null && unbakedModel != finalMultipart) {
                                    map.put(state, missingModel);
                                    throw new RuntimeException("Overlapping definition with: " + modelDefinition.getVariants().entrySet().stream().filter((model) -> model.getValue() == unbakedModel).findFirst().get().getKey());
                                }
                            });
                        } catch (Exception e) {
                            BovinesAndButtercups.LOG.warn("Exception loading bovinestate definition: '{}' in resourcepack: '{}' for variant: '{}': {}", newId, multiVariant, string, e.getMessage());
                        }
                    });

                    map.forEach((state, unbakedModel) -> {
                        ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(resourceLocation, state);
                        unbakedCache.put(modelLocation, unbakedModel);
                        topLevelModels.put(modelLocation, unbakedModel);
                    });
                }
            } catch (Exception ignored) {

            }
        }
    }
}
