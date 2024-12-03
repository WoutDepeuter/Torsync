package org.ehb.wout.torsync;

import okhttp3.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class QbittorrentDownloader {

    private static final String QB_URL = "http://localhost:8081/api/v2";
    private static final String QB_USERNAME = "admin";
    private static final String QB_PASSWORD = "adminadmin";

    private final OkHttpClient client = new OkHttpClient();
    private String sessionCookie;

    public void downloadMagnetLink(String magnetLink) {
        try {
            authenticate();
            addTorrent(magnetLink);
            System.out.println("Magnet link added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void authenticate() throws Exception {
        RequestBody formBody = new FormBody.Builder()
                .add("username", QB_USERNAME)
                .add("password", QB_PASSWORD)
                .build();

        Request request = new Request.Builder()
                .url(QB_URL + "/auth/login")
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Authentication failed: " + response.message());
            }
            sessionCookie = response.header("Set-Cookie");
            if (sessionCookie == null) {
                throw new Exception("Session cookie not found.");
            }
        }
    }

    private void addTorrent(String magnetLink) throws Exception {
        String encodedMagnetLink = URLEncoder.encode(magnetLink, StandardCharsets.UTF_8);
        RequestBody formBody = new FormBody.Builder()
                .add("urls", encodedMagnetLink)
                .build();

        Request request = new Request.Builder()
                .url(QB_URL + "/torrents/add")
                .addHeader("Cookie", sessionCookie)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Response Code: " + response.code());
                System.err.println("Response Body: " + response.body().string());
                throw new Exception("Failed to add torrent: " + response.message());
            }
        }
    }

    public static void main(String[] args) {
        String magnetLink = "magnet:?xt=urn:btih:your_magnet_link_here";
        QbittorrentDownloader downloader = new QbittorrentDownloader();
        downloader.downloadMagnetLink(magnetLink);
    }
}
