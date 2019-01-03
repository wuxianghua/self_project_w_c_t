package net.imoran.auto.morwechat.bean;

import android.support.v4.util.Pools;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by xinhua.shi on 2018/12/18.
 */
public class Latlng implements Serializable{
    private static final Pools.SynchronizedPool<Latlng> sPool
            = new Pools.SynchronizedPool(10);

    @Expose
    private String city;
    @Expose
    private String cityCode;


    @Expose
    private String province;

    @Expose
    private String district;

    @Expose
    private double latitude;
    @Expose
    private double longitude;

    private Map<String, Object> extData;

    //坐标系，0 为 WGS84，1为 GCJ02，2为BD09
    //本项目中默认使用高德的坐标系
    private int coordinate;

    private String poiName;
    private String address;

    /**
     * 判读是否是合法的地址
     *
     * @param latlng
     * @return
     */
    public static boolean isLocation(Latlng latlng) {
        if (latlng == null) {
            return false;
        }

        if (latlng.getLatitude() == 0
                && latlng.getLongitude() == 0) {
            return false;
        }

        return true;
    }


    private Latlng() {
        init();
    }

    private void init() {
        //默认为高德坐标系
        coordinate = 1;

        //坐标的经纬度默认都是 Double.MIN_VALUE
        latitude = Double.MIN_VALUE;
        longitude = Double.MIN_VALUE;

        extData = null;
    }

    public static Latlng obtain() {
        Latlng latlng = sPool.acquire();
        return (latlng == null) ? new Latlng() : latlng;
    }


    public boolean isNorthTo(Latlng latlng) {
        return latitude > latlng.getLatitude();
    }


    public boolean isWestTo(Latlng latlng) {
        return longitude > latlng.getLongitude();
    }

    public void recycle() {
        init();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    public Map<String, Object> getExtData() {
        return extData;
    }

    public void setExtData(Map<String, Object> extData) {
        this.extData = extData;
    }

    @Override
    public String toString() {
        return "Lat:" + latitude + ", longitude:" + longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
