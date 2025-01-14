package com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit;

import com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit.command.SlashCommand;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.SlashCommandEvent;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.Interaction;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.build.CommandData;
import github.scarsz.discordsrv.objects.managers.AccountLinkManager;

import java.util.UUID;

public class UnlinkSlashCommand extends SlashCommand {

    private final UnlinkSlashPlugin plugin;

    public UnlinkSlashCommand(UnlinkSlashPlugin plugin) {
        super(new CommandData("unlink", "Minecraftアカウントとのリンクを解除します"));
        this.plugin = plugin;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        AccountLinkManager accounts = plugin.getSRV().getAccountLinkManager();
        if (plugin.getSRV().getJda() == null || accounts == null) {
            event.reply("アカウントがリンクされているかどうかを確認できません。しばらくしてからもう一度お試しください").queue();
            return;
        }

        Interaction inter = event.getInteraction();

        String discordId = inter.getUser().getId();
        UUID uuid = accounts.getUuid(discordId);
        if (uuid != null) {
            accounts.unlink(discordId);
            plugin.getLogger().info("SRV Unlinked | Discord: " + discordId + ", Minecraft: " + uuid);
            event.reply("あなたのMinecraftアカウント(%uuid%)とのリンクが解除されました。".replace("%uuid%", uuid.toString())).queue();

        } else {
            event.reply("あなたのDiscordアカウントはMinecraftアカウントにリンクされていません。").queue();
        }
    }

}
