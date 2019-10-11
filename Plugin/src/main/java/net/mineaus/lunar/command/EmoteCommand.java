package net.mineaus.lunar.command;

import net.mineaus.lunar.LunarClientPlugin;
import net.mineaus.lunar.api.type.Emote;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EmoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command doesn't support execution from console.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp()){
            return true;
        }

        if (!LunarClientPlugin.getApi().isAuthenticated(player)) {
            player.sendMessage(ChatColor.RED + "You must be on Lunar Client to perform emotes.");
            return true;
        }

        if (args.length == 1) {
            if (isInteger(args[0])) {
                int emoteId = Integer.valueOf(args[0]);

                if (emoteId == -1) {
                    try {
                        LunarClientPlugin.getApi().performEmote(player, -1, true);
                        return true;
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Error occurred while halting emote.");
                        return false;
                    }
                }

                Emote emote = Emote.getById(emoteId);

                if (emote == null) {
                    player.sendMessage(ChatColor.RED + "That is not valid emote!");
                    for (Emote emotes : Emote.values()) {
                        player.sendMessage(ChatColor.RED + " - " + emotes.name());
                    }
                    return false;
                }

                try {
                    LunarClientPlugin.getApi().performEmote(player, emote.getEmoteId(), true);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "Error occurred while performing emote.");
                    return false;
                }
            } else {
                if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("cancel")) {
                    try {
                        LunarClientPlugin.getApi().performEmote(player, -1, true);
                        return true;
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Error occurred while halting emote.");
                        return false;
                    }
                }

                Emote emote = Emote.getByName(args[0]);

                if (emote == null) {
                    player.sendMessage(ChatColor.RED + "That is not valid emote!");
                    for (Emote emotes : Emote.values()) {
                        player.sendMessage(ChatColor.RED + " - " + emotes.name());
                    }
                    return false;
                }

                try {
                    LunarClientPlugin.getApi().performEmote(player, emote.getEmoteId(), true);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "Error occurred while performing emote.");
                    return false;
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /emote <emote>");
        }
        return false;
    }

    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
