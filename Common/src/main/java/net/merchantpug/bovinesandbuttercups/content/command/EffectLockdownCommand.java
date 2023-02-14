package net.merchantpug.bovinesandbuttercups.content.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EffectLockdownCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("effect").requires(cs -> cs.hasPermission(2))
                    .then(Commands.literal("lockdown")
                        .then(Commands.argument("targets", EntityArgument.entities())
                            .then(Commands.argument("effect", ResourceArgument.resource(context, Registries.MOB_EFFECT)).suggests((commandContext, suggestionsBuilder) -> EffectWithoutLockdownSuggestion.suggestions(context, suggestionsBuilder))
                                    .executes((command) -> giveLockdownEffect(command, EntityArgument.getEntities(command, "targets"),  ResourceArgument.getMobEffect(command, "effect"), null, true))
                                .then(Commands.argument("duration", IntegerArgumentType.integer(1, 1000000))
                                        .executes((command) -> giveLockdownEffect(command, EntityArgument.getEntities(command, "targets"), ResourceArgument.getMobEffect(command, "effect"), IntegerArgumentType.getInteger(command, "duration"), true))
                                    .then(Commands.argument("hideParticles", BoolArgumentType.bool())
                                            .executes((command) -> giveLockdownEffect(command, EntityArgument.getEntities(command, "targets"), ResourceArgument.getMobEffect(command, "effect"), IntegerArgumentType.getInteger(command, "duration"), !BoolArgumentType.getBool(command, "hideParticles"))
                                            )
                                        )
                                    )
                                )
                            )
                        )
        );
    }

    private static int giveLockdownEffect(CommandContext<CommandSourceStack> command, Collection<? extends Entity> targets, Holder.Reference<MobEffect> effect, @Nullable Integer seconds, boolean showParticles) throws CommandSyntaxException {
        if (effect.value() instanceof LockdownEffect) {
            throw new SimpleCommandExceptionType(Component.translatable("commands.effect.lockdown.failed")).create();
        }
        int i = 0;
        int j = seconds != null ? seconds * 20 : 600;
        for (Entity entity : targets) {
            MobEffectInstance statusEffectInstance = new MobEffectInstance(BovineEffects.LOCKDOWN.get(), j, 0, false, showParticles);
            if (!(entity instanceof LivingEntity)) continue;
            Services.COMPONENT.addLockdownMobEffect((LivingEntity)entity, effect.value(), j);
            Services.COMPONENT.syncLockdownMobEffects((LivingEntity)entity);
            ((LivingEntity)entity).addEffect(statusEffectInstance, command.getSource().getEntity());
            ++i;
        }
        if (i == 0) {
            throw new SimpleCommandExceptionType(Component.translatable("commands.effect.give.failed")).create();
        }
        if (targets.size() == 1) {
            command.getSource().sendSuccess(Component.translatable("commands.effect.lockdown.success.single", effect.value().getDisplayName(), targets.iterator().next().getDisplayName(), j / 20), true);
        } else {
            command.getSource().sendSuccess(Component.translatable("commands.effect.lockdown.success.multiple", effect.value().getDisplayName(), targets.size(), j / 20), true);
        }
        return i;
    }
}
