package com.github.merchantpug.bovinesandbuttercups.data.block.mushroom;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.util.JsonParsingUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MushroomType {
    final ResourceLocation resourceLocation;
    @Nullable final String name;
    @Nullable final BlockState blockState;
    @Nullable final ResourceLocation modelLocation;
    final String modelVariant;
    @Nullable final ResourceLocation itemModelLocation;
    final String itemModelVariant;
    @Nullable final ResourceLocation pottedModelLocation;
    final String pottedModelVariant;
    final String hugeBlockName;
    @Nullable final ResourceLocation hugeBlockModelLocation;
    final String hugeBlockModelVariant;
    @Nullable final ResourceLocation hugeBlockItemModelLocation;
    final String hugeBlockItemModelVariant;
    final List<ResourceLocation> hugeMushroomStructureList = new ArrayList<>();
    final boolean withMushroomBlocks;

    public MushroomType(ResourceLocation resourceLocation, @Nullable String name, @Nullable BlockState blockState, @Nullable ResourceLocation modelLocation, String modelVariant, @Nullable ResourceLocation itemModelLocation, String itemModelVariant, @Nullable ResourceLocation pottedModelLocation, String pottedModelVariant, @Nullable String hugeBlockName, @Nullable ResourceLocation hugeBlockModelLocation, String hugeBlockModelVariant, @Nullable ResourceLocation hugeBlockItemModelLocation, String hugeBlockItemModelVariant, boolean withMushroomBlocks) {
        this.resourceLocation = resourceLocation;
        this.name = name;
        this.blockState = blockState;
        this.modelLocation = modelLocation;
        this.modelVariant = modelVariant;
        this.itemModelLocation = itemModelLocation;
        this.itemModelVariant = itemModelVariant;
        this.pottedModelLocation = pottedModelLocation;
        this.pottedModelVariant = pottedModelVariant;
        this.hugeBlockName = hugeBlockName;
        this.hugeBlockModelLocation = hugeBlockModelLocation;
        this.hugeBlockModelVariant = hugeBlockModelVariant;
        this.hugeBlockItemModelLocation = hugeBlockItemModelLocation;
        this.hugeBlockItemModelVariant = hugeBlockItemModelVariant;
        this.withMushroomBlocks = withMushroomBlocks;
    }

    public static final MushroomType MISSING = new MushroomType(Constants.resourceLocation("missing"), "block.bovinesandbuttercups.custom_mushroom", null, Constants.resourceLocation("missing_mushroom"), "bovines", Constants.resourceLocation("missing_mushroom_item"),"bovines", Constants.resourceLocation("potted_missing_mushroom"), "bovines", "block.bovinesandbuttercups.custom_mushroom_block", Constants.resourceLocation("missing_mushroom_block"), "bovines", Constants.resourceLocation("missing_mushroom_block_item"), "bovines", false);

    public static MushroomType fromKey(String name) {
        try {
            ResourceLocation id = ResourceLocation.tryParse(name);
            return MushroomTypeRegistry.get(id);
        } catch (Exception exception) {
            Constants.LOG.warn("Could not get Mushroom type from name '" + name + "'.");
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

    public MutableComponent getOrCreateHugeBlockNameTranslationKey() {
        return hugeBlockName != null ? Component.translatable(hugeBlockName) : Component.translatable("block." + resourceLocation.getNamespace() + "." + resourceLocation.getPath() + "_block");
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

    public ResourceLocation getHugeBlockResourceLocation() {
        return this.resourceLocation;
    }

    @Nullable public String getHugeBlockName() {
        return name;
    }

    @Nullable public ResourceLocation getHugeBlockModelLocation() {
        return this.hugeBlockModelLocation;
    }

    public String getHugeBlockModelVariant() {
        return this.hugeBlockModelVariant;
    }

    @Nullable public ResourceLocation getHugeBlockItemModelLocation() {
        return this.hugeBlockItemModelLocation;
    }

    public String getHugeBlockItemModelVariant() {
        return this.hugeBlockItemModelVariant;
    }

    public List<ResourceLocation> getHugeMushroomStructureList() {
        return this.hugeMushroomStructureList;
    }

    public void addAllToHugeMushroomStructureList(List<ResourceLocation> template) {
        this.hugeMushroomStructureList.addAll(template);
    }

    public boolean isWithMushroomBlocks() {
        return this.withMushroomBlocks;
    }

    public static MushroomType read(FriendlyByteBuf buf) {
        ResourceLocation resourceLocation = buf.readResourceLocation();

        boolean hasName = buf.readBoolean();
        String name = null;
        if (hasName) {
            name = buf.readUtf(32767);
        }

        boolean hasMushroomBlock = buf.readBoolean();
        BlockState mushroomBlock = null;
        if (hasMushroomBlock) {
            String mushroomBlockString = buf.readUtf(32767);
            try {
                mushroomBlock = BlockStateParser.parseForBlock(Registry.BLOCK, mushroomBlockString, false).blockState();
            } catch (CommandSyntaxException e) {
                Constants.LOG.warn("Exception reading blockstate in buffer: \"" + mushroomBlockString + "\". " + e);
            }
        }

        boolean hasMushroomModel = buf.readBoolean();
        ResourceLocation mushroomModel = null;
        if (hasMushroomModel) {
            mushroomModel = buf.readResourceLocation();
        }

        String mushroomModelVariant = buf.readUtf(32767);

        boolean hasMushroomItemModel = buf.readBoolean();
        ResourceLocation mushroomItemModel = null;
        if (hasMushroomItemModel) {
            mushroomItemModel = buf.readResourceLocation();
        }

        String mushroomItemModelVariant = buf.readUtf(32767);

        boolean hasPottedMushroomModel = buf.readBoolean();
        ResourceLocation pottedMushroomModel = null;
        if (hasPottedMushroomModel) {
            pottedMushroomModel = buf.readResourceLocation();
        }

        String pottedMushroomModelVariant = buf.readUtf(32767);

        boolean hasHugeBlockName = buf.readBoolean();
        String hugeBlockName = null;
        if (hasHugeBlockName) {
            hugeBlockName = buf.readUtf(32767);
        }

        boolean hasHugeBlockModel = buf.readBoolean();
        ResourceLocation hugeBlockModel = null;
        if (hasHugeBlockModel) {
            hugeBlockModel = buf.readResourceLocation();
        }

        String hugeBlockModelVariant = buf.readUtf(32767);


        boolean hasHugeBlockItemModel = buf.readBoolean();
        ResourceLocation hugeBlockItemModel = null;
        if (hasHugeBlockItemModel) {
            hugeBlockItemModel = buf.readResourceLocation();
        }

        String hugeBlockItemModelVariant = buf.readUtf(32767);

        int mushroomStructureListSize = buf.readInt();
        List<ResourceLocation> structureTemplateList = new ArrayList<>();
        if (mushroomStructureListSize > 0) {
            for (int i = 0; i < mushroomStructureListSize; i++) {
                structureTemplateList.add(buf.readResourceLocation());
            }
        }

        boolean withMushroomBlocks = buf.readBoolean();

        MushroomType mushroomType = new MushroomType(resourceLocation, name, mushroomBlock, mushroomModel, mushroomModelVariant, mushroomItemModel, mushroomItemModelVariant, pottedMushroomModel, pottedMushroomModelVariant, hugeBlockName, hugeBlockModel, hugeBlockModelVariant, hugeBlockItemModel, hugeBlockItemModelVariant, withMushroomBlocks);
        mushroomType.addAllToHugeMushroomStructureList(structureTemplateList);

        return mushroomType;
    }

    public static void write(MushroomType type, FriendlyByteBuf buf) {
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

        buf.writeBoolean(type.hugeBlockName != null);
        if (type.hugeBlockName != null) {
            buf.writeUtf(type.hugeBlockName);
        }
        buf.writeBoolean(type.hugeBlockModelLocation != null);
        if (type.hugeBlockModelLocation != null) {
            buf.writeResourceLocation(type.hugeBlockModelLocation);
        }
        buf.writeUtf(type.hugeBlockModelVariant);

        buf.writeBoolean(type.hugeBlockItemModelLocation != null);
        if (type.hugeBlockItemModelLocation != null) {
            buf.writeResourceLocation(type.hugeBlockItemModelLocation);
        }
        buf.writeUtf(type.hugeBlockItemModelVariant);

        buf.writeInt(type.hugeMushroomStructureList.size());
        for (ResourceLocation templateLocation : type.hugeMushroomStructureList) {
            buf.writeResourceLocation(templateLocation);
        }

        buf.writeBoolean(type.withMushroomBlocks);
    }

    public static MushroomType fromJson(ResourceLocation resourceLocation, JsonObject json) {
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
        if (json.has("item_model_location")) {
            itemModelLocation = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("item_model_location").getAsString());
        } else if (modelLocation != null) {
            itemModelLocation = new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath() + "_item");
        }
        String itemModelVariant = modelVariant;
        if (json.has("item_model_variant")) {
            itemModelVariant = json.getAsJsonPrimitive("item_model_variant").getAsString();
        }

        ResourceLocation pottedModelLocaton = null;
        if (json.has("potted_model_location")) {
            pottedModelLocaton = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("potted_model_location").getAsString());
        } else if (modelLocation != null) {
            pottedModelLocaton = new ResourceLocation(modelLocation.getNamespace(), "potted_" + modelLocation.getPath());
        }
        String pottedModelVariant = modelVariant;
        if (json.has("potted_model_variant")) {
            pottedModelVariant = json.getAsJsonPrimitive("potted_model_variant").getAsString();
        }

        String hugeBlockName = null;
        if (json.has("huge_block_name")) {
            hugeBlockName = json.getAsJsonPrimitive("huge_block_name").getAsString();
        }
        ResourceLocation hugeBlockModelLocation = null;
        if (json.has("huge_block_model_location")) {
            hugeBlockModelLocation = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("huge_block_model_location").getAsString());
        }
        String hugeBlockModelVariant = "bovines";
        if (json.has("huge_block_model_variant")) {
            hugeBlockModelVariant = json.getAsJsonPrimitive("huge_block_model_variant").getAsString();
        }

        ResourceLocation hugeBlockItemModelLocation = null;
        if (json.has("huge_block_item_model_location")) {
            hugeBlockItemModelLocation = JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("huge_block_item_model_location").getAsString());
        } else if (hugeBlockModelLocation != null) {
            hugeBlockItemModelLocation = new ResourceLocation(hugeBlockModelLocation.getNamespace(), hugeBlockModelLocation.getPath() + "_item");
        }
        String hugeBlockItemModelVariant = hugeBlockModelVariant;
        if (json.has("huge_block_item_model_variant")) {
            hugeBlockItemModelVariant = json.getAsJsonPrimitive("huge_block_item_model_variant").getAsString();
        }

        List<ResourceLocation> mushroomStructures = new ArrayList<>();
        if (json.has("huge_structure_templates")) {
            if (json.get("huge_structure_templates").isJsonArray()) {
                json.get("huge_structure_templates").getAsJsonArray().forEach(jsonElement -> {
                    mushroomStructures.add(JsonParsingUtil.readResourceLocation(jsonElement.getAsJsonPrimitive().getAsString()));
                });
            } else if (json.get("huge_structure_templates").isJsonPrimitive()) {
                mushroomStructures.add(JsonParsingUtil.readResourceLocation(json.getAsJsonPrimitive("huge_structure_templates").getAsString()));
            } else {
                throw new JsonParseException("Expected 'huge_structure_templates' field to be either JSON Primitive or JSON Array.");
            }
        }

        boolean withMushroomBlocks = false;
        if (json.has("with_blocks")) {
            withMushroomBlocks = json.getAsJsonPrimitive("with_blocks").getAsBoolean();
        }

        if (blockState == null && modelLocation == null) {
            throw new NullPointerException("Could not find value for either 'block_state' or 'model_location' field in MushroomType '" + resourceLocation.toString() + "'. Set a value for either one and try again.");
        }

        MushroomType mushroomType = new MushroomType(resourceLocation2, name, blockState, modelLocation, modelVariant, itemModelLocation, itemModelVariant, pottedModelLocaton, pottedModelVariant, hugeBlockName, hugeBlockModelLocation, hugeBlockModelVariant, hugeBlockItemModelLocation, hugeBlockItemModelVariant, withMushroomBlocks);
        mushroomType.addAllToHugeMushroomStructureList(mushroomStructures);

        return mushroomType;
    }
}