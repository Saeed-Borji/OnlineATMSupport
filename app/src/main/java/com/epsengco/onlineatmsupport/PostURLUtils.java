package com.epsengco.onlineatmsupport;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PostURLUtils {
    //private static final String BASE_URL = "http://192.168.1.62:8585/api";
    private static final String BASE_URL = "http://epsama.com/api";
    private static final String API_Key = "SjGGeR4NxC4UXBquXn8KFsSyRFDc4IKABbWJbQT8V/Xbc6xl/9bCwVP1Qh3XnwoQUxPSRmus893SvDxsewxXDA==";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.removeAllHeaders();
        client.addHeader("API-Key", API_Key);
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

        public static void Get(String url, RequestParams params , AsyncHttpResponseHandler responseHandler) {
        client.removeAllHeaders();
        client.addHeader("API-Key", API_Key );
        client.get(getAbsoluteUrl(url),params,responseHandler);
    }
}
