package me.kermx.incomehistory;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

public class PaymentHistoryCommand implements CommandExecutor {
    private final IncomeHistory plugin;
    private final DecimalFormat decimalFormat;
    public PaymentHistoryCommand(IncomeHistory plugin){
        this.plugin = plugin;
        this.decimalFormat = new DecimalFormat("#.##");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] strings) {
        // Check if a username argument is provided
        if (strings.length > 0) {
            String targetUsername = strings[0];
            Player targetPlayer = plugin.getServer().getPlayer(targetUsername);

            // Check if the provided username corresponds to an online player
            if (targetPlayer != null) {
                UUID targetPlayerID = targetPlayer.getUniqueId();
                double paymentAmount = plugin.getPlayerPaymentAmount(targetPlayerID);
                String roundedPaymentAmount = decimalFormat.format(paymentAmount);
                sender.sendMessage("Payment info for " + targetPlayer.getName() + " over the last hour: " + roundedPaymentAmount);
            } else {
                sender.sendMessage("Player '" + targetUsername + "' is not online.");
            }
        } else if (sender instanceof Player) {
            // No argument provided, display the sender's payment history
            UUID playerID = ((Player) sender).getUniqueId();
            double paymentAmount = plugin.getPlayerPaymentAmount(playerID);
            String roundedPaymentAmount = decimalFormat.format(paymentAmount);
            sender.sendMessage("Your payment over the last hour: " + roundedPaymentAmount);
        } else {
            sender.sendMessage("Usage: /paymentinfo <username>");
        }

        return true;
    }
}