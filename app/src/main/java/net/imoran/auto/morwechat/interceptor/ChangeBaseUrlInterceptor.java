package net.imoran.auto.morwechat.interceptor;


import net.imoran.auto.morwechat.constant.URLConstant;

import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xinhua.shi on 2018/4/7.
 */

public class ChangeBaseUrlInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取request
        Request request = chain.request();
        //从request中获取原有的HttpUrl实例oldHttpUrl
        HttpUrl oldHttpUrl = request.url();
        //获取request的创建者builder
        Request.Builder builder = request.newBuilder();
        //从request中获取headers，通过给定的键url_name
        List<String> headerValues = request.headers("domin");
        if (headerValues != null && headerValues.size() > 0) {
            builder.removeHeader("domin");
            //匹配获得新的BaseUrl
            String headerValue = headerValues.get(0);
            HttpUrl newBaseUrl;
            if ("login".equals(headerValue)) {
                newBaseUrl = HttpUrl.parse(URLConstant.BASE_LOGIN_URL);
            } else if ("download".equals(headerValue)){
                newBaseUrl = HttpUrl.parse(URLConstant.BASE_DOWNLOAD_URL);
            }else if ("init".equals(headerValue)){
                newBaseUrl = HttpUrl.parse(URLConstant.BASE_INIT_URL);
            }else if ("syncheck".equals(headerValue)){
                newBaseUrl = HttpUrl.parse(URLConstant.BASE_SYNCCHECK_URL);
            }else if ("loginother".equals(headerValue)){
                newBaseUrl = HttpUrl.parse(URLConstant.BASE_LOGIN_OTHER_URL);
            }else if ("updatecontacts".equals(headerValue)){
                newBaseUrl = HttpUrl.parse(URLConstant.BASE_UPDATE_CONTACTS_URL);
            }else {
                newBaseUrl = HttpUrl.parse(URLConstant.BASE_URL);
            }
            //重建新的HttpUrl，修改需要修改的url部分
            HttpUrl newFullUrl = oldHttpUrl
                    .newBuilder()
                    .scheme(newBaseUrl.scheme())
                    .host(newBaseUrl.host())
                    .port(newBaseUrl.port())
                    .build();
            return chain.proceed(builder.url(newFullUrl).build());
        }

        return chain.proceed(request);
    }
}
