package cn.zlianpay.common.core.pays.xunhupay;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpUtils {

    public static String httpget(String url, JSONObject jsonObject) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (!jsonObject.isEmpty()) {
                jsonObject.forEach((k, v) -> {
                    uriBuilder.addParameter(k, v.toString());
                });
            }

            HttpGet httpget = new HttpGet(uriBuilder.toString());
            try (CloseableHttpClient httpclient = HttpClients.createDefault();
                 CloseableHttpResponse execute = httpclient.execute(httpget);) {
                HttpEntity entity = execute.getEntity();
                String s = EntityUtils.toString(entity);
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httppostjson(String url, String jsonObject) {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
            httpPost.setEntity(stringEntity);
            try (CloseableHttpClient httpclient = HttpClients.createDefault();
                 CloseableHttpResponse execute = httpclient.execute(httpPost);) {
                HttpEntity entity = execute.getEntity();
                String s = EntityUtils.toString(entity);
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String httppost(String url, JSONObject jsonObject) {
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> objects = new ArrayList<>();
            jsonObject.forEach((k, v) -> {
                BasicNameValuePair basicNameValuePair = new BasicNameValuePair(k, v.toString());
                objects.add(basicNameValuePair);
            });
            StringEntity stringEntity = new UrlEncodedFormEntity(objects, "utf-8");
            httpPost.setEntity(stringEntity);
            try (CloseableHttpClient httpclient = HttpClients.createDefault();
                 CloseableHttpResponse execute = httpclient.execute(httpPost);) {
                HttpEntity entity = execute.getEntity();
                String s = EntityUtils.toString(entity);
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
