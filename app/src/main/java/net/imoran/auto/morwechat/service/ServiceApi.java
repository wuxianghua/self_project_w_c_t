package net.imoran.auto.morwechat.service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by xinhuashi on 2018/7/24.
 */

public interface ServiceApi {
    @Headers({"domin:common"})
    @FormUrlEncoded
    @POST("/jslogin")
    Call<ResponseBody> getUuidMsg(@Field("appid")String appid,
                                  @Field("redirect_uri") String redirect_uri,
                                  @Field("fun")String fun,
                                  @Field("lang")String lang,
                                  @Field("_")long time);
    @Headers({"domin:common"})
    @GET("/qrcode/{uuid}")
    Call<ResponseBody> getQutrCode(@Path("uuid") String uuid);

    @Headers({"domin:common"})
    @GET("/cgi-bin/mmwebwx-bin/login")
    Call<ResponseBody> getLoginStatus(@Query("loginicon") boolean loginicon,
                                   @Query("uuid") String uuid,
                                   @Query("tip") int tip,
                                   @Query("r") long r,
                                   @Query("_") long time);

    @Headers({"domin:updatecontacts"})
    @POST("/api/wechat/status/change")
    Call<ResponseBody> updateContacts(@Body RequestBody body);

    @Headers({"domin:login"})
    @GET("/cgi-bin/mmwebwx-bin/webwxnewloginpage")
    Call<ResponseBody> webLoginWeixin(@Query("ticket") String ticket,
                                      @Query("uuid") String uuid,
                                      @Query("lang") String lang,
                                      @Query("scan") String scan,
                                      @Query("fun") String fun,
                                      @Query("r")long r);

    @Headers({"domin:loginother"})
    @GET("/cgi-bin/mmwebwx-bin/webwxnewloginpage")
    Call<ResponseBody> webLoginWeixinT(@Query("ticket") String ticket,
                                      @Query("uuid") String uuid,
                                      @Query("lang") String lang,
                                      @Query("scan") String scan,
                                       @Query("fun") String fun,
                                       @Query("r")long r);
    @Headers({"domin:init"})
    @POST("/cgi-bin/mmwebwx-bin/webwxinit")
    Call<ResponseBody> webinitWeixin(@Query("r") long t,
                                     @Query("pass_ticket") String pass_ticket,
                                     @Body RequestBody body);
    @Headers({"domin:init"})
    @GET("/cgi-bin/mmwebwx-bin/webwxgetcontact")
    Call<ResponseBody> getContactList(@Query("lang") String lang,
                                      @Query("pass_ticket")String pass_ticket,
                                      @Query("r") long r,
                                      @Query("seq")String seq,
                                      @Query("skey") String skey);

    @Headers({"domin:init"})
    @POST("/cgi-bin/mmwebwx-bin/webwxbatchgetcontact")
    Call<ResponseBody> getQunList(@Query("type")String ex,
                                  @Query("r") long r,
                                  @Query("pass_ticket") String pass_ticket);

    @Headers({"domin:init"})
    @POST("/cgi-bin/mmwebwx-bin/webwxstatusnotify")
    Call<ResponseBody> webWxStatusNotify(@Query("lang") String lang,
                                         @Query("pass_ticket")String pass_ticket,
                                         @Body RequestBody body);
    @Headers({"domin:init"})
    @GET("/cgi-bin/mmwebwx-bin/webwxgeticon")
    Call<ResponseBody> getSingleImage(@Query("seq") String seq,
                                      @Query("username")String username,
                                      @Query("skey") String skey);
    @Headers({"domin:syncheck"})
    @GET("/cgi-bin/mmwebwx-bin/synccheck")
    Call<ResponseBody> syncCheckMsg(@Query("deviceid") String deviceid,
                                    @Query("r")long r,
                                    @Query("sid") String sid,
                                    @Query("uin") String uin,
                                    @Query("skey")String skey,
                                    @Query("synckey") String synckey,
                                    @Query("_")long time);
    @Headers({"domin:init"})
    @POST("/cgi-bin/mmwebwx-bin/webwxsync")
    Call<ResponseBody> updateNewMsg(@Query("sid") String sid,
                                      @Query("skey")String skey,
                                      @Query("pass_ticket") String pass_ticket,
                                      @Body RequestBody requestBody);

    @Headers({"domin:init"})
    @POST("/cgi-bin/mmwebwx-bin/webwxsendmsg")
    Call<ResponseBody> sendMsg(@Query("pass_ticket") String pass_ticket,
                               @Body RequestBody requestBody);

    @Headers({"domin:init"})
    @GET("/cgi-bin/mmwebwx-bin/webwxgetvoice")
    Call<ResponseBody> getVoice(@Query("msgid") String msgid,
                                @Query ("skey") String skey);
}
