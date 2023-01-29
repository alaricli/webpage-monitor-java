package org.example;

import java.util.ArrayList;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.mashape.unirest.http.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class WebpageMonitor {
    public static void main(String[] args) throws IOException {
        // URL of the webpage to monitor
        ArrayList<String> urls = new ArrayList<String>();
        urls.add("https://arcteryx.com/ca/en/shop/bird-head-toque");
        urls.add("https://arcteryx.com/ca/en/shop/grotto-toque");
        urls.add("https://arcteryx.com/ca/en/shop/bird-word-toque");

        String webhook = "https://discord.com/api/webhooks/1068074137995706369/NJHQM0qaMp2iVY4EqmYZXhoCuLiH_mzbSvs3QIvv-JZ0_PJiCbXNMiK2yQHPq17WKZpg";

        while (true) {
            for (String url : urls) {
                // Connect to the webpage
                Document html = Jsoup.connect(url).get();
                // Find the button element
                Elements buttonElements = html.select("button");
                boolean flag = false;
                JSONObject payload = new JSONObject();

                for (Element button : buttonElements) {
                    if (button.text().equals("Add To Cart")) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    payload.put("content", "Restocked: " + url);
                }

                try {
                    HttpResponse<JsonNode> jsonResponse = Unirest.post(webhook)
                            .header("Content-Type", "application/json")
                            .body(payload)
                            .asJson();
                    if (jsonResponse.getStatus() != 200) {
                        System.out.println("Failed to send payloads to webhook. HTTP status code: " + jsonResponse.getStatus());
                    }
                } catch (UnirestException e) {
                    System.out.println("Failed to send payloads to webhook. Error message: " + e.getMessage());
                }
            }
            try {
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {

            }
        }
    }
}
