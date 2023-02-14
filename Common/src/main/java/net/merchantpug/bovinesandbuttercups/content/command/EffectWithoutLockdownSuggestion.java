package net.merchantpug.bovinesandbuttercups.content.command;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;

import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

public class EffectWithoutLockdownSuggestion {
    public static CompletableFuture<Suggestions> suggestions(CommandBuildContext context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(context.holderLookup(Registries.MOB_EFFECT).filterElements(effect -> !(effect instanceof LockdownEffect)).listElementIds().map(ResourceKey::location), builder);
    }
}