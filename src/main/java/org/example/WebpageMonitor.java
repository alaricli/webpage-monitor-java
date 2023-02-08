package org.example;

import java.util.ArrayList;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONObject;

public class WebpageMonitor {
    private static boolean running = true;
    private static String webhook;

    public static void setRunning(boolean value) {
        running = value;
    }

    public static void setWebhook(String value) {
        webhook = value;
    }

    public static void main() throws IOException {
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://arcteryx.com/ca/en/shop/bird-head-toque");
        urls.add("https://arcteryx.com/ca/en/shop/grotto-toque");
        urls.add("https://arcteryx.com/ca/en/shop/bird-word-toque");

        while (running) {
            for (String url : urls) {
                Document html = Jsoup.connect(url).get();
                Elements buttonElements = html.select("button");
                boolean restocked = false;
                for (Element button : buttonElements) {
                    if (button.text().equals("Add To Cart")) {
                        restocked = true;
                        break;
                    }
                }

                if (restocked) {
                    JSONObject payload = new JSONObject();
                    payload.put("content", "Restocked: " + url);
                    try {
                        HttpResponse<JsonNode> response = Unirest.post(webhook)
                                .header("Content-Type", "application/json")
                                .body(payload)
                                .asJson();
                        if (response.getStatus() != 200) {
                            System.out.println("Failed to send payload to webhook. HTTP status code: " + response.getStatus());
                        }
                    } catch (UnirestException e) {
                        System.out.println("Failed to send payload to webhook. Error message: " + e.getMessage());
                    }
                }
            }
            try {
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {

            }
        }
    }
}
