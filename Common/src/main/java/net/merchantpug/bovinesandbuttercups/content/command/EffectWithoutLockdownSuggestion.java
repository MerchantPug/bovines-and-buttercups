package net.merchantpug.bovinesandbuttercups.content.command;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.registries.BuiltInRegistries;

public class EffectWithoutLockdownSuggestion {
    public static CompletableFuture<Suggestions> suggestions(SuggestionsBuilder builder) {
        String remainder =  builder.getRemaining().toLowerCase(Locale.ROOT);

        if (BuiltInRegistries.MOB_EFFECT.keySet().isEmpty()) {
            return Suggestions.empty();
        }

        BuiltInRegistries.MOB_EFFECT.keySet().forEach(location -> {
            if ((location.toString().startsWith(remainder) || location.getNamespace().startsWith(remainder) || location.getNamespace().equals("minecraft") && location.getPath().startsWith(remainder)) && !location.equals(BovinesAndButtercups.asResource("lockdown"))) {
                builder.suggest(location.toString());
            }
        });

        return builder.buildFuture();
    }
}