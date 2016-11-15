package com.example.mauricio.http.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mauricio.http.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mauricio on 15/11/16.
 */
public class BookHttp {

    public static final String LIVROS_URL_JSON = "https://raw.githubusercontent.com/nglauber/dominando_android2/master/livros_novatec.json";

    private static HttpURLConnection connect() throws IOException {
        URL url = new URL(LIVROS_URL_JSON);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout((int)TimeUnit.SECONDS.toMillis(10));
        connection.setConnectTimeout((int)TimeUnit.SECONDS.toMillis(15));
        connection.setDoInput(true);
        connection.setDoOutput(false);

        connection.connect();

        return connection;
    }

    public static boolean hasConnection(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static List<Book> loadJsonBooks() {

        try {
            HttpURLConnection connection = connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                JSONObject json = new JSONObject(bytesToString(inputStream));
                return jsonToBookObjects(json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<Book> jsonToBookObjects(JSONObject json) throws JSONException {

        List<Book> books = new ArrayList<>();

        String category;

        JSONArray jsonArray = json.getJSONArray("novatec");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonCategory = jsonArray.getJSONObject(i);

            category = jsonCategory.getString("categoria");
            JSONArray jsonBooks = jsonCategory.getJSONArray("livros");

            for (int j = 0; j < jsonBooks.length(); j++) {
                JSONObject jsonBook = jsonBooks.getJSONObject(j);
                books.add(new Book(
                        jsonBook.getString("titulo"),
                        category,
                        jsonBook.getString("autor"),
                        jsonBook.getInt("ano"),
                        jsonBook.getInt("paginas"),
                        jsonBook.getString("capa")));
            }
        }

        return books;
    }

    private static String bytesToString(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];

        ByteArrayOutputStream bigBuffer = new ByteArrayOutputStream();

        int readBytes;

        while ((readBytes = inputStream.read(buffer)) != -1)
            bigBuffer.write(buffer, 0, readBytes);

        return new String(bigBuffer.toByteArray(), "UTF8");
    }
}
