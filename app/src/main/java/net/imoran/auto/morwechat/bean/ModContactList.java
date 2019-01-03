package net.imoran.auto.morwechat.bean;

import java.util.List;

/**
 * Created by xinhuashi on 2018/7/26.
 */

public class ModContactList {
    private String UserName;
    private String NickName;
    private int Sex;
    private int HeadImgUpdateFlag;
    private int ContactType;
    private String Alias;
    private String ChatRoomOwner;
    private String HeadImgUrl;
    private int ContactFlag;
    private int MemberCount;
    private List<MemberList> MemberList;
    private int HideInputBarFlag;
    private String Signature;
    private int VerifyFlag;
    private String RemarkName;
    private int Statues;
    private long AttrStatus;
    private String Province;
    private String City;
    private int SnsFlag;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public int getHeadImgUpdateFlag() {
        return HeadImgUpdateFlag;
    }

    public void setHeadImgUpdateFlag(int headImgUpdateFlag) {
        HeadImgUpdateFlag = headImgUpdateFlag;
    }

    public int getContactType() {
        return ContactType;
    }

    public void setContactType(int contactType) {
        ContactType = contactType;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getChatRoomOwner() {
        return ChatRoomOwner;
    }

    public void setChatRoomOwner(String chatRoomOwner) {
        ChatRoomOwner = chatRoomOwner;
    }

    public String getHeadImgUrl() {
        return HeadImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        HeadImgUrl = headImgUrl;
    }

    public int getContactFlag() {
        return ContactFlag;
    }

    public void setContactFlag(int contactFlag) {
        ContactFlag = contactFlag;
    }

    public int getMemberCount() {
        return MemberCount;
    }

    public void setMemberCount(int memberCount) {
        MemberCount = memberCount;
    }

    public List<MemberList> getMemberList() {
        return MemberList;
    }

    public void setMemberList(List<MemberList> memberList) {
        MemberList = memberList;
    }

    public int getHideInputBarFlag() {
        return HideInputBarFlag;
    }

    public void setHideInputBarFlag(int hideInputBarFlag) {
        HideInputBarFlag = hideInputBarFlag;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public int getVerifyFlag() {
        return VerifyFlag;
    }

    public void setVerifyFlag(int verifyFlag) {
        VerifyFlag = verifyFlag;
    }

    public String getRemarkName() {
        return RemarkName;
    }

    public void setRemarkName(String remarkName) {
        RemarkName = remarkName;
    }

    public int getStatues() {
        return Statues;
    }

    public void setStatues(int statues) {
        Statues = statues;
    }

    public long getAttrStatus() {
        return AttrStatus;
    }

    public void setAttrStatus(long attrStatus) {
        AttrStatus = attrStatus;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public int getSnsFlag() {
        return SnsFlag;
    }

    public void setSnsFlag(int snsFlag) {
        SnsFlag = snsFlag;
    }

    public String getKeyWord() {
        return KeyWord;
    }

    public void setKeyWord(String keyWord) {
        KeyWord = keyWord;
    }

    private String KeyWord;
}
