package com.edinburgh.ewireless.Class.SensorData;

import android.net.wifi.ScanResult;
import android.util.Log;

import java.util.List;

/**
 * Author: yijianzheng
 * Date: 13/04/2023 20:43
 * <p>
 * Notes:
 */
public class CurrWifi {

    private static List<ScanResult> wifiScanList = null;

    private static long timeStamp;

    public static List<ScanResult> getWifiScanList() {
        return wifiScanList;
    }

    public static void setWifiScanList(List<ScanResult> wifiScanList) {
        CurrWifi.wifiScanList = wifiScanList;
    }

    public static long getTimeStamp() {
        return timeStamp;
    }

    public static void setTimeStamp(long timeStamp) {
        CurrWifi.timeStamp = timeStamp;
    }
}
