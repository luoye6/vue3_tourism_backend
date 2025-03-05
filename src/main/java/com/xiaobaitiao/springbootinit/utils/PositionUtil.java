package com.xiaobaitiao.springbootinit.utils;
 
/**
 * 坐标位置相关util
 */
public class PositionUtil {
 
    /**
     * 赤道半径（单位：米）
     */
    private static final double EQUATOR_RADIUS = 6378137;
 
    /**
     * 方法一：（反余弦计算方式）
     *
     * @param longitude1 第一个点的经度
     * @param latitude1  第一个点的纬度
     * @param longitude2 第二个点的经度
     * @param latitude2  第二个点的纬度
     * @return 返回距离，单位km
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lon1 = Math.toRadians(longitude1);
        double lon2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lon1 - lon2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘赤道半径, 返回单位: 米
        s = s * EQUATOR_RADIUS / 1000.0;
        // 保留两位小数
        return Double.parseDouble(String.format("%.2f", s));
    }
 
}