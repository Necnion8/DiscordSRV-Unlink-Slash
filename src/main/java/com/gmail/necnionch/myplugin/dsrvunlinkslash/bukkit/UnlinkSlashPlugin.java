package com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit;

import com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit.command.SlashCommand;
import com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit.command.UnlinkSlashCommand;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.JDA;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.Command;
import github.scarsz.discordsrv.dependencies.jda.api.requests.RestAction;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class UnlinkSlashPlugin extends JavaPlugin {
    private final DiscordSRV srv = DiscordSRV.getPlugin();
    private final Set<SlashCommand> commands = new HashSet<>();
    private final Set<Command> registeredCommands = new HashSet<>();

    @Override
    public void onEnable() {
        if (!srv.isEnabled()) {
            getLogger().warning("DiscordSRV is not enabled!");
            setEnabled(false);
            return;
        }

        if (srv.getJda() != null) {
            addCommands(srv.getJda());

        } else {
            // wait for jda
            getServer().getScheduler().runTaskTimer(this, task -> {
                if (!srv.isEnabled()) {
                    task.cancel();
                    setEnabled(false);
                    getLogger().warning("DiscordSRV is not enabled!");
                    return;
                }

                if (srv.getJda() != null) {
                    task.cancel();
                    addCommands(srv.getJda());
                }

            }, 20, 20);
        }
    }

    @Override
    public void onDisable() {
        if (srv.isEnabled() && srv.getJda() != null) {
            deleteCommands(srv.getJda());
        } else {
            deleteCommands(null);
        }
    }


    public DiscordSRV getSRV() {
        return srv;
    }

    private void addCommands(JDA jda) {
        UnlinkSlashCommand command = new UnlinkSlashCommand(this);
        commands.add(command);

        // register commands
        commands.forEach(jda::addEventListener);
        jda.updateCommands()
                .addCommands(commands.stream().map(SlashCommand::getData).collect(Collectors.toSet()))
                .queue(slashCommands -> {
                    getLogger().info("Added commands!");
                    registeredCommands.addAll(slashCommands);
                });
    }

    public void deleteCommands(@Nullable JDA jda) {
        if (jda != null) {
            try {
                commands.forEach(jda::removeEventListener);

                getLogger().info("Deleting " + registeredCommands.size() + " commands");
                CompletableFuture.allOf(registeredCommands.stream()
                                .map(Command::delete)
                                .map(RestAction::submit)
                        .toArray(CompletableFuture[]::new)).get();

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        commands.clear();
        registeredCommands.clear();
    }

}
