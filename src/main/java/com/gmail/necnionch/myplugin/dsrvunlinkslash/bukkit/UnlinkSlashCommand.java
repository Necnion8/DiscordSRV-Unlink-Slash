package com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit;

import com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit.command.SlashCommand;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.SlashCommandEvent;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.Interaction;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.build.CommandData;
import github.scarsz.discordsrv.objects.managers.AccountLinkManager;

public class UnlinkSlashCommand extends SlashCommand {

    private final AccountLinkManager accounts;

    public UnlinkSlashCommand(AccountLinkManager accounts) {
        super(new CommandData("unlink", "Minecraftアカウントとのリンクを解除します"));
        this.accounts = accounts;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Interaction inter = event.getInteraction();

        String discordId = inter.getUser().getId();
        if (accounts.getLinkedAccounts().containsKey(discordId)) {
            //
            System.out.println("unlinked");
            accounts.unlink(discordId);

        } else {
            System.out.println("no linking");

        }


    }

}
