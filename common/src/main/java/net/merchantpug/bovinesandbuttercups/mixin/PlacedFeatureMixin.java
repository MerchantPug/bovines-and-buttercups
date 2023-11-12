package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.ChunkGeneratorAccess;
import net.merchantpug.bovinesandbuttercups.content.structure.RanchStructure;
import net.merchantpug.bovinesandbuttercups.registry.BovineStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(PlacedFeature.class)
public abstract class PlacedFeatureMixin {
    @Shadow public abstract Holder<ConfiguredFeature<?, ?>> feature();

    @Inject(method = "placeWithContext", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;forEach(Ljava/util/function/Consumer;)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$cancelPlacementIfInRanch(PlacementContext context, RandomSource randomSource, BlockPos pos, CallbackInfoReturnable<Boolean> cir, Stream<BlockPos> stream, ConfiguredFeature feature, MutableBoolean mutableBoolean) {
        if (!(context.getLevel() instanceof WorldGenRegion region) || ((ChunkGeneratorAccess)context.generator()).bovinesandbuttercups$getStep() != GenerationStep.Decoration.VEGETAL_DECORATION) return;

        List<StructureSet.StructureSelectionEntry> blockingStructures = new ArrayList<>();
        ((ServerChunkCache)context.getLevel().getChunkSource()).getGeneratorState().possibleStructureSets().forEach(holder -> holder.value().structures().forEach(entry -> {
            if (entry.structure().value().type() == BovineStructureTypes.RANCH.get() && (((RanchStructure)entry.structure().value()).getAllowedFeatures().isEmpty() || !((RanchStructure)entry.structure().value()).getAllowedFeatures().get().contains(this.feature())) && entry.structure().value().biomes().contains(region.getBiome(pos))) {
                blockingStructures.add(entry);
            }
        }));

        if (blockingStructures.isEmpty()) return;

        for (StructureSet.StructureSelectionEntry entry : blockingStructures) {
            if (((WorldGenRegionAccessor)region).getStructureManager().getStructureAt(pos, entry.structure().value()).isValid()) {
                cir.setReturnValue(false);
                break;
            }
        }
    }
}
