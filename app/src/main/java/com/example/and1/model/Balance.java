package com.example.and1.model;

public class Balance {
    private Double money;
    private String userId;

    public Balance(Double money, String userId) {
        this.money = 0.00;
        this.userId = userId;
    }
    public Balance(){}

    public Double getmoney() {
        return money;
    }

    public void setmoney(Double money) {
        this.money = money;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return  "money=" + money +
                ", userId='" + userId + '\'' +
                '}';
    }
}
