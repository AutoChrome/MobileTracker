package com.example.a10108309.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Phone implements Parcelable {

    private String deviceName;
    private String brand;
    private String cpu;
    private String battery;
    private String resolution;
    private String chargerPort;
    private String mainCamera;
    private String size;
    private String weight;
    private String dimensions;
    private String screenType;
    private String nfcEnabled;
    private String internal;

    public Phone(String deviceName, String brand, String cpu, String battery, String resolution, String chargerPort, String mainCamera, String size, String weight, String dimensions, String screenType, String nfcEnabled, String internal){
        this.deviceName = deviceName;
        this.brand = brand;
        this.cpu = cpu;
        this.battery = battery;
        this.resolution = resolution;
        this.chargerPort = chargerPort;
        this.mainCamera = mainCamera;
        this.size = size;
        this.weight = weight;
        this.dimensions = dimensions;
        this.screenType = screenType;
        this.nfcEnabled = nfcEnabled;
        this.internal = internal;
    }

    public Phone(Parcel in){
        String[] data = new String[13];

        in.readStringArray(data);

        this.deviceName = data[0];
        this.brand = data[1];
        this.cpu = data[2];
        this.battery = data[3];
        this.resolution = data[4];
        this.chargerPort = data[5];
        this.mainCamera = data[6];
        this.size = data[7];
        this.weight = data[8];
        this.dimensions = data[9];
        this.screenType = data[10];
        this.nfcEnabled = data[11];
        this.internal = data[12];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.deviceName,
                this.brand,
                this.cpu,
                this.battery,
                this.resolution,
                this.chargerPort,
                this.mainCamera,
                this.size,
                this.weight,
                this.dimensions,
                this.screenType,
                this.nfcEnabled,
                this.internal
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Phone createFromParcel(Parcel in){
            return new Phone(in);
        }

        public Phone[] newArray(int size){
            return new Phone[size];
        }
    };

    /* Getters and setters */

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getChargerPort() {
        return chargerPort;
    }

    public void setChargerPort(String chargerPort) {
        this.chargerPort = chargerPort;
    }

    public String getMainCamera() {
        return mainCamera;
    }

    public void setMainCamera(String mainCamera) {
        this.mainCamera = mainCamera;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getNfcEnabled() {
        return nfcEnabled;
    }

    public void setNfcEnabled(String nfcEnabled) {
        this.nfcEnabled = nfcEnabled;
    }

    public String getInternal() {
        return internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }
}
