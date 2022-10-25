package net.merchantpug.bovinesandbuttercups.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MobEffectArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import static net.minecraft.commands.Commands.*;

public class EffectLockdownCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("effect").requires(cs -> cs.hasPermission(2))
                    .then(literal("lockdown")
                        .then(argument("targets", EntityArgument.entities())
                            .then(argument("effect", MobEffectArgument.effect()).suggests((commandContext, suggestionsBuilder) -> EffectWithoutLockdownSuggestion.suggestions(suggestionsBuilder))
                                .executes((command) -> giveLockdownEffect(command, EntityArgument.getEntities(command, "targets"), MobEffectArgument.getEffect(command, "effect"), null, true)
                                )
                                .then(argument("duration", IntegerArgumentType.integer(1, 1000000))
                                    .executes((command) -> giveLockdownEffect(command, EntityArgument.getEntities(command, "targets"), MobEffectArgument.getEffect(command, "effect"), IntegerArgumentType.getInteger(command, "duration"), true)
                                    )
                                    .then(argument("hideParticles", BoolArgumentType.bool())
                                            .executes((command) -> giveLockdownEffect(command, EntityArgument.getEntities(command, "targets"), MobEffectArgument.getEffect(command, "effect"), IntegerArgumentType.getInteger(command, "duration"), !BoolArgumentType.getBool(command, "hideParticles")))))))));
    }

    private static int giveLockdownEffect(CommandContext<CommandSourceStack> command, Collection<? extends Entity> targets, MobEffect effect, @Nullable Integer seconds, boolean showParticles) throws CommandSyntaxException {
        if (effect instanceof LockdownEffect) {
            throw new SimpleCommandExceptionType(Component.translatable("commands.effect.lockdown.failed")).create();
        }
        int i = 0;
        int j = seconds != null ? seconds * 20 : 600;
        for (Entity entity : targets) {
            MobEffectInstance statusEffectInstance = new MobEffectInstance(BovineEffects.LOCKDOWN.get(), j, 0, false, showParticles);
            if (!(entity instanceof LivingEntity)) continue;
            Services.COMPONENT.addLockdownMobEffect((LivingEntity)entity, effect, j);
            Services.COMPONENT.syncLockdownMobEffects((LivingEntity)entity);
            if (!((LivingEntity)entity).addEffect(statusEffectInstance, command.getSource().getEntity())) continue;
            ++i;
        }
        if (i == 0) {
            throw new SimpleCommandExceptionType(Component.translatable("commands.effect.give.failed")).create();
        }
        if (targets.size() == 1) {
            command.getSource().sendSuccess(Component.translatable("commands.effect.lockdown.success.single", effect.getDisplayName(), targets.iterator().next().getDisplayName(), j / 20), true);
        } else {
            command.getSource().sendSuccess(Component.translatable("commands.effect.lockdown.success.multiple", effect.getDisplayName(), targets.size(), j / 20), true);
        }
        return i;
    }
}
