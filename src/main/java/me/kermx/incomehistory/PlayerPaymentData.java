package me.kermx.incomehistory;

public class PlayerPaymentData {
    private double paymentAmount;
    private long paymentTimestamp;

    public PlayerPaymentData(double paymentAmount, long paymentTimestamp){
        this.paymentAmount = paymentAmount;
        this.paymentTimestamp = paymentTimestamp;
    }
    public double getPaymentAmount(){
        return paymentAmount;
    }
    public long getPaymentTimestamp(){
        return paymentTimestamp;
    }
}
