package de.fau.cs.mad.fablab.rest.server.pushservice;



import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class AndroidPushService {

    private static final String REGISTRATION_ID = "";
    private static final String API_KEY = "";

    public static void main(String[] args) throws IOException{
        System.out.println("Start Pushservice");

        PushContent pushContent = new PushContent();
        pushContent.addRegId(REGISTRATION_ID);
        pushContent.createData("Test Title", "Test Message");

        AndroidPushService androidPushService = new AndroidPushService();
        androidPushService.pushJson(API_KEY,pushContent);
    }

    public void pushJson(String apiKey, PushContent aPushContent) throws IOException{
        URL url = new URL("https://android.clients.google.com/gcm/send");
        HttpsURLConnection
                .setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.setUseCaches(false);
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("Authorization", "key=" + apiKey);

        ObjectMapper mapper = new ObjectMapper();
        DataOutputStream wr = new DataOutputStream(httpsURLConnection.getOutputStream());
        mapper.writeValue(wr, aPushContent);

        wr.flush();
        wr.close();

        int responseCode = httpsURLConnection.getResponseCode();
        System.out.println("Responsecode: " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                httpsURLConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());

    }



    private static class CustomizedHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
