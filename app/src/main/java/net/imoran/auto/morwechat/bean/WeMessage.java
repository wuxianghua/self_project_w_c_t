package net.imoran.auto.morwechat.bean;

/**
 * Created by xinhua.shi on 2018/8/9.
 */

import java.util.List;

public class WeMessage
{
    private BaseResponse BaseResponse;

    private int AddMsgCount;

    private List<AddMsgList> AddMsgList;

    private int ModContactCount;

    private List<ModContactList> ModContactList;

    private int DelContactCount;

    private List<DelContactList> DelContactList;

    private int ModChatRoomMemberCount;

    private List<String> ModChatRoomMemberList;

    private Profile Profile;

    private int ContinueFlag;

    private SyncKey SyncKey;

    private String SKey;

    private SyncKey SyncCheckKey;

    public void setBaseResponse(BaseResponse BaseResponse){
        this.BaseResponse = BaseResponse;
    }
    public BaseResponse getBaseResponse(){
        return this.BaseResponse;
    }
    public void setAddMsgCount(int AddMsgCount){
        this.AddMsgCount = AddMsgCount;
    }
    public int getAddMsgCount(){
        return this.AddMsgCount;
    }
    public void setAddMsgList(List<AddMsgList> AddMsgList){
        this.AddMsgList = AddMsgList;
    }
    public List<AddMsgList> getAddMsgList(){
        return this.AddMsgList;
    }
    public void setModContactCount(int ModContactCount){
        this.ModContactCount = ModContactCount;
    }
    public int getModContactCount(){
        return this.ModContactCount;
    }
    public void setModContactList(List<ModContactList> ModContactList){
        this.ModContactList = ModContactList;
    }
    public List<ModContactList> getModContactList(){
        return this.ModContactList;
    }
    public void setDelContactCount(int DelContactCount){
        this.DelContactCount = DelContactCount;
    }
    public int getDelContactCount(){
        return this.DelContactCount;
    }
    public void setDelContactList(List<DelContactList> DelContactList){
        this.DelContactList = DelContactList;
    }
    public List<DelContactList> getDelContactList(){
        return this.DelContactList;
    }
    public void setModChatRoomMemberCount(int ModChatRoomMemberCount){
        this.ModChatRoomMemberCount = ModChatRoomMemberCount;
    }
    public int getModChatRoomMemberCount(){
        return this.ModChatRoomMemberCount;
    }
    public void setModChatRoomMemberList(List<String> ModChatRoomMemberList){
        this.ModChatRoomMemberList = ModChatRoomMemberList;
    }
    public List<String> getModChatRoomMemberList(){
        return this.ModChatRoomMemberList;
    }
    public void setProfile(Profile Profile){
        this.Profile = Profile;
    }
    public Profile getProfile(){
        return this.Profile;
    }
    public void setContinueFlag(int ContinueFlag){
        this.ContinueFlag = ContinueFlag;
    }
    public int getContinueFlag(){
        return this.ContinueFlag;
    }
    public void setSyncKey(SyncKey SyncKey){
        this.SyncKey = SyncKey;
    }
    public SyncKey getSyncKey(){
        return this.SyncKey;
    }
    public void setSKey(String SKey){
        this.SKey = SKey;
    }
    public String getSKey(){
        return this.SKey;
    }
    public void setSyncCheckKey(SyncKey SyncCheckKey){
        this.SyncCheckKey = SyncCheckKey;
    }
    public SyncKey getSyncCheckKey(){
        return this.SyncCheckKey;
    }
}
