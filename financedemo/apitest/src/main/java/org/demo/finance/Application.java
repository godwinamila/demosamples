package org.demo.finance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class Application {

    private String clientId = "dE0dssq50LDJ7n8zYypCcjJ5wh4a";
    private String secret = "nCmmGTvwfmaWgjROX7pXj3Ipkbca";
    private String apiURL = "http://192.168.99.1:8280/TestAPI/1.0.0/GetCountries";

    private static Log LOGGER = LogFactory.getLog(Application.class);

    public static void main(String[] args) {
        Application application = new Application();
        application.invokeApi();
    }

    private void invokeApi() {

        AccessTokenUtility tokenUtility = new AccessTokenUtility();
        String accessToken = null;
        try {
            accessToken = tokenUtility.getAccessToken(clientId, secret);
            APIUtility apiUtility = new APIUtility();
            String response = apiUtility.get(apiURL, accessToken);
            LOGGER.info("API Response: " + response);
        } catch (IOException e) {
            LOGGER.error("Error occurred while invoking API.", e);
        }
        System.out.println(accessToken);
    }

}
