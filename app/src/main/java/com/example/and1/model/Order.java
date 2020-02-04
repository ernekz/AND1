package com.example.and1.model;

public class Order {
    private String userId;
    private String bikeId;
    private String period;
    private double totalPrice;

    public Order(String userId, String bikeId, String period, double totalPrice) {
        this.userId = userId;
        this.bikeId = bikeId;
        this.period = period;
        this.totalPrice = totalPrice;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderCreate{" +
                "userId='" + userId + '\'' +
                ", bikeId='" + bikeId + '\'' +
                ", period='" + period + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
