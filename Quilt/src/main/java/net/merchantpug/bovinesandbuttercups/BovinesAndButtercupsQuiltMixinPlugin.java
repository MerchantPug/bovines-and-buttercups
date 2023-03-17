package net.merchantpug.bovinesandbuttercups;

import org.objectweb.asm.tree.ClassNode;
import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class BovinesAndButtercupsQuiltMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // TODO: Reintroduce Inspecio and EMI compat
        if (mixinClassName.startsWith("net.merchantpug.bovinesandbuttercups.mixin.quilt.inspecio") /*&& !QuiltLoader.isModLoaded("inspecio")*/) {
            return false;
        } else if (mixinClassName.startsWith("net.merchantpug.bovinesandbuttercups.mixin.quilt.emi") /*&& !QuiltLoader.isModLoaded("emi")*/) {
            return false;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
