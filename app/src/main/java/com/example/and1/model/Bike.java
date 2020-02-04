package com.example.and1.model;

import android.media.Image;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Bike implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Bike createFromParcel(Parcel in){
            return new Bike(in);
        }

        public Bike[] newArray(int size){
            return new Bike[size];
        }
    };

    private String userId;
    private String bikeId;
    private boolean isTaken;
    private String type;
    private String location;
    private String from;
    private String until;
    private Double price;
    private String nImageUrl;
    private String name;


    public Bike(String userId,String bikeId, String type, String location, String from, String until, Double price
    ,String name, String nImageUrl){
        this.userId = userId;
        this.bikeId = bikeId;
        this.isTaken = false;
        this.type = type;
        this.location = location;
        this.from = from;
        this.until = until;
        this.price = price;
        this.name = name;
        this.nImageUrl = nImageUrl;
    }
    public Bike(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getnImageUrl() {
        return nImageUrl;
    }

    public void setnImageUrl(String nImageUrl) {
        this.nImageUrl = nImageUrl;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Bike type is " + type +
                " and located in " + location +
                " available from: " + from +
                " and until:" + until +
                ". Price for a day is " + price;
    }

    public Bike(Parcel in){
        this.userId = in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.isTaken = in.readBoolean();
        }
        this.bikeId = in.readString();
        this.type = in.readString();
        this.location = in.readString();
        this.from = in.readString();
        this.until = in.readString();
        this.price = in.readDouble();
        this.name = in.readString();
        this.nImageUrl = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //TODO Parcel for listItemDetail and then create the button in that class so ppl can actually rent it.

    }
}
