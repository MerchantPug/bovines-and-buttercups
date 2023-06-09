package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.util.CreativeTabHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BovineCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BOVINES_AND_BUTTERCUPS = CREATIVE_MODE_TAB.register("items", () -> CreativeModeTab.builder()
            .title(Component.translatable("bovinesandbuttercups.itemGroup.items"))
            .icon(() -> new ItemStack(BovineItems.BUTTERCUP.get()))
            .displayItems((params, output) -> {
                output.accept(BovineItems.MOOBLOOM_SPAWN_EGG.get());
                output.accept(BovineItems.FREESIA.get());
                output.accept(BovineItems.BIRD_OF_PARADISE.get());
                output.accept(BovineItems.BUTTERCUP.get());
                output.accept(BovineItems.LIMELIGHT.get());
                output.accept(BovineItems.CHARGELILY.get());
                output.accept(BovineItems.TROPICAL_BLUE.get());
                output.accept(BovineItems.HYACINTH.get());
                output.accept(BovineItems.PINK_DAISY.get());
                output.accept(BovineItems.SNOWDROP.get());
                output.acceptAll(CreativeTabHelper.getCustomFlowersForCreativeTab());
                output.acceptAll(CreativeTabHelper.getCustomMushroomsForCreativeTab());
                output.acceptAll(CreativeTabHelper.getCustomMushroomBlocksForCreativeTab());
                output.acceptAll(CreativeTabHelper.getNectarBowlsForCreativeTab());
            })
            .build());

    public static void init(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }

}
