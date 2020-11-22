import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONObject;
public class json{
    public static void main(String args[])
    {
        try
        {
            String api="https://api.covid19india.org/state_district_wise.json";
            URL url = new URL(api);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            System.out.println(responsecode);

            if(responsecode != 200)
                throw new RuntimeException("HttpResponseCode: " +responsecode);
            else
            {
                Scanner sc = new Scanner(url.openStream());
                String inline = null;
                while(sc.hasNext())
                {
                    inline+=sc.nextLine();
                }
                System.out.println(inline);

                sc.close();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
