package dev.xplate.create_security.misc;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.xplate.create_security.reg.SecurityEntityAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import static dev.xplate.create_security.CSSecurity.REG;

public class SecurityCommands {

    private static <T> RequiredArgumentBuilder<CommandSourceStack, T> arg(String name, ArgumentType<T> type) {
        return Commands.argument(name, type);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> getEndSicknessBuildup() {
        return Commands.literal("getEndSicknessBuildup")
                .then(arg("target", EntityArgument.entity()))
                    .executes(getEndSicknessCommand(true))
                .executes(getEndSicknessCommand(false));
    }

    private static Command<CommandSourceStack> getEndSicknessCommand(boolean hasArg) {
        return s -> {
            LivingEntity target;
            if (hasArg)
                target = ((LivingEntity) EntityArgument.getEntity(s, "target"));
            else
                target = Minecraft.getInstance().player;
            if (target == null) return 1;
            Long counter = target.getData(SecurityEntityAttachmentTypes.END_SICKNESS_COUNTER.get());
            String transKey = "chat.end_sick.command";
            REG.addRawLang(transKey, "%s has %s end sickness build-up.");
            s.getSource().sendSuccess(() -> Component.translatable(transKey, target.getDisplayName(), counter), true);
            return 0;
        };
    }
}
