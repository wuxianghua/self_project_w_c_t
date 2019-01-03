package net.imoran.auto.morwechat.manager;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import net.imoran.auto.morwechat.interceptor.ChangeBaseUrlInterceptor;
import net.imoran.auto.morwechat.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by xinhuashi on 2018/7/24.
 */

public class RetrofitUtils {
    public static final String BASE_URL = "https://login.weixin.qq.com";
    private Gson gson = new Gson();
    /**
     * 超时时间
     */
    public static final int TIMEOUT = 60;
    private static volatile RetrofitUtils mInstance;
    private Retrofit mRetrofit;

    public static RetrofitUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (RetrofitUtils.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtils(context);
                }
            }
        }
        return mInstance;
    }

    private RetrofitUtils(Context context) {
        initRetrofit(context);
    }

    /**
     * 初始化Retrofit
     */
    HashMap<String,List<Cookie>> map = new HashMap();
    private void initRetrofit(final Context context) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                map.put("http/weixin/qq",cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = map.get("http/weixin/qq");
                if (cookies != null) {
                    return cookies;
                }else {
                    return new ArrayList<Cookie>();
                }
            }
        });
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                Headers headers = response.headers();
                List<String> values = headers.values("Set-Cookie");
                Log.e("hehecookie",gson.toJson(headers));
                if (values != null && values.size() != 0) {
                    SharedPreferencesUtil.setDataList(context,"cookie",values);
                }
                return response;
            }
        });
        // 设置超时
        builder.retryOnConnectionFailure(true).connectTimeout(60,TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(new ChangeBaseUrlInterceptor());
        OkHttpClient client = builder.build();
        mRetrofit = new Retrofit.Builder()
                // 设置请求的域名
                .baseUrl(BASE_URL)
                // 设置解析转换工厂，用自己定义的
                .addConverterFactory(ResponseConvert.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    /**
     * 创建API
     */
    public <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }
}
