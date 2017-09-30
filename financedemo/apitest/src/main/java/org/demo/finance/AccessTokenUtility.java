package org.demo.finance;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;

import java.io.IOException;

public class AccessTokenUtility {

    public String getAccessToken(String clientId, String secret) throws IOException {

        HttpClient httpClient = getHttpClient();
        PostMethod postRequest = new PostMethod("https://192.168.99.1:8243/token");
        postRequest.addParameter("grant_type", "client_credentials");

        String clientidsecret = clientId + ":" + secret;
        String value = new String(java.util.Base64.getEncoder().encode(clientidsecret.getBytes()));
        setAuthorizationHeader(postRequest, value);

        int response = httpClient.executeMethod(postRequest);
        if (response == HttpStatus.SC_OK) {
            String respStr = new String(postRequest.getResponseBody());
            JSONObject resultObj = new JSONObject(respStr);
            return resultObj.get("access_token").toString();
        }
        return "";
    }

    private void setAuthorizationHeader(HttpMethodBase request, String header) {

        request.addRequestHeader("Authorization", "Basic " + header);
    }

    private HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setParameter("http.connection.stalecheck", new Boolean(true));
        return httpClient;
    }

}
