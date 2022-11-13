package net.merchantpug.bovinesandbuttercups.client.util;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.client.api.BovineBlockstateTypeRegistry;
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

public class BovineStateModelUtil {
    private static final BlockModelDefinition.Context CONTEXT = new BlockModelDefinition.Context();

    public static void initModels(ModelBakery modelBakery, ResourceManager resourceManager, Map<ResourceLocation, UnbakedModel> unbakedCache, Map<ResourceLocation, UnbakedModel> topLevelModels) {
        UnbakedModel missingModel = unbakedCache.get(ModelBakery.MISSING_MODEL_LOCATION);
        Map<ResourceLocation, Resource> blocks = resourceManager.listResources("bovinestates", fileName -> fileName.getPath().endsWith(".json"));

        for (Map.Entry<ResourceLocation, Resource> resourceEntry : blocks.entrySet()) {
            StringBuilder newIdBuilder = new StringBuilder(resourceEntry.getKey().getPath());
            newIdBuilder.replace(0, 13, "");
            newIdBuilder.replace(newIdBuilder.length() - 5, newIdBuilder.length(), "");
            String newId = newIdBuilder.toString();

            try {
                Reader reader = resourceEntry.getValue().openAsReader();
                JsonElement json = JsonParser.parseReader(reader);
                reader.close();
                if (json instanceof JsonObject jsonObject) {
                    ResourceLocation resourceLocation = ResourceLocation.tryParse(jsonObject.get("type").getAsString());
                    StateDefinition<Block, BlockState> tempStateDefinition = null;
                    try {
                        tempStateDefinition = BovineBlockstateTypeRegistry.get(resourceLocation);
                    } catch (NullPointerException e) {
                        BovinesAndButtercups.LOG.warn("Could not find 'type' field value in registry. (Skipping). {}", e.getMessage());
                    }

                    if (tempStateDefinition == null) continue;

                    if (jsonObject.has("inventory")) {
                        ModelResourceLocation inventoryModelLocation = new ModelResourceLocation(jsonObject.get("inventory").getAsString(), "inventory");
                        UnbakedModel model = modelBakery.getModel(inventoryModelLocation);
                        unbakedCache.put(inventoryModelLocation, model);
                        topLevelModels.put(inventoryModelLocation, model);
                    }

                    StateDefinition<Block, BlockState> stateDefinition = tempStateDefinition;
                    CONTEXT.setDefinition(stateDefinition);

                    Reader modelDefinitionReader = resourceEntry.getValue().openAsReader();
                    BlockModelDefinition modelDefinition = BlockModelDefinition.fromStream(CONTEXT, modelDefinitionReader);
                    modelDefinitionReader.close();

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
                        ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(new ResourceLocation(resourceEntry.getKey().getNamespace(), newId), state);
                        unbakedCache.put(modelLocation, unbakedModel);
                        topLevelModels.put(modelLocation, unbakedModel);
                    });
                }
            } catch (Exception ignored) {

            }
        }
    }
}
