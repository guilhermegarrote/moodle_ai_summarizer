package org.moodlescrapping.client;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple client for interacting with a Moodle-based website using HTTP requests.
 * <p>
 * This class handles authentication and session persistence via cookies,
 * allowing subsequent requests to be made as an authenticated user.
 */
public class MoodleClient {

    /**
     * Stores session cookies used to maintain authentication state
     * across multiple HTTP requests.
     */
    Map<String, String> cookies = new HashMap<>();

    /**
     * Logs into the Moodle platform using the provided credentials.
     * <p>
     * This method performs:
     * <ol>
     *     <li>A GET request to retrieve the login page and session cookies</li>
     *     <li>Extraction of the login token (CSRF protection)</li>
     *     <li>A POST request with credentials and token</li>
     *     <li>Storage of updated session cookies from the response</li>
     * </ol>
     *
     * @param user the username used for authentication
     * @param pass the password used for authentication
     * @throws Exception if any network or parsing error occurs
     */
    public void login(String user, String pass) throws Exception {
        Connection.Response page = Jsoup.connect("https://fatecead.cps.sp.gov.br/login/index.php")
                .userAgent("Mozilla/5.0")
                .method(Connection.Method.GET)
                .execute();

        cookies.putAll(page.cookies());

        Document doc = page.parse();
        String token = doc.select("input[name=logintoken]").attr("value");

        Connection.Response res = Jsoup.connect("https://fatecead.cps.sp.gov.br/login/index.php")
                .cookies(cookies)
                .data("username", user)
                .data("password", pass)
                .data("logintoken", token)
                .followRedirects(false)
                .method(Connection.Method.POST)
                .execute();

        Map<String, String> newCookies = new HashMap<>();

        for (String header : res.headers("Set-Cookie")) {
            String[] parts = header.split(";", 2)[0].split("=", 2);
            if (parts.length == 2) {
                newCookies.put(parts[0], parts[1]);
            }
        }

        cookies.putAll(newCookies);
    }

    /**
     * Sends an authenticated GET request to the specified URL.
     * <p>
     * This method reuses the cookies obtained during login to maintain
     * the authenticated session.
     *
     * @param url the target URL to fetch
     * @return the parsed HTML document from the response
     * @throws Exception if the request fails or the response cannot be parsed
     */
    public Document get(String url) throws Exception {
        return Jsoup.connect(url)
                .cookies(cookies)
                .userAgent("Mozilla/5.0")
                .get();
    }
}