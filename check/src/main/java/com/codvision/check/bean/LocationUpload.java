package com.codvision.check.bean;

/**
 * Project: Modules
 * Des:
 *
 * @author xujichang
 * created by 2018/7/23 - 2:39 PM
 */
public class LocationUpload {
    private String key;
    private String type;
    private double lat;
    private double lng;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
