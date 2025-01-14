package com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit.command;

import com.gmail.necnionch.myplugin.dsrvunlinkslash.bukkit.UnlinkSlashPlugin;
import github.scarsz.discordsrv.dependencies.jda.api.entities.ChannelType;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.SlashCommandEvent;
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
            event.reply("アカウントがリンクされているかどうかを確認できません。しばらくしてからもう一度お試しください")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        String discordId = event.getInteraction().getUser().getId();
        UUID uuid = accounts.getUuid(discordId);
        if (uuid != null) {
            accounts.unlink(discordId);
            plugin.getLogger().info("SRV Unlinked | Discord: " + discordId + ", Minecraft: " + uuid);
            event.reply("あなたのMinecraftアカウント(%uuid%)とのリンクが解除されました。".replace("%uuid%", uuid.toString()))
                    .setEphemeral(!ChannelType.PRIVATE.equals(event.getChannelType()))
                    .queue();

        } else {
            event.reply("あなたのDiscordアカウントはMinecraftアカウントにリンクされていません。")
                    .setEphemeral(true)
                    .queue();
        }
    }

}
