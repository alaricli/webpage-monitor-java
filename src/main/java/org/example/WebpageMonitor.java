package org.example;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebpageMonitor {
    public static void main(String[] args) throws IOException {
        // URL of the webpage to monitor
        String url = "https://arcteryx.com/ca/en/shop/bird-head-toque";

        // Connect to the webpage
        Document document = Jsoup.connect(url).get();

        // Find the button element
        Elements buttonElements = document.select("button");
        boolean flag = false;
        for (Element button : buttonElements) {
            if (button.text().equals("Add To Cart")) {
                flag = true;
                break;
            }
        }
        if (flag) {
            System.out.println("Add to cart button found!");
        } else {
            System.out.println("Add to cart button not found.");
        }
    }
}