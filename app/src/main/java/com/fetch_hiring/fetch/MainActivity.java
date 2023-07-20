package com.fetch_hiring.fetch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //to display on the UI
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    private void fetchData() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String dataJsonString = getDataFromUrl("https://fetch-hiring.s3.amazonaws.com/hiring.json");
            if (dataJsonString != null) {
                // Process the retrieved data here
                //Log.d(TAG, dataJsonString); - this was to check if the json data was fetched properly

                List<DataItem> dataItems = parseJson(dataJsonString);

                if (dataItems != null) {
                    // Apply grouping, sorting, and filtering

                    //displayItems(dataItems); - not needed after adding UI aspect of app

                    List<DataItem> sortedAndFilteredItems = processItems(dataItems);

                    runOnUiThread(() -> {
                        dataAdapter = new DataAdapter(sortedAndFilteredItems);
                        recyclerView.setAdapter(dataAdapter);
                    });
                }
            }
        });
    }

    private String getDataFromUrl(String urlString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String dataJsonString = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }

            dataJsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        return dataJsonString;
    }

    private List<DataItem> parseJson(String dataJsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<DataItem>>() {}.getType();
        return gson.fromJson(dataJsonString, type);
    }

    private List<DataItem> processItems(List<DataItem> dataItems) {
        // Group the items by "listId" filtering out "null" values
        List<DataItem> groupedItems = dataItems.stream()
                .filter(item -> item.getName() != null && !item.getName().isEmpty())
                .collect(Collectors.groupingBy(DataItem::getListId))
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // Sort the results first by "listId" then by "name"
        Collections.sort(groupedItems, Comparator
                .comparingInt(DataItem::getListId)
                .thenComparing(DataItem::getName));

        return groupedItems;
    }

    //This code was to display the sorted data in Logcat for testing

//    private void displayItems(List<DataItem> dataItems) {
//        // Group the items by "listId" filtering out "null" values
//        List<DataItem> groupedItems = dataItems.stream()
//                .filter(item -> item.getName() != null && !item.getName().isEmpty())
//                .collect(Collectors.groupingBy(DataItem::getListId))
//                .values()
//                .stream()
//                .flatMap(List::stream)
//                .collect(Collectors.toList());
//
//        // Sort the results first by "listId" then by "name"
//        Collections.sort(groupedItems, Comparator
//                .comparingInt(DataItem::getListId)
//                .thenComparing(DataItem::getName));
//
//        // Print the grouped and sorted items
//        for (DataItem item : groupedItems) {
//            Log.d(TAG, "listId: " + item.getListId() + ", name: " + item.getName());
//        }
//    }
}