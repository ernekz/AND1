package com.example.and1.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Order createFromParcel(Parcel in){
            return new Order(in);
        }

        public Order[] newArray(int size){
            return new Order[size];
        }
    };
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

    public Order(Parcel in) {
            this.bikeId = in.readString();
            this.period = in.readString();
            this.totalPrice = in.readDouble();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
