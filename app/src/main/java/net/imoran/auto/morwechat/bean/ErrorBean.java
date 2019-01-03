package net.imoran.auto.morwechat.bean;

/**
 * Created by xinhuashi on 2018/7/25.
 */

public class ErrorBean {
    private BaseResponse BaseResponse;

    public BaseResponse getBaseResponse() {
        return BaseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        BaseResponse = baseResponse;
    }

    public String getMsgID() {
        return MsgID;
    }

    public void setMsgID(String msgID) {
        MsgID = msgID;
    }

    private String MsgID;
}
