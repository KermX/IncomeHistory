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
        if (!(sender instanceof Player)){
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        Player player = (Player) sender;
        UUID playerID = player.getUniqueId();
        double paymentAmount = plugin.getPlayerPaymentAmount(playerID);
        String roundedPaymentAmount = decimalFormat.format(paymentAmount);
        player.sendMessage("Your payment over the last hour: " + roundedPaymentAmount);
        return true;
    }
}
