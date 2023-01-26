package org.example;

import java.util.ArrayList;
import java.io.IOException;

import com.mashape.unirest.http.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import org.json.JSONObject;

public class WebpageMonitor {
    public static void main(String[] args) throws IOException {
        // URL of the webpage to monitor
        ArrayList<String> urls = new ArrayList<String>();
        urls.add("https://arcteryx.com/ca/en/shop/bird-head-toque");
        urls.add("https://arcteryx.com/ca/en/shop/grotto-toque");
        urls.add("https://arcteryx.com/ca/en/shop/bird-word-toque");
        urls.add("https://arcteryx.com/ca/en/shop/rho-lightweight-wool-toque");

        // loop for each item in the urls list
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
                payload.put("content", "Add to cart button found for: " + url);
                HttpResponse< JsonNode > jsonResponse = Unirest.post("https://discord.com/api/webhooks/1062994904382443570/rBDvD_8DVO_WyoUISZ85wsdqFjMe5_40cxwKq7_jBGp6Etu7CoZZKPLdHm1sI0qj43Lx")
                        .header("Content-Type", "application/json")
                        .body(payload)
                        .asJson();
            } else {
                payload.put("content", "Add to cart button not found for: " + url);
                HttpResponse< JsonNode > jsonResponse = Unirest.post("https://discord.com/api/webhooks/1062994904382443570/rBDvD_8DVO_WyoUISZ85wsdqFjMe5_40cxwKq7_jBGp6Etu7CoZZKPLdHm1sI0qj43Lx")
                        .header("Content-Type", "application/json")
                        .body(payload)
                        .asJson();
            }
        }
    }
}