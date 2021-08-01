package com.zms.mongo.utils;

import org.bson.Document;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author : zms
 * @create : 2021/7/31
 * @desc : 二维坐标生成工具类
 */

@SuppressWarnings("SameParameterValue")
public abstract class GeoGraphicalPointGenerateUtil {

    /**
     * @author : zms
     * @create : 2021/7/31
     * @desc : 经纬度
     */

    public static List<Double> generatePoint(){
        String longStr = randomLnt(130d,140d);
        Double lnt = Double.valueOf(longStr);
        String latStr = randomLat(30d, 40d);
        Double lat = Double.valueOf(latStr);
        return Arrays.asList(lnt,lat);
    }


    public static Document generatePointDocument(){
        Document document = new Document();
        List<Double> point = generatePoint();
        document.append("lnt",point.get(0));
        document.append("lat",point.get(1));
        return document;
    }

    /**
     * @author : zms
     * @create : 2021/7/31
     * @desc : 生成纬度
     */
    private static String randomLat(double minLat, double maxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (maxLat - minLat) + minLat);
        return db.setScale(14, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * @author : zms
     * @create : 2021/7/31
     * @desc : 生成经度
     */

    private static String randomLnt(double minLon, double maxLon) {
        BigDecimal db = new BigDecimal(Math.random() * (maxLon - minLon) + minLon);
        return db.setScale(14, BigDecimal.ROUND_HALF_UP).toString();
    }


}
