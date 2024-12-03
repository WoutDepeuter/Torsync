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
        String encodedMagnetLink = magnetLink;
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
        String magnetLink = "magnet:?xt=urn:btih:BC8973E7F59AB0444D2E7A90950B2F4FEFC4A0D7&dn=Cars+%282006%29+%5B3D%5D+%5BYTS.MX%5D&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=udp%3A%2F%2Fopen.tracker.cl%3A1337%2Fannounce&tr=udp%3A%2F%2Fp4p.arenabg.com%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.torrent.eu.org%3A451%2Fannounce&tr=udp%3A%2F%2Ftracker.dler.org%3A6969%2Fannounce&tr=udp%3A%2F%2Fopen.stealth.si%3A80%2Fannounce&tr=udp%3A%2F%2Fipv4.tracker.harry.lu%3A80%2Fannounce&tr=https%3A%2F%2Fopentracker.i2p.rocks%3A443%2Fannounce";
        QbittorrentDownloader downloader = new QbittorrentDownloader();
        downloader.downloadMagnetLink(magnetLink);
    }
}
