package com.ycc.customer1.customer1;

import com.alibaba.fastjson.JSONArray;
import com.ycc.customer1.customer1.http.HttpClientUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpClient {

    public static void main(String[] args) throws Exception {
        String sss ="123456";
        JSONArray jsonArray =new JSONArray();
        jsonArray.add(sss);
        HttpClientUtils.sendHttpPostJson("http://localhost:8762/str",jsonArray.toJSONString());
    }

}
