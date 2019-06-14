package com.example.a10108309.object;

public class PhoneImage {
    private String deviceName;
    private String url;

    public PhoneImage(String deviceName, String url){
        this.deviceName = deviceName;
        this.url = url;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getUrl() {
        return url;
    }
}
