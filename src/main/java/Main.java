import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import org.json.JSONObject;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name : ");
        String name = scanner.nextLine();
        JSONObject json = new JSONObject();
        json.put("name", name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://dummy.restapiexample.com/api/v1/create"))
                .timeout(Duration.of(15, SECONDS))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        HttpClient client = HttpClient.newBuilder().build();

        while (true) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String statusCode = response.headers().firstValue("response").get();
            if (statusCode.equals("429")) {
                int resendAfter = Integer.parseInt(response.headers().firstValue("resend after").get()) + 5;
                System.out.println("Status code : " + statusCode + " , resend after " + resendAfter  + " seconds");
                Thread.sleep(resendAfter * 1000);
            } else {
                System.out.println(response.body());
                break;
            }
        }
    }
}
