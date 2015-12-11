package com.trojx.busdrivingtest.domain;

import java.util.Date;

/**
 * Created by Administrator on 2015/12/11.
 */
public class Record {
    private  int SN;
    private Date date;
    private  float xAccel;
    private float yAccel;
    private  float zAccel;
    private float lng;
    private  float lat;
    private float xOrien;
    private float yOrien;
    private float zOrien;



    public int getSN() {
        return SN;
    }

    public void setSN(int SN) {
        this.SN = SN;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getxAccel() {
        return xAccel;
    }

    public void setxAccel(float xAccel) {
        this.xAccel = xAccel;
    }

    public float getyAccel() {
        return yAccel;
    }

    public void setyAccel(float yAccel) {
        this.yAccel = yAccel;
    }

    public float getzAccel() {
        return zAccel;
    }

    public void setzAccel(float zAccel) {
        this.zAccel = zAccel;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getxOrien() {
        return xOrien;
    }

    public void setxOrien(float xOrien) {
        this.xOrien = xOrien;
    }

    public float getyOrien() {
        return yOrien;
    }

    public void setyOrien(float yOrien) {
        this.yOrien = yOrien;
    }

    public float getzOrien() {
        return zOrien;
    }

    public void setzOrien(float zOrien) {
        this.zOrien = zOrien;
    }
}
