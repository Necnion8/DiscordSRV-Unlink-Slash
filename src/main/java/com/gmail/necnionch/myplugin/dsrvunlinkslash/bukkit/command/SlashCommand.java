package com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit.command;

import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.SlashCommandEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.build.CommandData;

import javax.annotation.Nonnull;

public abstract class SlashCommand extends ListenerAdapter {

    private final CommandData data;

    public SlashCommand(CommandData data) {
        this.data = data;
    }

    public abstract void execute(SlashCommandEvent event);

    public String getName() {
        return data.getName();
    }

    public CommandData getData() {
        return data;
    }

    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        if (!getName().equals(event.getCommandString())) {  // FIXME: 名前の競合
            try {
                execute(event);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
