package me.kermx.incomehistory;

import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class IncomeHistory extends JavaPlugin implements Listener {

    private Map<UUID, List<PlayerPaymentData>> playerPayments = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("paymentinfo").setExecutor(new PaymentHistoryCommand(this));
        getServer().getScheduler().runTaskTimerAsynchronously(this, this::cleanupPayments, 0, 20 * 60);
    }

    @EventHandler
    public void onPlayerPayment(JobsPaymentEvent event) {
        Player player = event.getPlayer().getPlayer();
        if (player == null) return;

        Map<CurrencyType, Double> paymentMap = event.getPayment();
        double totalPayment = paymentMap.getOrDefault(CurrencyType.MONEY, 0.0);

        UUID playerId = player.getUniqueId();

        List<PlayerPaymentData> paymentDataList = playerPayments.computeIfAbsent(playerId, k -> new ArrayList<>());

        paymentDataList.add(new PlayerPaymentData(totalPayment, System.currentTimeMillis()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerPayments.remove(event.getPlayer().getUniqueId());
    }

    public void cleanupPayments() {
        long currentTime = System.currentTimeMillis();
        long oneHourAgo = currentTime - 3600000;

        Iterator<Map.Entry<UUID, List<PlayerPaymentData>>> iterator = playerPayments.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, List<PlayerPaymentData>> entry = iterator.next();
            UUID playerId = entry.getKey();
            List<PlayerPaymentData> paymentDataList = entry.getValue();

            paymentDataList.removeIf(paymentData -> paymentData.getPaymentTimestamp() < oneHourAgo);

            if (paymentDataList.isEmpty()) {
                iterator.remove();
            }
        }
    }

    public double getPlayerPaymentAmount(UUID playerId) {
        long oneHourAgo = System.currentTimeMillis() - 3600000;

        double totalPayment = 0.0;
        List<PlayerPaymentData> paymentDataList = playerPayments.get(playerId);

        if (paymentDataList != null) {
            for (PlayerPaymentData paymentData : paymentDataList) {
                if (paymentData.getPaymentTimestamp() >= oneHourAgo) {
                    totalPayment += paymentData.getPaymentAmount();
                }
            }
        }

        return totalPayment;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
