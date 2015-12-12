package com.trojx.busdrivingtest.Util;

import com.trojx.busdrivingtest.domain.Point;

/**
 * Created by Administrator on 2015/12/12.
 */


public class WGS84_BD09 {
    /**
     * 原始经纬度转百度经纬度
     * @param lng GPS经度
     * @param lat GPS纬度
     * @return
     */
    public  static Point trans(double lng,double lat){
        Point point=new Point();
        point=CoordinateConversion.wgs_gcj_encrypts(lat, lng);
        Point point1=new Point();
        point1=CoordinateConversion.google_bd_encrypt(point.getLat(),point.getLng());
        return point1;
    }
}
