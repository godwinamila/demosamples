package org.demo.finance;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

public class APIUtility {

    public String get(String url, String token) throws IOException {

        HttpClient httpClient = getHttpClient();
        GetMethod postRequest = new GetMethod(url);
        setAuthorizationHeader(postRequest, token);

        int response = httpClient.executeMethod(postRequest);
        if (response == HttpStatus.SC_OK) {
            String respStr = new String(postRequest.getResponseBody());
            return respStr;
        }
        return "";
    }

    private void setAuthorizationHeader(HttpMethodBase request, String header) {
        request.addRequestHeader("Authorization", "Bearer " + header);
    }

    private HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setParameter("http.connection.stalecheck", new Boolean(true));
        return httpClient;
    }

}
