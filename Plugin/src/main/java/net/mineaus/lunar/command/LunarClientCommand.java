package net.mineaus.lunar.command;

import net.mineaus.lunar.LunarClientPlugin;
import net.mineaus.lunar.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LunarClientCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            User targetData = LunarClientPlugin.getApi().getUserManager().getPlayerData(target);

            if (target != null) {
                if (targetData.isLunarClient()) {
                    sender.sendMessage(ChatColor.GREEN + target.getName() + " is on Lunar Client.");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + target.getName() + " is not on Lunar Client.");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "The player '" + args[0] + "' is not online.");
                return false;
            }
        } else {
            StringBuilder sb = new StringBuilder(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString()).append("-------").append(ChatColor.AQUA).append(" Lunar Client").append(ChatColor.AQUA + " Users ").append(ChatColor.GRAY).append(ChatColor.STRIKETHROUGH.toString()).append(" -------\n");

            int amount = LunarClientPlugin.getApi().getUserManager().getPlayerDataMap().size();

            if (amount > 0) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    User data = LunarClientPlugin.getApi().getUserManager().getPlayerData(player);

                    if (data != null && data.isLunarClient()) {
                        sb.append(ChatColor.WHITE).append(data.getName()).append("\n");
                    }
                }
            } else {
                sb.append(ChatColor.RED).append("There is no one currently using the client.").append("\n");
            }

            sb.append(ChatColor.GRAY).append(ChatColor.STRIKETHROUGH.toString()).append("------------------------------");
            sender.sendMessage(sb.toString());
            return true;
        }
    }
}