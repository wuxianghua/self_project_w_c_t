package net.imoran.auto.morwechat.bean;

/**
 * Created by xinhuashi on 18/8/15.
 */

public class CheckAuthResponse extends BaseResponse {
    //扫描二维码并确认
    public static final String STATE_SCAN_OK = "200";
    //扫码未确认
    public static final String STATE_SCAN_NOT_YET = "201";

    public static final String STATE_SCAN_SCANED = "202";
    //二维码失效
    public static final String STATE_QRCODE_OUTOFDATE = "400";
    //查看二维码结果超时
    public static final String STATE_TIMEOUT = "888";

    private Data data;

    public class Data{
        private String code;

        public String getCode() {
            return code;
        }
    }

    public Data getData() {
        return data;
    }

    /**
     * 获取data 字段中 code字段的内容，没有的话则返回空字符串
     * @return
     */
    public String getDataCode(){
        if (data == null) {
            return "";
        }

        String code = data.getCode();
        if (code == null) {
            return "";
        } else {
            return code;
        }
    }
}
