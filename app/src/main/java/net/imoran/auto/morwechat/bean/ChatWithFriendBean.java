package net.imoran.auto.morwechat.bean;

import android.util.Log;

/**
 * Created by xinhuashi on 2018/8/2.
 */

public class ChatWithFriendBean {
    private String baseAddressUrl = "http://restapi.amap.com/v3/staticmap?markers=mid,0xFF0000,A:116.37359,39.92437&key=352c8f38a730448b4b21d35f355a3bdb";
    private String userName;
    private String content;
    private boolean isFromMsg;
    private String mapUrl;
    private String OriContent;
    private String label;
    private String poiname;
    private UserLocationInfo userLocationInfo;

    public ChatWithFriendBean() {
        userLocationInfo = new UserLocationInfo();
    }


    public UserLocationInfo getLocationInfo() {
        return userLocationInfo;
    }


    public void setLocationInfo(String latitude,String longitude) {
        if (latitude != null && longitude != null) {
            userLocationInfo.setLatitude(Float.valueOf(latitude));
            userLocationInfo.setLongitude(Float.valueOf(longitude));
            this.mapUrl = baseAddressUrl.replace("A:116.37359,39.92437","A:" + longitude +","+ latitude);
        }
    }

    public String getOriContent() {
        return OriContent;
    }

    public void setOriContent(String mOriContent) {
        if (mOriContent == null || "".equals(mOriContent) ) return;
        label = mOriContent.substring(mOriContent.indexOf("label") +7, mOriContent.indexOf("maptype") - 2);
        poiname = mOriContent.substring(mOriContent.indexOf("poiname") + 9, mOriContent.indexOf("poiid")-2);
        OriContent = mOriContent;
    }

    public String getLabel() {
        return label;
    }

    public String getPoiname() {
        return poiname;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPoiname(String poiname) {
        this.poiname = poiname;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        if(mapUrl == null) {
           this.mapUrl = null;
        }else if ("".equals(mapUrl)) {
            this.mapUrl = "";
        }else {
            String[] split = mapUrl.split("=");
            String[] split1 = split[1].split(",");
            if (split1.length <2) return;
            userLocationInfo.setLongitude(Float.valueOf(split1[1]));
            userLocationInfo.setLatitude(Float.valueOf(split1[0]));
            this.mapUrl = baseAddressUrl.replace("A:116.37359,39.92437","A:" + split1[1]+","+split1[0]);
            Log.e("heheha","split11" + split1[0] + "split2" + split1[1]);
        }
        Log.e("heheha","split11mapUrl" + this.mapUrl);
    }

    public boolean isFromMsg() {
        return isFromMsg;
    }

    public void setFromMsg(boolean fromMsg) {
        isFromMsg = fromMsg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static class UserLocationInfo{
        private float longitude;
        private float latitude;

        public float getLongitude() {
            return longitude;
        }

        public void setLongitude(float longitude) {
            this.longitude = longitude;
        }

        public float getLatitude() {
            return latitude;
        }

        public void setLatitude(float latitude) {
            this.latitude = latitude;
        }
    }
}
