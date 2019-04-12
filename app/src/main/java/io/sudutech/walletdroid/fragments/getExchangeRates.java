import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class getExchangeRates {



    public static void main(String[] args) throws Exception {
        final String BASE= "SEK";



        String url = String.format("https://api.exchangeratesapi.io/latest?base=%s", BASE);
        URL obj = new URL(url);

        HttpURLConnection http =  (HttpURLConnection)obj.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("base",BASE);
        int ResponseCode=http.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + ResponseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(http.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }


}